package de.unisaarland.cs.se.selab.map

/**
 * data structure that represents the weighted graph
 * and is used to store and access the road weights
 */
class AdjacencyMatrix() {

    private var arrayRepresentation: Array<DoubleArray> = emptyArray() // represents the AM as two nested arrays

    constructor(vertexAmount: Int) : this() {
        /**
         * Creates an array of doubleArrays
         * Each Double array has as many entries as there are vertices
         * Each Double array gets initialized with zeros and gets filled with infinity afterwards
         */
        val outerArray: Array<DoubleArray> = Array(vertexAmount) { DoubleArray(vertexAmount) }
        outerArray.forEach { it.fill(Double.MAX_VALUE) }

        // set (n,n) entries to 0
        for (i in 0 until vertexAmount) {
            for (j in 0 until vertexAmount) {
                if (i == j) {
                    outerArray[i][j] = 0.0
                }
            }
        }

        this.arrayRepresentation = outerArray
    }

    /**
     * changes entries inside the matrix according
     * to the changes of the roads' weights
     */
    fun accommodateRoadChanges(roads: List<Road>) {
        for (i in roads.indices) {
            val sourceVertex: Vertex = roads[i].sourceVertex
            val targetVertex: Vertex = roads[i].targetVertex
            val roadWeight: Double = roads[i].weight

            /*
            If road is a oneWayRoad, only sets the weight between target and source,
            else sets the weight in both directions
             */
            if (roads[i].secondaryType == SecondaryRoadType.ONE_WAY_ROAD) {
                this.setWeight(sourceVertex, targetVertex, roadWeight)
                continue
            }

            this.setWeight(sourceVertex, targetVertex, roadWeight)
            this.setWeight(targetVertex, sourceVertex, roadWeight)
        }
    }

    /**
     * This method returns a list of all vertices connected to the passed vertex
     * Also it might be faster to assign this list to each vertex during parsing
     */
    fun calculateNeighbouringNodes(vertex: Vertex): List<Vertex> {
        val positionArray: MutableList<Int> = mutableListOf()
        val neighbouringVertices: MutableList<Vertex> = mutableListOf()
        val vertexIndex: Int = Map.vertices.indexOf(vertex)

        // returns the array in which the weight between the passed vertex and all other vertices is stored
        val distanceToOtherVertices: DoubleArray = arrayRepresentation[vertexIndex]

        // save the positions on which the vertex is connected to other vertices
        for (i in distanceToOtherVertices.indices) {
            if (distanceToOtherVertices[i] > 0 && distanceToOtherVertices[i] < Int.MAX_VALUE) {
                positionArray.add(i)
            }
        }

        // return the entries on those positions in a list
        for (i in 0 until positionArray.size) {
            val vertexPosition: Int = positionArray[i]
            neighbouringVertices.add(Map.vertices[vertexPosition])
        }

        return neighbouringVertices.toList()
    }

    /**
     * returns the weight between two connected vertices
     */
    fun getWeight(sourceVertex: Vertex?, targetVertex: Vertex?): Double {
        val sourceVertexPos = Map.vertices.indexOf(sourceVertex)
        val targetVertexPos = Map.vertices.indexOf(targetVertex)

        val weight: Double = arrayRepresentation[sourceVertexPos][targetVertexPos]
        return weight
    }

    /**
     * sets the weight between two vertices
     */
    fun setWeight(sourceVertex: Vertex, targetVertex: Vertex, roadWeight: Double) {
        val sourceVertexPos = Map.vertices.indexOf(sourceVertex)
        val targetVertexPos = Map.vertices.indexOf(targetVertex)

        arrayRepresentation[sourceVertexPos][targetVertexPos] = roadWeight
    }

    /**
     * returns the array storing the connections to a vertex
     */
    fun getOuterArray(row: Int): DoubleArray {
        return arrayRepresentation[row]
    }

    /**
     * gets the weight between two vertices
     */
    fun getInnerEntry(row: Int, column: Int): Double {
        return this.getOuterArray(row)[column]
    }
}
