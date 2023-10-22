package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.base.Base

/**
 * TODO
 *
 * @property id
 */
class Vertex(val id: Int) {
    var base: Base? = null // superfluous?
    var connectedVertices: List<Vertex> = emptyList()
    var hasBase = false

    /**
     *ewqeew
     */
    fun calculateConnectedVertices() {
        connectedVertices = Map.adjacencyMatrix.calculateNeighbouringNodes(this)
    }

    override fun toString(): String {
        return id.toString()
    }
}
