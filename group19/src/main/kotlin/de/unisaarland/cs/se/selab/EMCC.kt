package de.unisaarland.cs.se.selab

import de.unisaarland.cs.se.selab.base.Base
import de.unisaarland.cs.se.selab.base.Request
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.EmergencyState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.event.ClosedRoad
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.vehicle.Vehicle

/**
 * EMCC class
 */
class EMCC(
    val allOccurringEmergencies: List<Emergency>?,
    var onGoingEmergencies: MutableList<Emergency>,
    val services: List<Base>,
    var forwardedRequests: Int?,
) {

    private val receivedRequests: MutableList<Request> = mutableListOf<Request>()
    var requestIDtoAssign: Int = 1

    /**
     * Receives all emergencies in the current tick
     * @param currentTick : the current tick
     */
    fun receiveCurrentEmergencies(currentTick: Int) {
        if (allOccurringEmergencies != null) {
            // just for detekt
            // val currentTickEmergencies:
            // List<Emergency> = allOccurringEmergencies.filter { it.occurringTick == currentTick }
            // handle currentTickEmergencies
            val currentTickEmergencies: List<Emergency> =
                allOccurringEmergencies.filter { it.occurringTick == currentTick }
            for (emergency in currentTickEmergencies) {
                /*
                 * looks kinda sketchy to me right now since I use a validate function to get the road,
                 * but if the config is correct no exception should be thrown at this point
                 */
                val road = Map.findEmergencyRoad(emergency)
                when (road.event) {
                    is ClosedRoad -> (road.event as ClosedRoad).delayRoadClosing(currentTick)
                    else -> break
                }
                /*
                 * when the emergency starts on a road set boolean to true,
                 * else when it ends -> set it to false again
                 */
                road.emergencyHappeningOnRoad = true
            }

            onGoingEmergencies.addAll(currentTickEmergencies)
            distributeEmergencies(currentTickEmergencies)
        } else {
            // handle allOccurringEmergencies is null
        }
    }

    /**
     * distributes all new emergencies
     */
    private fun distributeEmergencies(currentTickEmergencies: List<Emergency>) {
        currentTickEmergencies.sortedBy { it.id }
        for (e in currentTickEmergencies) {
            Navigation.calculateClosestBase(e).assignEmergencyToBase(e)
        }
    }

    /**
     * update emergency status call all bases
     */
    fun updateEMCCEmergenciesStatus(currentTick: Int) {
        val stateToID = mutableMapOf<Int, EmergencyState>()

        if (allOccurringEmergencies != null) {
            for (e in allOccurringEmergencies) {
                e.updateEmergencyStatus(currentTick, stateToID)
            }
        }

        val handling = mutableListOf<Int>()
        val resolved = mutableListOf<Int>()
        val failure = mutableListOf<Int>()

        for (entry in stateToID) {
            when (entry.value) {
                EmergencyState.CURRENTLY_BEING_HANDLED -> handling.add(entry.key)
                EmergencyState.SUCCESSFUL -> resolved.add(entry.key)
                EmergencyState.FAIL -> failure.add(entry.key)
                else -> break
            }
        }

        handling.sort()
        resolved.sort()
        failure.sort()

        handling.map { Logger.logEmergencyHandlingStart(it) }
        resolved.map { Logger.logEmergencyResolved(it) }
        failure.map { Logger.logEmergencyFailed(it) }

        onGoingEmergencies = onGoingEmergencies.filter {
            it.emergencyState != EmergencyState.SUCCESSFUL &&
                it.emergencyState != EmergencyState.FAIL
        }.toMutableList()
    }

    /**
     * update assets call to all bases
     */
    fun updateAssetsAllBases() {
        services.sortedBy { it.baseID }
        for (b in services) {
            b.updateAssets()
        }
    }

    /**
     * assign call to every base
     */
    fun assignAssetsAllBases(currentTick: Int) {
        services.sortedBy { it.baseID }
        for (b in services) {
            b.assignAssets(currentTick)
        }
    }

    /**
     * sort call to every base
     */
    fun sortEmergenciesAllBases() {
        for (b in services) {
            b.sortEmergencies()
        }
    }

    /**
     * queue request in to list of requests ,here the cannot direct ++
     * just for detekt, check the forwardedRequest not null
     */
    fun queueRequest(r: Request) {
        val currentForwardedRequests = forwardedRequests // 保存当前的值
        if (currentForwardedRequests != null) {
            forwardedRequests = currentForwardedRequests + 1
        } else {
            forwardedRequests = 0
        }

        val listOfBaseType: List<BaseType> = determineBaseType(r.emergency.inferRequiredVehicles())
        val listOfNearestBases: MutableList<List<Base>> = mutableListOf<List<Base>>()

        for (baseType in listOfBaseType) {
            listOfNearestBases.add(Navigation.findNearestBasesRequest(r.initialRequester, baseType))
        }

        listOfNearestBases.removeIf { it.isEmpty() }
        if (listOfNearestBases.isEmpty()) {
            return
        }
        listOfNearestBases.sortBy { it[0].baseID }

        for (bases in listOfNearestBases) {
            Logger.logAssetRequest(requestIDtoAssign, bases[0].baseID, r.emergency.id)
            receivedRequests.add(
                Request(
                    r.emergency,
                    requestIDtoAssign,
                    r.initialRequester,
                    bases.toMutableList()
                )
            )
            requestIDtoAssign++
        }
    }

    /**
     * handle request
     */
    fun handleRequests(currentTick: Int) {
        for (request in receivedRequests) {
            val base = request.listOfNearestBases?.firstOrNull()
            base?.handleRequest(request, currentTick)
        }
        receivedRequests.clear()
    }

    /**
     * TODO
     *
     * @param r
     */
    fun addRequestToQueue(
        r: Request
    ) {
        requestIDtoAssign++
        r.requestID = requestIDtoAssign
        receivedRequests.add(r)
    }

    private fun determineBaseType(list: List<Vehicle>): List<BaseType> {
        val groupOfVehicles = list.groupBy { it.vehicleType }
        val baseTypes = mutableListOf<BaseType>()

        if (VehicleType.FIRE_TRUCK_WATER in groupOfVehicles ||
            VehicleType.FIRE_TRUCK_LADDER in groupOfVehicles
        ) {
            baseTypes.add(BaseType.FIRE_STATION)
        } else if (VehicleType.FIREFIGHTER_TRANSPORTER in groupOfVehicles ||
            VehicleType.FIRE_TRUCK_TECHNICAL in groupOfVehicles
        ) {
            baseTypes.add(BaseType.FIRE_STATION)
        }

        if (VehicleType.K9_POLICE_CAR in groupOfVehicles ||
            VehicleType.POLICE_CAR in groupOfVehicles ||
            VehicleType.POLICE_MOTORCYCLE in groupOfVehicles
        ) {
            baseTypes.add(BaseType.POLICE_STATION)
        }

        if (VehicleType.AMBULANCE in groupOfVehicles ||
            VehicleType.EMERGENCY_DOCTOR_CAR in groupOfVehicles
        ) {
            baseTypes.add(BaseType.HOSPITAL)
        }
        return baseTypes
    }
}
