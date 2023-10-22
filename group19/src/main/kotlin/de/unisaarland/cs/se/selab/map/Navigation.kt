package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.base.Base
import de.unisaarland.cs.se.selab.base.FireStation
import de.unisaarland.cs.se.selab.base.Hospital
import de.unisaarland.cs.se.selab.base.PoliceStation
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.vehicle.DISTANCE_TO_DURATION_FACTOR
import de.unisaarland.cs.se.selab.vehicle.Vehicle
import java.util.*

/**
 *necessary to perform distance calculations more succinctly
 */
data class LinkedNode(var distance: Double, var previousNode: Vertex)

/**
 * contains the information calculated by dijkstra
 */
data class DistanceInformation(var distanceSourceToTarget: Double, val mapVertexToNode: MutableMap<Vertex, LinkedNode>)

/**
 * this class handles all the calculations
 * concerning distance
 */
object Navigation {

    var bases: List<Base> = emptyList()
    private var policeStations: List<Base> = emptyList()
    private var fireStations: List<Base> = emptyList()
    private var hospitals: List<Base> = emptyList()

    /**
     * sets all bases to passed base list and categorizes them
     */
    fun initNavi(bases: List<Base>) {
        this.bases = bases
        this.setBasesByType()
    }

    /**
     * sorts bases by their type's and assigns them to one of the lists above
     * info: the reason for doing this in such a seemingly convoluted way is to keep those
     * lists as non-mutable, since otherwise this could give the impression that the list of
     * police stations and so on could change, which it cannot since there are no stations
     * being added / removed
     */
    private fun setBasesByType() {
        // creates new temporary lists that get filled with the according bases
        val policeStationsToBeFilled: MutableList<Base> = mutableListOf()
        val fireStationsToBeFilled: MutableList<Base> = mutableListOf()
        val hospitalsToBeFilled: MutableList<Base> = mutableListOf()

        for (base in bases) {
            when (base) {
                is PoliceStation -> policeStationsToBeFilled.add(base)
                is FireStation -> fireStationsToBeFilled.add(base)
                is Hospital -> hospitalsToBeFilled.add(base)
            }
        }

        // converts the temp. lists to non-mutable lists and sets them as attribute
        this.policeStations = policeStationsToBeFilled.toList()
        this.fireStations = fireStationsToBeFilled.toList()
        this.hospitals = hospitalsToBeFilled.toList()
    }

    /**
     * calculates both the shortest distance and the means to retrace
     * the shortest path
     */
    fun dijkstraAlgorithm(sourceVertex: Vertex, targetVertex: Vertex, objectAM: AdjacencyMatrix): DistanceInformation {
        // Create a list that keeps track of all the already visited nodes
        val unvisitedNodes: MutableList<Vertex> = Map.vertices.toMutableList()

        /*
        Create a map function, that maps nodes (or vertices) to the distance
        to reach them and their previous node
         */
        val mapVertexToNode = mutableMapOf<Vertex, LinkedNode>()

        /*
        map each node to a shortest distance from sourceVertex and a previous node
        sets the distance to source node to 0 afterwards
         */
        unvisitedNodes.forEach {
            mapVertexToNode[it] =
                LinkedNode(Double.MAX_VALUE, it)
        }
        mapVertexToNode[sourceVertex] = LinkedNode(0.0, sourceVertex)

        // repeat the procedure inside the while loop until we visited all possible nodes
        while (unvisitedNodes.size > 0) {
            /*
            set currentlyVisiting to the node with the lowest distance
            that hasn't been visited yet
             */
            val currentlyVisiting: Vertex = getCurrentVisitor(unvisitedNodes, mapVertexToNode)

            /*
            Get neighbouring nodes of currentlyVisiting vertex
            Then filter out all the nodes that already have been visited
             */
            val neighbouringNodes: List<Vertex> = currentlyVisiting.connectedVertices
            val unvisitedNeighbours: List<Vertex> = neighbouringNodes.filter { unvisitedNodes.contains(it) }

            /*
            Calculates distance to all neighbouring nodes and
            sets total cost to reach neighbouring nodes to a new
            value if the calculated distance is shorter
             */
            for (i in unvisitedNeighbours) {
                val costToNeighbour =
                    mapVertexToNode.getValue(currentlyVisiting).distance +
                        objectAM.getWeight(currentlyVisiting, i)

                if (costToNeighbour <= mapVertexToNode.getValue(i).distance) {
                    mapVertexToNode.getValue(i).distance = costToNeighbour
                    mapVertexToNode.getValue(i).previousNode = currentlyVisiting
                }
            }
        }

        val distance = mapVertexToNode.getValue(targetVertex).distance
        return DistanceInformation(distance, mapVertexToNode)
    }

    /**
     This method calculates the next node that is to be visited
     Before returning the correct node, it removes that node from the list of unvisited nodes
     **/
    private fun getCurrentVisitor(
        unvisitedNodes: MutableList<Vertex>,
        mapVertexToNode: MutableMap<Vertex, LinkedNode>
    ): Vertex {
        var shortestDistance: Double = Double.MAX_VALUE
        var currentVisitor: Vertex = unvisitedNodes[0]

        for (i in unvisitedNodes) {
            val distanceToSource: Double = mapVertexToNode.getValue(i).distance

            if (distanceToSource <= shortestDistance) {
                shortestDistance = distanceToSource
                currentVisitor = i
            }
        }

        unvisitedNodes.remove(currentVisitor)
        return currentVisitor
    }

    /**
     * returns shortest distance between two vertices
     */
    fun calculateDistance(distanceInformation: DistanceInformation): Double {
        return distanceInformation.distanceSourceToTarget
    }

    /**
     * returns shortest route between two vertices
     * PROBLEM!!: differentiate according to specs p.29 if there are several viable routes
     * of the same shortest distance
     */
    fun calculateRoute(
        sourceVertex: Vertex,
        targetVertex: Vertex,
        distanceInformation: DistanceInformation
    ): List<Vertex> {
        // retrieves the map, which contains the linked nodes, which can be recursively traced back to sourceVertex
        val mapVertexToNode: MutableMap<Vertex, LinkedNode> = distanceInformation.mapVertexToNode

        // sets targetVertex as last vertex of the road
        val route: MutableList<Vertex> = mutableListOf()
        route.add(targetVertex)

        while (route[0] != sourceVertex) {
            val currentVertex: Vertex = route[0] // retrieves closest current vertex to base
            val currentLinkedNode: LinkedNode =
                mapVertexToNode.getValue(currentVertex) // maps that vertex to it's linkedNode
            val nextVertex: Vertex = currentLinkedNode.previousNode // retrieves next vertex from linkedNode
            route.add(0, nextVertex) // sets retrieved vertex to newest closet vertex to base
        }

        return route
    }

    /**
     * return a pair of both route and distance
     */
    fun calculateRouteDistancePair(
        sourceVertex: Vertex,
        targetVertex: Vertex,
        adjacencyMatrix: AdjacencyMatrix
    ): Pair<Double, List<Vertex>> {
        // first calculates the necessary distance information via dijkstra
        val distanceInformation: DistanceInformation = this.dijkstraAlgorithm(
            sourceVertex,
            targetVertex,
            adjacencyMatrix
        )

        // calculates distance and route in the next step
        val distance = this.calculateDistance(distanceInformation)
        val route = this.calculateRoute(sourceVertex, targetVertex, distanceInformation)

        return Pair(distance, route)
    }

    /**
     * calculates route distance and routes and sets them inside the vehicle
     */
    fun distanceCalcVehicleToEmergency(vehicle: Vehicle, emergency: Emergency): Pair<Double, List<Vertex>> {
        // gets the road on which the emergency takes place and the vehicles' position
        val emergencyRoad: Road = Map.findEmergencyRoad(emergency)
        val vehicleVertex: Vertex = vehicle.getCurrentLocation()

        // sets the vehicle's own AdjacencyMatrix
        vehicle.determineAM()

        /*
        Calculates the distance and route between the vehicle source and the emergency source.
        If the vehicle is still at base, the vehicle source is equal to the base vertex
         */
        val vehicleToEMSource = this.calculateRouteDistancePair(
            vehicleVertex,
            emergencyRoad.sourceVertex,
            vehicle.vehicleAM
        )

        /*
        Calculates the distance and route between the vehicle source and the emergency target.
        If the vehicle is still at base, the vehicle source is equal to the base vertex
         */
        val vehicleToEMTarget = this.calculateRouteDistancePair(
            vehicleVertex,
            emergencyRoad.targetVertex,
            vehicle.vehicleAM
        )

        // compares both routes and returns the shorter one
        val shorterPair = this.shorterDistanceRoutePair(vehicleToEMSource, vehicleToEMTarget)
        return shorterPair
    }

    /**
     * reroutes the vehicle, by calculating the route
     * between current road's target vertex and
     * between current road's source vertex, if the road
     */
    fun calculateShortestReroute(vehicle: Vehicle): Pair<Double, List<Vertex>> {
        // gets the vehicle's road's source and target vertex
        val currentRoadSourceVertex: Vertex = vehicle.getCurrentLocation()
        val currentRoadTargetVertex: Vertex = vehicle.getIntermediaryTarget()

        // gets the vehicle's current final target vertex
        val currentRouteTarget: Vertex = vehicle.route.last()

        // sets the vehicle's AM
        vehicle.determineAM()

        /*
        First calculates distance and route from road target vertex, since the source
        may not be reachable if the current road is a one way road
         */
        val roadTargetCurrentTargetPair = this.calculateRouteDistancePair(
            currentRoadTargetVertex,
            currentRouteTarget,
            vehicle.vehicleAM
        )
        // add remaining travel time to target
        val distanceRTargetCTarget = roadTargetCurrentTargetPair.first +
            vehicle.distanceToTarget * DISTANCE_TO_DURATION_FACTOR
        var reroute = Pair(
            distanceRTargetCTarget,
            roadTargetCurrentTargetPair.second
        )

        // only perform calculations that require a "U-turn" on the road if the road is not a one way road
        if (Map.findRoadByVertex(currentRoadSourceVertex, currentRoadTargetVertex).secondaryType
            != SecondaryRoadType.ONE_WAY_ROAD
        ) {
            // calculate distance from road source only if the road is no one way road
            val roadSourceCurrentTargetPair = this.calculateRouteDistancePair(
                currentRoadSourceVertex,
                currentRouteTarget,
                vehicle.vehicleAM
            )
            // add remaining travel time from source
            val distanceRSourceCTarget = roadSourceCurrentTargetPair.first +
                vehicle.distanceToSource * DISTANCE_TO_DURATION_FACTOR
            val roadSourceCurrentTargetPairWithRemainder = Pair(
                distanceRSourceCTarget,
                roadSourceCurrentTargetPair.second
            )

            reroute = this.shorterDistanceRoutePair(reroute, roadSourceCurrentTargetPairWithRemainder)
        }

        return reroute
    }

    /**
     * Performs the route calculation for a reallocated vehicle
     */
    fun reallocationRoute(vehicle: Vehicle, emergency: Emergency): Pair<Double, List<Vertex>>? {
        // gets the vehicle's road's source vertex
        val currentRoadTarget: Vertex = vehicle.getIntermediaryTarget()
        val currentVehicleRoad: Road = Map.findRoadByVertex(currentRoadTarget, vehicle.getCurrentLocation())

        // gets the EM's road's source and target vertex
        val emRoad: Road = Map.findEmergencyRoad(emergency)
        val emergencyRoadSource: Vertex = emRoad.sourceVertex
        val emergencyRoadTarget: Vertex = emRoad.targetVertex

        // calculates the distance to the EM source from the road target
        val roadTargetEMSourcePair = this.calculateRouteDistancePair(
            currentRoadTarget,
            emergencyRoadSource,
            vehicle.vehicleAM
        )
        // add remaining travel time to target
        val totalDistanceRoadTargetEMSource = roadTargetEMSourcePair.first +
            vehicle.distanceToTarget * DISTANCE_TO_DURATION_FACTOR
        val rerouteRoadTargetEMSource = Pair(
            totalDistanceRoadTargetEMSource,
            roadTargetEMSourcePair.second
        )

        // calculates the distance to the EM target from the road target
        val roadTargetEMTargetPair = this.calculateRouteDistancePair(
            currentRoadTarget,
            emergencyRoadTarget,
            vehicle.vehicleAM
        )
        // add remaining travel time to target
        val totalDistanceRoadTargetEMTarget = roadTargetEMSourcePair.first +
            vehicle.distanceToTarget * DISTANCE_TO_DURATION_FACTOR
        val rerouteRoadTargetEMTarget = Pair(
            totalDistanceRoadTargetEMTarget,
            roadTargetEMTargetPair.second
        )

        // gets the shorter reroute from road target
        var reroute = this.shorterDistanceRoutePair(rerouteRoadTargetEMSource, rerouteRoadTargetEMTarget)

        // if current road is not a one way, do the same from source vertex
        if (currentVehicleRoad.secondaryType != SecondaryRoadType.ONE_WAY_ROAD) {
            val rerouteFromSource = this.reallocationNotOnOneWay(vehicle, emergency)
            reroute = this.shorterDistanceRoutePair(reroute, rerouteFromSource)
        }

        return reroute
    }

    private fun reallocationNotOnOneWay(vehicle: Vehicle, emergency: Emergency): Pair<Double, List<Vertex>> {
        // get the vehicle's road's source vertex
        val vehicleSource: Vertex = vehicle.getCurrentLocation()

        // get EM's source and target vertex
        val emRoad: Road = Map.findEmergencyRoad(emergency)
        val emergencyRoadSource: Vertex = emRoad.sourceVertex
        val emergencyRoadTarget: Vertex = emRoad.targetVertex

        // calculates the distance to the EM source from the road source
        val roadSourceEMSourcePair = this.calculateRouteDistancePair(
            vehicleSource,
            emergencyRoadSource,
            vehicle.vehicleAM
        )
        // add remaining travel time to target
        val totalDistanceRoadSourceEMSource = roadSourceEMSourcePair.first +
            vehicle.distanceToSource * DISTANCE_TO_DURATION_FACTOR
        val rerouteRoadSourceEMSource = Pair(
            totalDistanceRoadSourceEMSource,
            roadSourceEMSourcePair.second
        )

        // calculates the distance to the EM target from the road target
        val roadSourceEMTargetPair = this.calculateRouteDistancePair(
            vehicleSource,
            emergencyRoadTarget,
            vehicle.vehicleAM
        )
        // add remaining travel time to target
        val totalDistanceRoadTargetEMTarget = roadSourceEMTargetPair.first +
            vehicle.distanceToSource * DISTANCE_TO_DURATION_FACTOR
        val rerouteRoadSourceEMTarget = Pair(
            totalDistanceRoadTargetEMTarget,
            roadSourceEMTargetPair.second
        )

        // return shorter route
        return this.shorterDistanceRoutePair(rerouteRoadSourceEMSource, rerouteRoadSourceEMTarget)
    }

    /**
     * calculates the distance for a route in the CURRENT tick (may change next tick with events)
     * Only returns the distance from a source / target vertex of a road, remaining travel time
     * from vehicle needs to be added still
     */
    fun routeDistance(route: List<Vertex>): Double {
        // converts route to mutable list
        val mutableRoute: MutableList<Vertex> = route.toMutableList()
        var distance: Double = 0.0

        // if route has more than one element, the target node has not been reached yet
        while (mutableRoute.size > 1) {
            val firstNode: Vertex = mutableRoute[0]
            val secondNode: Vertex = mutableRoute[1]

            // calculates current distance between the first two nodes in route
            val distanceBetween: Double = Map.findRoadByVertex(firstNode, secondNode).weight

            // inf. indicates that the old route is impossible to take now
            if (distanceBetween == Double.MAX_VALUE) {
                return Double.MAX_VALUE
            }

            // removes the first node, so that in the next iteration the distance between the next
            // two vertices of route is calculated
            mutableRoute.removeFirst()

            distance += distanceBetween
        }

        return distance
    }

    /**
     * Calculates vehicle's route home
     */
    fun sendVehicleToHomeBase(vehicle: Vehicle): Pair<Double, List<Vertex>> {
        // get base's home vertex from base list
        val baseVertex: Vertex = bases.filter { it.baseID == vehicle.homeBaseId }[0].location

        // sets vehicle's own AM
        vehicle.determineAM()

        return this.calculateRouteDistancePair(vehicle.getCurrentLocation(), baseVertex, vehicle.vehicleAM)
    }

    /**
     * calculates the shorter route (per specification p. 29) out of two routes with the same distance
     */
    private fun shorterRoute(route1: List<Vertex>, route2: List<Vertex>): List<Vertex> {
        // create new vars to which the shorter and the longer list will be assigned
        // Info: shorter / longer means a list has fewer elements, not that it takes less time to travel
        val shorterRoute: List<Vertex>
        val longerRoute: List<Vertex>

        // set the route that contains fewer elements as shorter
        // if both lists are of the same size, the first list is chosen
        if (route1.size <= route2.size) {
            shorterRoute = route1
            longerRoute = route2
        } else {
            shorterRoute = route2
            longerRoute = route1
        }

        /**
         * Iterate over the length of shorterRoute
         * As soon as there is a vertex inside the longerRoute, which has a lower ID than the one
         * in shorterRoute at that position, we need to return the longerRoute
         * If there is no element inside longerRoute with a lower ID we return the route with fewer elements
         */
        for (i in shorterRoute.indices) {
            if (longerRoute[i].id < shorterRoute[i].id) {
                return longerRoute
            }
        }

        return shorterRoute
    }

    /**
     * calculates the closest bases to an emergency
     */
    fun calculateClosestBase(emergency: Emergency): Base {
        // gets emergency's road's source and target vertex
        val road: Road = Map.findEmergencyRoad(emergency)
        val sourceVertex: Vertex = road.sourceVertex
        val targetVertex: Vertex = road.targetVertex

        // gets the type of the EM (police, fire, medical)
        val emergencyType = emergency.emergencyType

        // calculates closest specific base type
        return when (emergencyType) {
            EmergencyType.CRIME -> this.calculateClosestBaseInner(sourceVertex, targetVertex, this.policeStations)
            EmergencyType.FIRE -> this.calculateClosestBaseInner(sourceVertex, targetVertex, this.fireStations)
            EmergencyType.ACCIDENT -> this.calculateClosestBaseInner(sourceVertex, targetVertex, this.fireStations)
            EmergencyType.MEDICAL -> this.calculateClosestBaseInner(sourceVertex, targetVertex, this.hospitals)

            else -> throw IllegalArgumentException("factory should not be inside base list")
        }
    }

    /**
     * does the actual logic that performs the search for the shortest base
     */
    private fun calculateClosestBaseInner(sourceVertex: Vertex, targetVertex: Vertex, bases: List<Base>): Base {
        var shortestDistance: Double = Double.MAX_VALUE
        var closestBase: Base? = null

        for (base in bases) {
            // calculates distance from base to EM source and to EM target
            val baseEMSourcePair = this.calculateRouteDistancePair(sourceVertex, base.location, Map.adjacencyMatrix)
            val baseEMTargetPair = this.calculateRouteDistancePair(targetVertex, base.location, Map.adjacencyMatrix)

            // compares distances and returns the shorter pair
            val shorterPair = this.shorterDistanceRoutePair(baseEMSourcePair, baseEMTargetPair)

            // if distance to EM is shorter from current base, set it along with its distance as closest
            if (shorterPair.first < shortestDistance) {
                shortestDistance = shorterPair.first
                closestBase = base
                continue
            }

            // if distance is the same to current closest base, set the one with the lower ID
            if (shorterPair.first == shortestDistance) {
                requireNotNull(closestBase)
                if (base.baseID < closestBase.baseID) {
                    closestBase = base
                }
            }
        }

        requireNotNull(closestBase)
        return closestBase
    }

    /**
     * first checks if the distance of pair1 is shorter than the one of pair2
     * if they are the same, routes are compared according to spec. p.29
     * returns the pair that has the "smaller" values
     */
    fun shorterDistanceRoutePair(
        pair1: Pair<Double, List<Vertex>>,
        pair2: Pair<Double, List<Vertex>>
    ): Pair<Double, List<Vertex>> {
        // checks distance1 < distance2
        if (pair1.first < pair2.first) {
            return pair1
        }

        // checks distance2 < distance1
        if (pair2.first < pair1.first) {
            return pair2
        }

        // checks for the shorter route
        return if (this.shorterRoute(pair1.second, pair2.second) == pair1.second) {
            pair1
        } else {
            pair2
        }
    }

    /**
     * finds nearest bases of a certain type and sorts them according to ID
     */
    fun findNearestBasesRequest(base: Base, baseType: BaseType): List<Base> {
        var eligibleReceivers: List<Base> = emptyList()
        val mapBaseToDistance: MutableMap<Base, Double> = mutableMapOf()

        when (baseType) {
            BaseType.POLICE_STATION -> eligibleReceivers = this.policeStations.filter { it != base }
            BaseType.FIRE_STATION -> eligibleReceivers = this.fireStations.filter { it != base }
            BaseType.HOSPITAL -> eligibleReceivers = this.hospitals.filter { it != base }
        }

        // preemptively returns if there is only one type of a base
        if (eligibleReceivers.isEmpty()) {
            return eligibleReceivers
        }

        for (receiver in eligibleReceivers) {
            val distanceInformationReceiver: DistanceInformation = this.dijkstraAlgorithm(
                base.location,
                receiver.location,
                Map.adjacencyMatrix
            )
            val receiverDistance: Double = this.calculateDistance(distanceInformationReceiver)

            mapBaseToDistance[receiver] = receiverDistance
        }

        // sorts all calculated distances and removes multiply occurring entries
        val distanceList: SortedSet<Double> = mapBaseToDistance.values.toSortedSet()

        // converts map to pair list and sorts the map according to distance
        val baseDistancePairList: List<Pair<Base, Double>> = mapBaseToDistance.toList()

        // final list that is stored by distance and then ID
        val receiversCompletelySorted: MutableList<Base> = mutableListOf()

        for (distance in distanceList) {
            // creates a sublist that contains all the bases of a certain distance
            val sublistFilteredByDistance = baseDistancePairList.filter { it.second == distance }

            // sorts those bases by ID
            val sublistFilteredDistanceSortedID = sublistFilteredByDistance.sortedBy { it.first.baseID }

            // adds the base list to the final list
            sublistFilteredDistanceSortedID.forEach { receiversCompletelySorted.add(it.first) }
        }

        return receiversCompletelySorted
    }

    /**
     * returns the base with the according Base ID
     */
    fun getBaseById(searchedBaseID: Int): Base {
        return bases.filter { it.baseID == searchedBaseID }[0]
    }
}
