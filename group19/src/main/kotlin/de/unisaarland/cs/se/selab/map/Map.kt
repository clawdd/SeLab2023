package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.base.Base
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.vehicle.Vehicle

/**
 * allows to access vertices, roads;
 * returns vertices and roads in regard to passed attributes
 * such as emergency, source and target vertex, ...
 */
object Map {
    var adjacencyMatrix: AdjacencyMatrix = AdjacencyMatrix(0)
    var vertices: List<Vertex> = emptyList()
    var roads: List<Road> = emptyList()
    var name: String = ""

    /**
     * updates the road network's edges' weights
     */
    fun updateAdjacencyMatrix() {
        roads.let { adjacencyMatrix.accommodateRoadChanges(it) }
    }

    /**
     * returns the vertex with the according id
     */
    fun getVertexByID(referenceID: Int): Vertex {
        val listContainingVertex: List<Vertex> = vertices.filter { it.id == referenceID }
        return listContainingVertex[0]
    }

    /**
     * returns the vertex with the according id
     */
    fun getVertexByIDParser(referenceID: Int?): Vertex? {
        if (referenceID == null) { return null }
        val listContainingVertex: List<Vertex> = vertices.filter { it.id == referenceID }
        if (listContainingVertex.isEmpty()) {
            return null
        }
        return listContainingVertex[0]
    }

    /**
     * returns all roads that are connected to a vertex
     */
    fun getConnectedRoads(vertex: Vertex): List<Road> {
        val connectedVertices: List<Vertex?> = vertex.connectedVertices
        val connectedRoads: MutableList<Road> = mutableListOf()

        for (neighbour in connectedVertices) {
            connectedRoads.add(Map.findRoadByVertex(vertex, neighbour))
        }

        return connectedRoads
    }

    /**
     * returns the road with the according source and target vertices
     */
    fun findRoadByVertex(referenceSource: Vertex?, referenceTarget: Vertex?): Road {
        return roads.filter {
            (it.sourceVertex == referenceSource && it.targetVertex == referenceTarget) ||
                (it.sourceVertex == referenceTarget && it.targetVertex == referenceSource)
        }[0]
    }

    /**
     * only for eventEM parser, can return null
     */
    fun findRoadByVertexParser(referenceSource: Vertex?, referenceTarget: Vertex?): Road? {
        val road = roads.filter {
            (it.sourceVertex == referenceSource && it.targetVertex == referenceTarget) ||
                (it.sourceVertex == referenceTarget && it.targetVertex == referenceSource)
        }
        if (road.size != 1) {
            return null
        } else {
            return road[0]
        }
    }

    /**
     * returns the emergency on which a road occurs
     */
    fun findEmergencyRoad(emergency: Emergency): Road {
        // retrieve emergency's village and road
        val emVillageName: String = emergency.occurringVillage
        val emRoadNameName: String = emergency.roadName

        // return the road in that particular village
        val villageNameRoads = this.getAllRoadsInVillage(emVillageName)
        val emRoad: List<Road> = villageNameRoads.filter { it.name == emRoadNameName }

        return emRoad[0]
    }

    /**
     * returns a list of all the roads belonging to a village
     */
    fun getAllRoadsInVillage(villageName: String): List<Road> {
        // gets a list of all the roads which have that villageName as attribute
        val villageNameRoads: List<Road> = this.roads.filter { it.villageName == villageName }

        return villageNameRoads
    }

    /**
     * sets the map's attributes after parsing them
     */
    fun setAttributes(parsedVertices: List<Vertex>, parsedRoads: List<Road>, name: String) {
        this.vertices = parsedVertices.sortedBy { it.id } // important for dijkstra
        this.roads = parsedRoads
        this.name = name
        this.adjacencyMatrix = AdjacencyMatrix(parsedVertices.size)
    }

    /**
     * sets the base attribute of a vertex if a base is located on that vertex
     */
    fun setBasesToVertices(parsedBases: List<Base>) {
        for (i in 0 until parsedBases.size) {
            val baseVertex: Vertex = parsedBases[i].location
            baseVertex.base = parsedBases[i]
        }
    }

    /**
     *from map
     */
    fun checkRoadExistByRoadNameAndVillage(villageName: String, roadName: String): Road? {
        // val tempRoad = roads[0]
        for (road in roads) {
            if (road.villageName == villageName && road.name == roadName) {
                return road
            }
        }
        // throw IllegalArgumentException("Village or Road does not exist")
        return null
    }

    /**
     *from map
     */
    fun checkVillageExist(villageName: String): Boolean {
        var exist = false
        for (road in roads) {
            if (road.villageName == villageName) {
                exist = true
            }
        }
        return exist
    }

    /**
     * Helper function, since vehicle was full
     */
    fun checkArrivalVehicle(vehicle: Vehicle) {
        if (vehicle.route.size == 1) {
            vehicle.vehicleState = VehicleState.AT_BASE
            return
        }
    }
}
