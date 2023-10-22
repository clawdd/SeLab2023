package de.unisaarland.cs.se.selab.base

import de.unisaarland.cs.se.selab.Logger
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.DISTANCE_TO_DURATION_FACTOR
import de.unisaarland.cs.se.selab.vehicle.PoliceVehicle
import de.unisaarland.cs.se.selab.vehicle.Vehicle
import de.unisaarland.cs.se.selab.vehicle.generateAllSubsets
import de.unisaarland.cs.se.selab.vehicle.searchForLowestIDSubset
import kotlin.math.ceil

const val REQUIRED_MOTORCYCLE = 3
const val REQUIRED_K9S = 4

/**
 * class docuhbqew
 */
class PoliceStation(
    baseID: Int,
    location: Vertex,
    availableStaff: Int,
    assignedEmergencies: MutableList<Emergency>,
    var assignedVehicles: MutableList<PoliceVehicle>,
    var availableK9s: Int,
    baseType: BaseType
) : Base(
    baseID,
    location,
    availableStaff,
    assignedEmergencies,
    baseType
) {

    override fun updateAssets() {
        assignedVehicles.sortBy { it.vehicleId }
        for (vehicle in assignedVehicles) {
            vehicle.updateVehicleStatus()
        }
    }

    override fun collectVehicles(e: Emergency, currentTick: Int): Pair<List<Vehicle>, List<Vehicle>> {
        val group = e.inferRequiredVehicles()
            .groupingBy { it.vehicleType }
            .eachCount()

        val remainingTime = e.lastPossibleTick - e.handleTime - currentTick

        val requiredPoliceCars = group[VehicleType.POLICE_CAR] ?: 0
        val requiredMotorcycles = group[VehicleType.POLICE_MOTORCYCLE] ?: 0
        val requiredK9Cars = group[VehicleType.K9_POLICE_CAR] ?: 0

        val assignedFiltered = assignedVehicles
            .filter { it.vehicleState == VehicleState.AT_BASE && !it.unavailability && !it.recovering }
        val staffableVehicles = assignedFiltered.filter {
            it.staffCapacity <= availableStaff
        }.toMutableList()

        staffableVehicles.removeAll {
            it.vehicleType == VehicleType.POLICE_CAR &&
                requiredPoliceCars == 0
        }
        staffableVehicles.removeAll {
            it.vehicleType == VehicleType.POLICE_MOTORCYCLE &&
                requiredMotorcycles == 0
        }
        staffableVehicles.removeAll {
            it.vehicleType == VehicleType.K9_POLICE_CAR &&
                requiredK9Cars == 0
        }

        val filterByTime = staffableVehicles.filter {
            ceil(
                Navigation.distanceCalcVehicleToEmergency(it, e).first /
                    DISTANCE_TO_DURATION_FACTOR
            ).toInt() <= remainingTime
        }

        val allSubsets = filterByTime.generateAllSubsets()
        val subset = allSubsets.getCorrectSubset(
            e,
            requiredPoliceCars,
            requiredMotorcycles,
            requiredK9Cars
        )

        val usedStaff = subset.sumOf { it.staffCapacity }
        availableStaff - usedStaff
        availableK9s - subset.count { it.vehicleType == VehicleType.K9_POLICE_CAR }
        return Pair(staffableVehicles, subset)
    }

    /*
    override fun collectVehicles(e: Emergency): List<PoliceVehicle> {
        val allSubsets = availableVehicles.generateAllSubsets()
        return allSubsets.getCorrectSubset(
            e,
            e.inferRequiredVehicles().size,
            e.inferRequiredVehicles().size
        )
    }
     */

    override fun reallocatePreviouslyAssigned(e: Emergency, currentTick: Int): List<PoliceVehicle> {
        val group = e.inferRequiredVehicles()
            .groupingBy { it.vehicleType }
            .eachCount()

        val remainingTime = e.lastPossibleTick - e.handleTime - currentTick

        val requiredPoliceCars = group[VehicleType.POLICE_CAR] ?: 0
        val requiredMotorcycles = group[VehicleType.POLICE_MOTORCYCLE] ?: 0
        val requiredK9Cars = group[VehicleType.K9_POLICE_CAR] ?: 0

        val severity = e.severity

        val readyForRealloc = assignedEmergencies
            .filter { it.severity < severity }
            .flatMap { it.assignedVehicles }
            .filterIsInstance<PoliceVehicle>()
            .filter { it.vehicleState == VehicleState.EN_ROUTE }.toMutableList()

        readyForRealloc.addAll(
            assignedVehicles.filter {
                it.vehicleState == VehicleState.RETURNING && it.criminalCapacity > it.criminalsOnBoard
            }
        )

        val filteredByTime = readyForRealloc.filter {
            ceil(
                Navigation.distanceCalcVehicleToEmergency(it, e).first /
                    DISTANCE_TO_DURATION_FACTOR
            ).toInt() <= remainingTime
        }

        val allSubsets = filteredByTime.generateAllSubsets()
        return allSubsets.getCorrectSubset(
            e,
            requiredPoliceCars,
            requiredMotorcycles,
            requiredK9Cars
        )
    }

    private fun List<List<PoliceVehicle>>.getCorrectSubset(
        emergency: Emergency,
        requiredPoliceCars: Int,
        requiredMotorcycles: Int,
        requiredK9Cars: Int
    ): List<PoliceVehicle> {
        val group = emergency.requiredVehicles
            .groupingBy { it.vehicleType }
            .eachCount()

        val originalRequiredPoliceCars = group[VehicleType.POLICE_CAR] ?: 0
        val requiredCriminals = emergency.requiredCriminals - totalCriminals(emergency.assignedVehicles)
        val requiredVehicles = emergency.inferRequiredVehicles()

        /**
         * not needed since we do pass required k9 vehicles
         */
        var requiredK9s = 0

        for (vehicle in requiredVehicles) {
            if (vehicle.vehicleType == VehicleType.K9_POLICE_CAR && vehicle is PoliceVehicle) {
                requiredK9s += 1
            }
        }

        val filteredSubsets = filterByRequirements(
            this.toMutableList(),
            listOf(
                requiredPoliceCars,
                requiredMotorcycles,
                requiredK9Cars,
                requiredK9s
            )
        )

        val validSubset = findValidSubset(filteredSubsets, requiredCriminals, requiredPoliceCars)

        if (validSubset != null) {
            return validSubset
        }

        return handleOtherCases(
            emergency,
            filteredSubsets,
            listOf(
                requiredCriminals,
                originalRequiredPoliceCars,
                requiredPoliceCars,
                requiredMotorcycles,
                requiredK9Cars
            )
        )
    }

    private fun findValidSubset(
        filteredSubsets: List<List<PoliceVehicle>>,
        requiredCriminals: Int,
        requiredPoliceCars: Int
    ): List<PoliceVehicle>? {
        for (item in filteredSubsets) {
            if (totalCriminals(item.toMutableList()) >= requiredCriminals && item.size == requiredPoliceCars) {
                return item
            }
        }
        return null
    }

    private fun handleOtherCases(
        emergency: Emergency,
        filteredSubsets: List<List<PoliceVehicle>>,
        requirements: List<Int>
    ): List<PoliceVehicle> {
        var result = searchForLowestIDSubset(filteredSubsets)
        val finalCheck = result
            .groupingBy { it.vehicleType }
            .eachCount()
        finalCheck.toList()

        val policeCarCount = finalCheck[VehicleType.POLICE_CAR] ?: 0

        if (totalCriminalsPoliceVehicles(result) < requirements[0] && policeCarCount == requirements[1]) {
            result = assignedVehicles.generateAllSubsets().getCorrectSubset(
                emergency,
                requirements[2] - 1,
                requirements[REQUIRED_MOTORCYCLE],
                requirements[REQUIRED_K9S]
            )
        } else if (totalCriminalsPoliceVehicles(result) < requirements[0] && requirements[2] == 1) {
            result = assignedVehicles.generateAllSubsets().getCorrectSubset(
                emergency,
                0,
                requirements[REQUIRED_MOTORCYCLE],
                requirements[REQUIRED_K9S]
            )
        }
        return result
    }

    private fun filterByRequirements(
        list: MutableList<List<PoliceVehicle>>,
        requirements: List<Int>
    ): MutableList<List<PoliceVehicle>> {
        val filteredByStaff = list.filter { subset ->
            val staffSum = subset.sumOf { if (it.vehicleState == VehicleState.AT_BASE) it.staffCapacity else 0 }
            staffSum <= availableStaff
        }

        val filteredByDogs = filteredByStaff.filter { subset ->
            val dogSum = subset.count {
                it.vehicleType == VehicleType.K9_POLICE_CAR && it
                    .vehicleState == VehicleState.AT_BASE
            }
            dogSum <= requirements[2]
        }

        val filteredByTypeAmount = filteredByDogs.filter { subset ->
            val groups = subset.groupingBy { it.vehicleType }
                .eachCount()

            val actualPoliceCars = groups[VehicleType.POLICE_CAR] ?: 0
            val actualMotorcycles = groups[VehicleType.POLICE_MOTORCYCLE] ?: 0
            val actualK9Cars = groups[VehicleType.K9_POLICE_CAR] ?: 0

            actualPoliceCars <= requirements[0] &&
                actualMotorcycles <= requirements[1] &&
                actualK9Cars <= requirements[2]
        }

        val biggestSubsetSize = filteredByTypeAmount.maxOfOrNull { it.size }
        val biggestSubsets = filteredByTypeAmount.filter { it.size == biggestSubsetSize }

        return biggestSubsets.map { vehicles ->
            vehicles.sortedBy { it.vehicleId }
        }.toMutableList()
    }

    private fun totalCriminals(subset: MutableList<Vehicle>): Int {
        if (subset.isEmpty()) {
            return 0
        }

        var sum = 0
        for (vehicle in subset) {
            if (vehicle.vehicleType == VehicleType.POLICE_CAR && vehicle is PoliceVehicle) {
                sum += vehicle.criminalCapacity
            }
        }
        return sum
    }

    private fun totalCriminalsPoliceVehicles(subset: List<PoliceVehicle>): Int {
        if (subset.isEmpty()) {
            return 0
        }

        var sum = 0
        for (vehicle in subset) {
            if (vehicle.vehicleType == VehicleType.POLICE_CAR) {
                sum += vehicle.criminalCapacity
            }
        }
        return sum
    }

    /**
     * TODO
     *
     * @param r
     * @param currentTick
     */
    override fun handleRequest(r: Request, currentTick: Int) {
        val collectedVehicles = collectVehicles(r.emergency, currentTick)

        r.listOfNearestBases?.takeIf { it.isNotEmpty() }?.removeFirst()

        r.emergency.addToAssignedVehicles(collectedVehicles.second)
        for (vehicle in collectedVehicles.second) {
            vehicle.prepareForEmergency(r.emergency)
        }

        if (r.emergency.comparePolice()) {
            return
        }

        if (r.listOfNearestBases?.isEmpty() != false && !r.emergency.comparePolice()) {
            Logger.logFailedRequest(r.emergency.id)
            return
        }

        if (!r.emergency.comparePolice()) {
            emcc?.addRequestToQueue(
                Request(
                    r.emergency,
                    r.requestID,
                    r.initialRequester,
                    r.listOfNearestBases
                )
            )
        }
    }
}
