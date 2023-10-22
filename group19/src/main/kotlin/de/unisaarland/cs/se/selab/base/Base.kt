package de.unisaarland.cs.se.selab.base

import de.unisaarland.cs.se.selab.EMCC
import de.unisaarland.cs.se.selab.Logger
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.EmergencyState
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.Vehicle
/**
 *base
 */

sealed class Base(
    val baseID: Int,
    val location: Vertex,
    var availableStaff: Int,
    var assignedEmergencies: MutableList<Emergency>,
    val baseType: BaseType
) {

    var emcc: EMCC? = null

    /**
     * sorts an Emergency list according to severity and ID
     */
    fun sortEmergencies() {
        val severityOneList: List<Emergency> = assignedEmergencies.filter { it.severity == 1 }
            .sortedBy { it.id }
        val severityTwoList: List<Emergency> = assignedEmergencies.filter { it.severity == 2 }
            .sortedBy { it.id }
        val severityThreeList: List<Emergency> = assignedEmergencies.filter { it.severity == 3 }
            .sortedBy { it.id }

        this.assignedEmergencies.clear()
        this.assignedEmergencies.addAll(severityThreeList)
        this.assignedEmergencies.addAll(severityTwoList)
        this.assignedEmergencies.addAll(severityOneList)
    }

    /**
     * allocates the assets for the assigned emergencies
     */
    fun assignAssets(currentTick: Int) {
        for (emergency in assignedEmergencies) {
            if (emergency.emergencyState != EmergencyState.SUCCESSFUL) {
                if (emergency.emergencyState != EmergencyState.FAIL) {
                    assignAssetsToSingleEmergency(emergency, currentTick)
                }
            }
        }
    }

    /**
     * allocates assets for the given emergency
     * @param emergency: the current emergency that the assets need to be assigned to
     */
    private fun assignAssetsToSingleEmergency(emergency: Emergency, currentTick: Int) {
        if (emergency.handleTime > emergency.lastPossibleTick - currentTick) {
            return
        }

        val collectedVehicles = collectVehicles(emergency, currentTick)
        emergency.addToAssignedVehicles(collectedVehicles.second)
        val canSendRQ = checkRequestPossible(collectedVehicles)
        /*
        we collect vehicles from our base and assign them
         */

        for (vehicle in collectedVehicles.second) {
            vehicle.prepareForEmergency(emergency)
        }

        /*
        if the compare method returns false we could not fulfill the requirements, and
        have to reallocate and prepare the vehicles correctly
         */

        if (!emergency.compare()) {
            val reallocatedVehicles: List<Vehicle> = reallocatePreviouslyAssigned(emergency, currentTick)
            removeFromEMIfReallocated(reallocatedVehicles)
            for (vehicle in reallocatedVehicles) {
                vehicle.realloc(emergency)
            }
            emergency.addToAssignedVehicles(reallocatedVehicles)
        }

        /*
        if this still did not help we create a request
         */

        if (!emergency.compare() && canSendRQ) {
            emcc?.queueRequest(Request(emergency, 0, this, mutableListOf<Base>()))
        }
    }

    /**
     * TODO
     *
     * @param collectedVehicles
     * @return
     */
    private fun checkRequestPossible(
        collectedVehicles: Pair<List<Vehicle>, List<Vehicle>>
    ): Boolean {
        if (collectedVehicles.first.isNotEmpty() && collectedVehicles.second.isEmpty()) {
            return false
        }
        return true
    }

    /**
     * TODO
     *
     * @param vehicles
     */
    private fun removeFromEMIfReallocated(vehicles: List<Vehicle>) {
        for (emergency in assignedEmergencies) {
            for (vehicle in vehicles) {
                emergency.assignedVehicles.removeIf { it.vehicleId == vehicle.vehicleId }
            }
        }
    }

    /**
     *base
     */
    abstract fun updateAssets()

    /**
     * fvghbjnkml,ö
     */
    abstract fun collectVehicles(e: Emergency, currentTick: Int): Pair<List<Vehicle>, List<Vehicle>>

    /*
    deleted collectForRequest since collectVehicles has the same functionality
     */

    /*
    private fun returnSuitableVehicle(vehicle: Vehicle): Vehicle? {
        // TODO
        throw UnsupportedOperationException("Method returnSuitableVehicle() is not implemented")
    }
     */

    /**
     * adds the emergency to the assignedEmergencies list
     * @param emergency: the emergency to be added to the list
     */
    fun assignEmergencyToBase(emergency: Emergency) {
        this.assignedEmergencies.add(emergency)
        Logger.logEmergencyAssignment(emergency.id, this.baseID)
    }

    /**
     *reallocate things
     */
    abstract fun reallocatePreviouslyAssigned(e: Emergency, currentTick: Int): List<Vehicle>

    /**
     * TODO
     *
     * @param r
     */
    abstract fun handleRequest(r: Request, currentTick: Int)
}
