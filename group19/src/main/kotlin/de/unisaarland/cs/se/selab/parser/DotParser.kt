package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.PrimaryRoadType
import de.unisaarland.cs.se.selab.map.Road
import de.unisaarland.cs.se.selab.map.SecondaryRoadType
import de.unisaarland.cs.se.selab.map.Vertex
import java.io.File

/**
 * parses attributes out of the provided dot files
 * and sets the map with those attributes
 */
class DotParser(val filePath: String) {

    val anyWhiteSpace = "[\\s]*"

    val parsedVertices: MutableList<Vertex> = mutableListOf()
    val parsedRoads: MutableList<Road> = mutableListOf()
    var countyName: String = ""

    val parsedSourceID: MutableList<Int> = mutableListOf()
    val parsedTargetID: MutableList<Int> = mutableListOf()

    val fileContents: String = File(filePath).readText(Charsets.UTF_8)
    var rightOfCurly: String = ""

    private val dotFileValidator: DotFileValidator = DotFileValidator(this.filePath, this)

    /**
     * parses the map's attributes, validates them (including the map itself)
     * and sets the map's attributes
     */
    fun parseMap(): Boolean {
        if (!this.parseMapName()) {
            return false
        }

        if (!this.parseVertices()) {
            return false
        }

        if (!this.parseEdges()) {
            return false
        }

        Map.setAttributes(parsedVertices.toList(), parsedRoads.toList(), countyName)
        Map.updateAdjacencyMatrix()
        this.parsedVertices.forEach { it.calculateConnectedVertices() }

        return dotFileValidator.validateMap()
    }

    /**
     * parses the map name out of the dot file
     */
    private fun parseMapName(): Boolean {
        // checks if there is content after the last "}"
        val nothingLeft = fileContents.split(Regex("\\}"))

        if (nothingLeft[1].replace(Regex(anyWhiteSpace), "") != "") {
            return false
        }

        // looks for all patterns that match: "digraph String {"
        val fileSplittedAtCurly = fileContents.split(Regex("\\{"))

        val leftOfCurly = fileSplittedAtCurly[0]
        this.rightOfCurly = fileSplittedAtCurly[1]

        // too inefficient for big patterns
        // use string split

        val digraphNameRegex = Regex("digraph[\\s]*[a-zA-Z][a-zA-Z_]*\\s")
        var digraphNameString = digraphNameRegex.find(leftOfCurly, 0)?.value ?: return false
        digraphNameString = digraphNameString.replace(Regex("digraph[\\s]*|[\\s]*"), "")

        val mapNameRegex = Regex("[a-zA-Z][a-zA-Z_]*")
        val mapName = mapNameRegex.find(digraphNameString, 0)?.value ?: return false
        this.countyName = mapName.replace(Regex(anyWhiteSpace), "")

        return true
    }

    /**
     * parses the vertices out of the dot file
     */
    private fun parseVertices(): Boolean {
        val validCharacters = "[0]|[1-9][0-9]*|[[a-zA-Z][a-zA-Z_]]".toRegex()
        // cuts the file to the part between the first "{ and the first number without a ";"

        val rightSideBracket: String = fileContents.split(Regex("\\{"))[1]
        val firstBracketFirstArrow: String = rightSideBracket.split(Regex("->"))[0]

        val idList = firstBracketFirstArrow.split(Regex(";")).toMutableList()
        parsedSourceID.add(idList.last().replace(Regex(anyWhiteSpace), "").toInt())
        idList.removeLast()

        // turns parsed ID strings into vertex objects and adds them to parsed vertices list if validation success.
        for (subexpression in idList) {
            val parsedVertexIDString = validCharacters.find(subexpression) ?: return false
            val vertexIDString: String = parsedVertexIDString.value.replace(Regex(anyWhiteSpace), "")
            val vertexID: Int = vertexIDString.toInt()
            val vertex = Vertex(vertexID)
            if (this.dotFileValidator.validateVertex(vertex)) {
                this.parsedVertices.add(vertex)
            } else {
                return false
            }
        }

        return true
    }

    /**
     * parses the edges out of the dot file
     */
    private fun parseEdges(): Boolean {
        val splittedByArrows = rightOfCurly.split("->").toMutableList()

        // looks for all entries that match: "Int -> Int [String];", values will be validated later on

        for (i in 1 until splittedByArrows.size) {
            val road: Road? = this.parseSingleEdge(splittedByArrows[i], i)
            if (road == null) {
                return false
            }
            if (!this.dotFileValidator.validateRoad(road)) {
                return false
            }
            this.parsedRoads.add(road)
        }

        return true
    }

    /**
     * parses a single edge out of the dot file
     */
    private fun parseSingleEdge(betweenArrows: String, index: Int): Road? {
        var road: Road?
        var nullReturn = false

        // splits the left side of the first "["
        val splitAtBracket = betweenArrows.split("[")
        val leftOfBracket = splitAtBracket[0]
        val rightOfBracket = splitAtBracket[1]

        // converts into int
        val targetVertexThis = leftOfBracket.replace(Regex(anyWhiteSpace), "")
        if (Regex("[0][0-9]+").containsMatchIn(targetVertexThis)) {
            nullReturn = true
        }

        val targetVertexID = targetVertexThis.toInt()

        if (targetVertexID < 0) {
            nullReturn = true
        }

        this.parsedTargetID.add(targetVertexID)

        // splits the right side of the closing "["
        val splitAtClosingBracket = rightOfBracket.split("];")
        val sourceVertexNext = splitAtClosingBracket[1].replace(Regex(anyWhiteSpace), "")

        if (Regex("[0][0-9]+").containsMatchIn(sourceVertexNext)) {
            nullReturn = true
        }

        if (sourceVertexNext != "}") {
            val sourceVertexIDNext = sourceVertexNext.toInt()
            if (sourceVertexIDNext < 0) {
                nullReturn = true
            } else {
                this.parsedSourceID.add(sourceVertexIDNext)
            }
        }

        if (nullReturn) {
            return null
        }

        val roadAttributes = splitAtClosingBracket[0].replace(Regex(anyWhiteSpace), "")

        val villageName: String? = this.parseVillageName(roadAttributes)
        val roadName: String? = this.parseRoadName(roadAttributes)
        val heightLimit: Int? = this.parseHeightLimit(roadAttributes)
        val weight: Double? = this.parseWeight(roadAttributes)
        val primaryType: PrimaryRoadType? =
            this.parsePrimaryType(roadAttributes)
        val secondaryType: SecondaryRoadType? =
            this.parseSecondaryType(roadAttributes)

        if (villageName == null || roadName == null || heightLimit == null) {
            return null
        } else if (primaryType == null || secondaryType == null || weight == null) {
            return null
        }

        val sourceVertex = this.parsedVertices.filter { it.id == parsedSourceID[index - 1] }[0]
        val targetVertex = this.parsedVertices.filter { it.id == parsedTargetID[index - 1] }[0]

        road = Road(
            sourceVertex,
            targetVertex,
            primaryType,
            secondaryType,
            roadName,
            villageName,
            weight,
            heightLimit
        )

        return road
    }

    /**
     * parses the village name out of the dot file
     */
    private fun parseVillageName(roadDeclaration: String): String? {
        // looks for all entries that match: "village=String;" with arbitrary amount of whitespaces betw. equal sign
        val regexPattern: Regex = Regex("village=[a-zA-z][a-zA-Z_]*")

        // filters all entries that coincide with that pattern
        val villageNameExpression: MatchResult = regexPattern.find(roadDeclaration, 0) ?: return null

        // removes = and last ;
        val villageName: String = villageNameExpression.value.replace(
            Regex("village="),
            ""
        )

        return villageName
    }

    /**
     * parses the road's name out of the dot file
     */
    private fun parseRoadName(roadDeclaration: String): String? {
        // looks for all entries that match: "name=String" with arbitrary amount of whitespaces betw. equal sign
        val regexPattern: Regex = Regex("name=[a-zA-z][a-zA-Z_]*")

        // filters all entries that coincide with that pattern
        val roadNameExpression: MatchResult = regexPattern.find(roadDeclaration, 0) ?: return null

        // replaces name declarator and remaining whitespaces
        val roadName: String = roadNameExpression.value.replace(Regex("name="), "")

        return roadName
    }

    /**
     * parses height limit out of the dot file
     */
    private fun parseHeightLimit(roadDeclaration: String): Int? {
        // looks for all entries that match: "heightLimit=Int" with arbitrary amount of whitespaces betw. equal sign
        val regexPattern: Regex = Regex("heightLimit=+[1-9][0-9]*")

        // filters all entries that coincide with that pattern
        val heightLimitExpression: MatchResult = regexPattern.find(roadDeclaration, 0) ?: return null

        // replaces heightLimit declarator and remaining whitespaces
        val withoutHLDec: String = heightLimitExpression.value.replace(Regex("heightLimit="), "")

        val heightLimit: Int = withoutHLDec.toInt()
        return heightLimit
    }

    /**
     * parses weight out of the dot file
     */
    private fun parseWeight(roadDeclaration: String): Double? {
        // looks for all entries that match: "weight=Int" with arbitrary amount of whitespaces betw. equal sign
        val regexPattern: Regex = Regex("weight=+[1-9][0-9]*")

        // filters all entries that coincide with that pattern
        val weightExpression: MatchResult = regexPattern.find(roadDeclaration, 0) ?: return null

        // replaces weight declarator and remaining whitespaces
        val withoutWeightDec: String = weightExpression.value.replace(
            Regex("weight="),
            ""
        )
        val weight: Double = withoutWeightDec.toDouble()
        return weight
    }

    /**
     * parses primary road type out of the dot file
     */
    private fun parsePrimaryType(roadDeclaration: String): PrimaryRoadType? {
        // looks for all entries that match: "primaryType=String" with arbitrary amount of whitespaces betw. equal sign
        val regexPattern: Regex = Regex("primaryType=[a-zA-Z]+")

        // filters all entries that coincide with that pattern
        val primRoadExpression: MatchResult = regexPattern.find(roadDeclaration, 0) ?: return null

        // replaces primRoadType declarator and remaining whitespaces
        val withoutPrimTypeDec: String = primRoadExpression.value.replace(Regex("primaryType="), "")

        val primRoadTypeString: String = withoutPrimTypeDec.replace(Regex(anyWhiteSpace), "")

        // infers correct primaryRoadType
        when (primRoadTypeString) {
            "countyRoad" -> return PrimaryRoadType.COUNTY_ROAD
            "mainStreet" -> return PrimaryRoadType.MAIN_STREET
            "sideStreet" -> return PrimaryRoadType.SIDE_STREET
            else -> {
                return null
            }
        }
    }

    /**
     * parses secondary Type out of the dot file
     */
    private fun parseSecondaryType(roadDeclaration: String): SecondaryRoadType? {
        // looks for all entries that match: "primaryType=String" with arbitrary amount of whitespaces betw. equal sign
        val regexPattern: Regex = Regex("secondaryType=[a-zA-Z]+")

        // filters all entries that coincide with that pattern
        val secRoadExpression: MatchResult = regexPattern.find(roadDeclaration, 0) ?: return null

        // replaces primRoadType declarator and remaining whitespaces
        val secRoadType: String = secRoadExpression.value.replace(Regex("secondaryType="), "")

        // infers correct primaryRoadType
        when (secRoadType) {
            "oneWayStreet" -> return SecondaryRoadType.ONE_WAY_ROAD
            "tunnel" -> return SecondaryRoadType.TUNNEL
            "none" -> return SecondaryRoadType.NONE
            else -> {
                return null
            }
        }
    }
}
