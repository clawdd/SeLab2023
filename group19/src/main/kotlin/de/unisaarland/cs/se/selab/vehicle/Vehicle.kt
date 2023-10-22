package de.unisaarland.cs.se.selab.vehicle

import de.unisaarland.cs.se.selab.Logger
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.AdjacencyMatrix
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.map.Road
import de.unisaarland.cs.se.selab.map.SecondaryRoadType
import de.unisaarland.cs.se.selab.map.Vertex
import kotlin.math.ceil

const val DISTANCE_TO_DURATION_FACTOR: Double = 10.0

/**
 * attributes and functionality regarding the vehicle
 */
sealed class Vehicle(
    val vehicleId: Int,
    val homeBaseId: Int,
    val vehicleType: VehicleType?,
    val vehicleHeight: Int,
    val staffCapacity: Int,
) {

    // Initialized outside constructor as we do not need this initially
    var vehicleState: VehicleState = VehicleState.AT_BASE
    var unavailability: Boolean = false

    // todo for janos : we are supposed to log only when the vehicle is actually rendered unavailable
    // not when set unavailable for is called. I think you do already do it this way!
    var recovering: Boolean = false
    var prepared: Boolean = false // indicates if the vehicle can be send to em next turn
    val route: MutableList<Vertex> = mutableListOf()
    var distanceToSource: Int = 0
    var distanceToTarget: Int = 0
    var vehicleAM: AdjacencyMatrix = AdjacencyMatrix(0)

    /**
     * determines vehicle's behaviour according to its current status
     **/
    fun updateVehicleStatus() {
        when (vehicleState) {
            VehicleState.AT_BASE -> this.updateAtBaseStatus()
            VehicleState.EN_ROUTE -> this.updateEnRouteStatus()
            VehicleState.AT_EMERGENCY -> this.vehicleState = VehicleState.AT_EMERGENCY
            VehicleState.RETURNING -> this.updateReturningStatus()
        }
    }

    /**
     * does nothing with the vehicle if it is AT_BASE since
     * only other methods can change its status
     */
    private fun updateAtBaseStatus() {
        // if vehicle has to recover, all other actions are postponed
        if (this.recovering) {
            this.recover()
        }

        if (this.unavailability) {
            return
        }
    }

    /**
     * first the vehicle's driving status is updated (moving it from road to road)
     * Afterwards, check if the vehicle has arrived at its target. If so set the
     * vehicle's status to AT_EMERGENCY
     */
    private fun updateEnRouteStatus() {
        // ignores any route updates if it was prepared this tick
        if (prepared) {
            prepared = false
            return
        }

        if (this.assetsUsed() && this.onBase()) {
            this.recover()
            return
        }

        // if route size is one the vehicle has already arrived
        if (this.route.size == 1) {
            this.vehicleState = VehicleState.AT_EMERGENCY
            Logger.logAssetArrival(this.vehicleId, this.getCurrentLocation().id)
            return
        }

        // drives a tick if it is possible to move forward
        if (this.route.size >= 1 && distanceToTarget >= 1) {
            this.distanceToSource += 1
            this.distanceToTarget -= 1
        }

        // check if arrived at intermediary vertex
        if (this.distanceToTarget == 0 && route.size > 2) {
            this.calculateTravelTimeOnRoad(this.getCurrentLocation(), this.getIntermediaryTarget())
            this.route.removeFirst()
        }

        // check if at final destination
        if (this.distanceToTarget == 0 && route.size == 2) {
            // these values are set to 0, since the vehicle remains still for the EM duration
            this.distanceToSource = 0
            this.distanceToTarget = 0

            this.route.removeFirst()

            this.vehicleState = VehicleState.AT_EMERGENCY
            Logger.logAssetArrival(this.vehicleId, this.getCurrentLocation().id)
            return
        }
    }

    /**
     * Similar to updateArrivalStatus, as it checks the same return condition as well
     * However, when returning to base a different vehicle state needs to be set and
     * the vehicle's staff needs to be returned to base
     */
    private fun updateReturningStatus() {
        Map.checkArrivalVehicle(this)

        if (this.route.size >= 2 && distanceToTarget >= 1) {
            this.distanceToSource += 1
            this.distanceToTarget -= 1
        }

        // check if at final destination (base)
        if (this.distanceToTarget == 0 && route.size == 2) {
            // these values are set to 0, since the vehicle remains still for the EM duration
            this.distanceToSource = 0
            this.distanceToTarget = 0

            this.route.removeFirst()

            this.vehicleState = VehicleState.AT_BASE
            this.returnAssets()

            // If the vehicles respective resource is not at full capacity, start recovering next turn
            if (this.assetsUsed()) {
                this.recovering = true
                return
            }
        }

        if (this.distanceToTarget == 0 && route.size > 2) {
            this.route.removeFirst()
            this.calculateTravelTimeOnRoad(this.getCurrentLocation(), this.getIntermediaryTarget())
        }
    }

    /**
     * refills water, unloads criminals / patients for according vehicles
     * if recovery action is done, sets vehicle recovering attribute to false
     */
    abstract fun recover()

    /**
     * sets the duration to source to 0
     * sets the duration to target to the calculated value
     */
    private fun calculateTravelTimeOnRoad(vehicleCurrenLocation: Vertex, roadTargetVertex: Vertex) {
        // sets distance to source to null since vehicle has now reached a new vertex
        this.distanceToSource = 0

        // changes distance to target to road weight / 10, rounded up
        this.distanceToTarget = ceil(
            Map.findRoadByVertex(
                vehicleCurrenLocation,
                roadTargetVertex
            ).weight / DISTANCE_TO_DURATION_FACTOR
        ).toInt()
    }

    /**
     * documentation
     */
    fun prepareForEmergency(emergency: Emergency) {
        // vehicle does let its route get calculated by Navigation
        val routeDistancePair: Pair<Double, List<Vertex>> = Navigation.distanceCalcVehicleToEmergency(this, emergency)

        // calculates the amount of ticks away from emergency, increases by one if zero
        var ticksAway: Int = ceil(routeDistancePair.first / DISTANCE_TO_DURATION_FACTOR).toInt()
        if (ticksAway == 0) {
            ticksAway = 1
        }

        this.route.clear()
        // check if the route is equal to 0, or if the vehicle is already at target
        for (vertex in routeDistancePair.second) {
            route.add(vertex)
        }
        // sets calculated route
        if (route.size > 1) {
            this.calculateTravelTimeOnRoad(this.getCurrentLocation(), this.getIntermediaryTarget())
        }

        // sets vehicle's status and logs assignment
        this.prepared = true
        this.vehicleState = VehicleState.EN_ROUTE
        Logger.logAssetAllocation(vehicleId, emergency.id, ticksAway)
    }

    /**
     * calculates return route and duration
     */
    fun sendBackToBase() {
        // calculates return route and duration
        val returnDistanceRoutePair = Navigation.sendVehicleToHomeBase(this)
        val returnRoute = returnDistanceRoutePair.second.toMutableList()

        // if route size is zero, we are already at the target
        if (returnRoute.size == 1) {
            this.route.clear()
            this.route.add(returnDistanceRoutePair.second.first())

            this.distanceToTarget = 0
            this.distanceToSource = 0
            this.vehicleState = VehicleState.EN_ROUTE
            return
        }
        /**
         * actual route calculation has to be done if route is not only the current
         * location
         */
        if (returnRoute.size > 1) {
            // sets calculated values
            this.route.clear()
            for (vertex in returnRoute) {
                this.route.add(vertex)
            }
            // calculates time from em vertex to first vertex in route
            this.calculateTravelTimeOnRoad(this.getCurrentLocation(), getIntermediaryTarget())
            this.vehicleState = VehicleState.RETURNING
        }
    }

    /**
     * documentation
     */
    fun reroute() {
        // log even if only travel time changes
        if (this.vehicleState == VehicleState.AT_BASE || this.vehicleState == VehicleState.AT_EMERGENCY) {
            return
        }

        if (this.route.size < 2) {
            return
        }

        // calc the shortest reroute from either the target or the source vertex
        val shortestRerouteDistance: Pair<Double, List<Vertex>> = Navigation.calculateShortestReroute(this)
        val shortestReroute = Pair(
            ceil(
                shortestRerouteDistance.first / DISTANCE_TO_DURATION_FACTOR
            ),
            shortestRerouteDistance.second
        )

        // check if rerouting is necessary, by comparing routes
        var currentRoutePair: Pair<Double, List<Vertex>> = Pair(0.0, emptyList())

        // if vec is on a vertex, we can just calculate the distance from its
        // current location to the target
        val remainingDuration = ceil(Navigation.routeDistance(this.route) / DISTANCE_TO_DURATION_FACTOR)

        if (isOnVertex() && route.size > 1) {
            currentRoutePair = Pair(remainingDuration, this.route)
        } else if (!isOnVertex() && route.size > 1) {
            // if not on vertex, calculate the distance from next intermediary target to final target
            // then add remaining travel time until current intermediary target
            currentRoutePair = Pair(remainingDuration + distanceToTarget, this.route)
        }

        if (currentRoutePair == shortestReroute) {
            return
        }

        val shortestOverall = Navigation.shorterDistanceRoutePair(currentRoutePair, shortestReroute)

        this.setNewRoute(shortestOverall)
        // increase rerouting
        Logger.logCntRerouteOneTickIsFinished(false)
    }

    /**
     * Sets the new route if vehicle actually needs to be rerouted
     */
    fun setNewRoute(newRoute: Pair<Double, List<Vertex>>) {
        val newRouteCopy = newRoute.second.toList()

        // if vehicle currently is on a vertex, we can just set the new route since it can turn in any direction
        if (this.isOnVertex()) {
            this.route.clear()
            this.route.addAll(newRouteCopy)
            return
        }

        // if not: first check if U-turn is necessary:
        if (newRoute.second[0] == this.getCurrentLocation()) {
            // save old roadTarget, in case of a U-turn
            val newSource = this.getIntermediaryTarget()

            // set new route
            this.route.clear()
            this.route.add(newSource)
            this.route.addAll(newRoute.second)

            // swap distances into the road
            val temp = this.distanceToSource
            this.distanceToSource = distanceToTarget
            this.distanceToTarget = temp
        } else {
            // if no U-turn necessary: just set the new route
            val currentSource = this.getCurrentLocation()
            this.route.clear()
            this.route.add(currentSource)
            this.route.addAll(newRoute.second)
        }

        this.vehicleState = VehicleState.EN_ROUTE
        return
    }

    /**
     * Reallocates the vehicle to a new emergency
     */
    fun realloc(emergency: Emergency) {
        // calculates shortest reroute
        val reallocRoute: Pair<Double, List<Vertex>> =
            Navigation.reallocationRoute(this, emergency) ?: return

        // this part covers what happens when the vehicle gets realloc'd to the road it is currently on
        val currentRoad = Map.findRoadByVertex(this.getCurrentLocation(), this.getIntermediaryTarget())
        if (reallocRoute.second.size == 1) {
            if (reallocRoute.first == 0.0 || currentRoad == Map.findEmergencyRoad(emergency)) {
                this.route.clear()
                this.route.add(reallocRoute.second[0])
                this.distanceToTarget = 0
                this.distanceToSource = 0
                this.prepared = true
                this.vehicleState = VehicleState.EN_ROUTE
                Logger.logAssetReallocation(this.vehicleId, emergency.id)
                return
            }
        }

        this.setNewRoute(reallocRoute)
        this.vehicleState = VehicleState.EN_ROUTE
        Logger.logAssetReallocation(this.vehicleId, emergency.id)
    }

    /**
     * sets values inside the vehicle's adjacency Matrix according to
     * height restriction. This method is then further used inside the
     * navigation class for the dijkstra algorithm
     */
    fun determineAM() { // called inside Navigation
        // calculates the vehicle's own adjacencyMatrix
        this.vehicleAM = AdjacencyMatrix(Map.vertices.size)
        val roads: List<Road> = Map.roads
        vehicleAM.accommodateRoadChanges(roads)

        // takes the vehicle's weight into account when calculating weights
        for (i in roads.indices) {
            if (this.vehicleHeight > roads[i].height && roads[i].secondaryType == SecondaryRoadType.ONE_WAY_ROAD) {
                vehicleAM.setWeight(roads[i].sourceVertex, roads[i].targetVertex, Double.MAX_VALUE)
            } else if (this.vehicleHeight > roads[i].height &&
                roads[i].secondaryType != SecondaryRoadType.ONE_WAY_ROAD
            ) {
                vehicleAM.setWeight(roads[i].sourceVertex, roads[i].targetVertex, Double.MAX_VALUE)
                vehicleAM.setWeight(roads[i].targetVertex, roads[i].sourceVertex, Double.MAX_VALUE)
            }
        }
    }

    /**
     * returns the current location of the vehicle
     */
    fun getCurrentLocation(): Vertex {
        return route[0]
    }

    /**
     * return the current target of the vehicle
     */
    fun getIntermediaryTarget(): Vertex {
        return route[1]
    }

    /**
     * Checks if a vehicle is 0 ticks into a road
     */
    fun isOnVertex(): Boolean {
        if (this.distanceToSource == 0) {
            return true
        } else {
            return false
        }
    }

    /**
     * Checks if the vehicle is currently on its base's vertex
     */
    fun onBase(): Boolean {
        if (this.isOnVertex() && this.getCurrentLocation().hasBase) {
            if (getCurrentLocation().base?.baseID == this.homeBaseId) {
                return true
            }
        }
        return false
    }

    /**
     * returns the non-specialist staff members to their base by increasing the base's
     * available staff by the vehicles capacity, since any vehicle has to be fully staffed
     * for an emergency
     * Adds vehicles back to their respective bases as well
     */
    abstract fun returnAssets()

    /**
     * If any of the vehicle's resources has already been used, wait at the base to fill them up
     */
    abstract fun assetsUsed(): Boolean
}
