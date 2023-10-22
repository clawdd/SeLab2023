package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.PrimaryRoadType
import de.unisaarland.cs.se.selab.map.Road
import de.unisaarland.cs.se.selab.map.SecondaryRoadType
import de.unisaarland.cs.se.selab.map.Vertex

/**
 * Provides functionality to validated objects parsed from dot file
 */
class DotFileValidator(val filePath: String, val dotParser: DotParser) {

    /**
     * validates following properties (specification p.15):
     * 1. all vertexIDs are unique and >= 0
     */
    fun validateVertex(vertex: Vertex): Boolean {
        // checks if vertexID >= 0 and not already present in map
        if (vertex.id < 0) {
            return false
        }
        if (dotParser.parsedVertices.any { it.id == vertex.id }) {
            return false
        }

        return true
    }

    /**
     validates following properties (specification p.15):
     5. at most one edge between vertices
     6. all edges connect two existing vertices
     10. weight of a road must be > 0
     11. height of a road must be >= 1
     12. height of a tunnel must be <= 3
     13. for all village names: villageName != countyName
     by me: if countyRoad, then villageName must be equal to countyName
     **/
    fun validateRoad(road: Road): Boolean {
        var result: Boolean = true

        // road weight must be > 0
        if (road.weight <= 0) {
            result = false
        }

        // road height must be >= 1
        if (road.height < 1) {
            result = false
        }

        // if tunnel: height must be <= 3
        if (road.secondaryType == SecondaryRoadType.TUNNEL && road.height > 3) {
            result = false
        }

        // check if source and target vertex exist
        if (!dotParser.parsedVertices.contains(road.sourceVertex) ||
            !dotParser.parsedVertices.contains(road.targetVertex)
        ) {
            result = false
        }

        // this part checks if another road with same source and target vertex exists
        if (dotParser.parsedRoads.any { it.sourceVertex == road.sourceVertex && it.targetVertex == road.targetVertex }
        ) {
            result = false
        }

        return result
    }

    /**
     validates following properties (specification p.15):
     2. each vertex is connected to another vertex
     3. road name is unique in a village
     4. no edge from vertex to itself
     7. all edges connecting the same vertex belong to same village or are countyRoads
     8. each village: mainStreet >= 1
     9. whole map: sideStreet >= 1
     **/
    fun validateMap(): Boolean {
        /*
        A village is list of all the roads that have the same villageName attribute.
        This means that each villageName will get mapped to a List<Road>
         */
        val mapNameToVillage = dotParser.parsedRoads.groupBy { it.villageName }

        // for 2.: check if at least one entry in the inner array in the adjacencMatrix is > 0 and < infinity
        for (i in 0 until dotParser.parsedVertices.size) {
            if (Map.adjacencyMatrix.getOuterArray(i).none { it > 0 && it < Double.MAX_VALUE }) {
                return false
            }
        }

        // for 4.: check if every (n, n) entry in the adjacencyMatrix is 0.0
        for (i in 0 until dotParser.parsedVertices.size) {
            if (Map.adjacencyMatrix.getInnerEntry(i, i) != 0.0) {
                return false
            }
        }

        // for 3.: go through each list in mapNameToVillage and check if road names occur more than once
        for (villageName in mapNameToVillage.keys) {
            val roadsByName = mapNameToVillage[villageName]?.groupBy { it.name }

            if (roadsByName?.any { it.value.size != 1 } == true) {
                return false
            }
        }

        return this.validateMapPart2(mapNameToVillage)
    }

    /**
     * Second part of validate that does exactly the same as the first part
     * but detekt went off
     */
    private fun validateMapPart2(mapNameToVillage: kotlin.collections.Map<String, List<Road>>): Boolean {
        /*
        for 7.: get all positions in the AM, on which weight >0 && < Inf.
        Find the roads that connects the vertices of those positions and make all the roads into a list
        Check if the villageName attribute is the same for all the roads or if it is equal to the countyName
         */
        for (vertex in dotParser.parsedVertices) {
            val connectedRoads: List<Road> = Map.getConnectedRoads(vertex)
            val referenceName: String = connectedRoads.filter { it.villageName != dotParser.countyName }[0].villageName

            if (connectedRoads.any { it.villageName != dotParser.countyName && it.villageName != referenceName }) {
                return false
            }
        }

        // for 8.: check for each roadList in mapNameToVillage if it contains a mainStreet
        for (villageName in mapNameToVillage.keys) {
            if (villageName == dotParser.countyName) {
                continue
            }

            if (mapNameToVillage[villageName]?.none { it.primaryType == PrimaryRoadType.MAIN_STREET } == true) {
                return false
            }
        }

        // for 9.: go through the map's road list and check if any of it is a sideStreet
        if (dotParser.parsedRoads.none { it.primaryType == PrimaryRoadType.SIDE_STREET }) {
            return false
        }

        return true
    }
}
