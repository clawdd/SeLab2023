package de.unisaarland.cs.se.selab.base

import de.unisaarland.cs.se.selab.Logger
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.DISTANCE_TO_DURATION_FACTOR
import de.unisaarland.cs.se.selab.vehicle.FireVehicle
import de.unisaarland.cs.se.selab.vehicle.Vehicle
import de.unisaarland.cs.se.selab.vehicle.generateAllSubsets
import de.unisaarland.cs.se.selab.vehicle.searchForLowestIDSubset
import kotlin.math.ceil

const val REQUIRED_TECHNICAL = 3
const val REQUIRED_LADDER = 4
const val REQUIRED_TRANSPORTER = 3
const val REQUIRED_TECHNICAL_V2 = 5

/**
 * Firestation
 */
class FireStation(
    baseID: Int,
    location: Vertex,
    availableStaff: Int,
    assignedEmergencies: MutableList<Emergency>,
    var assignedVehicles: MutableList<FireVehicle>,
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

    /**
     * TODO
     *
     * @param e
     * @return
     */
    override fun collectVehicles(e: Emergency, currentTick: Int): Pair<List<Vehicle>, List<Vehicle>> {
        val group = e.inferRequiredVehicles()
            .groupingBy { it.vehicleType }
            .eachCount()

        val remainingTime = e.lastPossibleTick - e.handleTime - currentTick

        val requiredWaterTrucks = group[VehicleType.FIRE_TRUCK_WATER] ?: 0
        val requiredTransporter = group[VehicleType.FIREFIGHTER_TRANSPORTER] ?: 0
        val requiredLadder = group[VehicleType.FIRE_TRUCK_LADDER] ?: 0
        val requiredTechnical = group[VehicleType.FIRE_TRUCK_TECHNICAL] ?: 0

        val assignedFiltered = assignedVehicles
            .filter { it.vehicleState == VehicleState.AT_BASE && !it.unavailability && !it.recovering }
        // filter the to create the first list

        val staffableVehicles = assignedFiltered.filter {
            it.staffCapacity <= availableStaff
        }.toMutableList()

        staffableVehicles.removeAll {
            it.vehicleType == VehicleType.FIRE_TRUCK_WATER &&
                requiredWaterTrucks == 0
        }
        staffableVehicles.removeAll {
            it.vehicleType == VehicleType.FIRE_TRUCK_LADDER &&
                requiredLadder == 0
        }
        staffableVehicles.removeAll {
            it.vehicleType == VehicleType.FIREFIGHTER_TRANSPORTER &&
                requiredTransporter == 0
        }
        staffableVehicles.removeAll {
            it.vehicleType == VehicleType.FIRE_TRUCK_TECHNICAL &&
                requiredTechnical == 0
        }

        // staff capacity filter und required für emergency, wenn nicht leer aber die leer dann
        // keine request

        val filterByTime = staffableVehicles.filter {
            ceil(
                Navigation.distanceCalcVehicleToEmergency(it, e).first /
                    DISTANCE_TO_DURATION_FACTOR
            ).toInt() <= remainingTime
        }

        val allSubsets = filterByTime.generateAllSubsets()

        // val allSubsets = availableVehicles.generateAllSubsets()
        val subset = allSubsets.getCorrectSubset(
            e,
            requiredWaterTrucks,
            requiredTransporter,
            requiredLadder,
            requiredTechnical
        )

        val usedStaff = subset.sumOf { it.staffCapacity }
        availableStaff - usedStaff
        return Pair(staffableVehicles, subset)
    }

    /**
     * TODO
     *
     * @param e
     * @return
     */
    override fun reallocatePreviouslyAssigned(e: Emergency, currentTick: Int): List<FireVehicle> {
        val group = e.inferRequiredVehicles()
            .groupingBy { it.vehicleType }
            .eachCount()

        val remainingTime = e.lastPossibleTick - e.handleTime - currentTick

        val requiredWaterTrucks = group[VehicleType.FIRE_TRUCK_WATER] ?: 0
        val requiredTransporter = group[VehicleType.FIREFIGHTER_TRANSPORTER] ?: 0
        val requiredLadder = group[VehicleType.FIRE_TRUCK_LADDER] ?: 0
        val requiredTechnical = group[VehicleType.FIRE_TRUCK_TECHNICAL] ?: 0

        val severity = e.severity

        val readyForRealloc = assignedEmergencies
            .filter { it.severity < severity }
            .flatMap { it.assignedVehicles }
            .filterIsInstance<FireVehicle>()
            .filter {
                it.vehicleState == VehicleState.EN_ROUTE &&
                    !it.unavailability
            }.toMutableList()

        readyForRealloc.addAll(
            this.assignedVehicles.filter {
                it.vehicleState == VehicleState.RETURNING && (!it.unavailability || !it.recovering)
            }
        )

        val filterByTime = readyForRealloc.filter {
            ceil(
                Navigation.distanceCalcVehicleToEmergency(it, e).first /
                    DISTANCE_TO_DURATION_FACTOR
            ).toInt() <= remainingTime
        }

        val allSubsets = filterByTime.generateAllSubsets()
        return allSubsets.getCorrectSubset(
            e,
            requiredWaterTrucks,
            requiredTransporter,
            requiredLadder,
            requiredTechnical
        )
    }

    private fun List<List<FireVehicle>>.getCorrectSubset(
        emergency: Emergency,
        requiredWaterTrucks: Int,
        requiredTransporter: Int,
        requiredLadder: Int,
        requiredTechnical: Int
    ): List<FireVehicle> {
        val group = emergency.requiredVehicles
            .groupingBy { it.vehicleType }
            .eachCount()

        val originalRequiredWaterTrucks = group[VehicleType.FIRE_TRUCK_WATER] ?: 0
        val requiredWater = emergency.requiredWater - totalWater(emergency.assignedVehicles)
        val requiredVehicles = emergency.inferRequiredVehicles()

        var requiredLadderLength = 0

        for (vehicle in requiredVehicles) {
            if (vehicle.vehicleType == VehicleType.FIRE_TRUCK_LADDER && vehicle is FireVehicle) {
                requiredLadderLength = vehicle.ladderLength
            }
        }

        val filteredSubsets = filterByRequirements(
            this.toMutableList(),
            listOf(
                requiredWaterTrucks,
                requiredTransporter,
                requiredLadder,
                requiredTechnical,
                requiredLadderLength
            )
        )

        val validSubset = findValidSubset(filteredSubsets, requiredWater, requiredWaterTrucks)

        if (validSubset != null) {
            return validSubset
        }

        return handleOtherCases(
            emergency,
            filteredSubsets,
            listOf(
                requiredWater,
                originalRequiredWaterTrucks,
                requiredWaterTrucks,
                requiredTransporter,
                requiredLadder,
                requiredTechnical
            )
        )
    }

    private fun findValidSubset(
        filteredSubsets: List<List<FireVehicle>>,
        requiredWater: Int,
        requiredWaterTrucks: Int
    ): List<FireVehicle>? {
        for (item in filteredSubsets) {
            if (totalWaterFireVehicles(item.toMutableList()) >= requiredWater && item.size == requiredWaterTrucks) {
                return item
            }
        }
        return null
    }

    private fun handleOtherCases(
        emergency: Emergency,
        filteredSubsets: List<List<FireVehicle>>,
        requirements: List<Int>,
    ): List<FireVehicle> {
        var result = searchForLowestIDSubset(filteredSubsets)
        val finalCheck = result
            .groupingBy { it.vehicleType }
            .eachCount()
        finalCheck.toList()

        val fireTruckCount = finalCheck[VehicleType.FIRE_TRUCK_WATER] ?: 0

        if (totalWaterFireVehicles(result) < requirements[0] && fireTruckCount == requirements[1]) {
            result = assignedVehicles.generateAllSubsets().getCorrectSubset(
                emergency,
                requirements[2] - 1,
                requirements[REQUIRED_TRANSPORTER],
                requirements[REQUIRED_LADDER],
                requirements[REQUIRED_TECHNICAL_V2]
            )
        } else if (totalWaterFireVehicles(result) < requirements[0] && requirements[2] == 1) {
            result = assignedVehicles.generateAllSubsets().getCorrectSubset(
                emergency,
                0,
                requirements[REQUIRED_TRANSPORTER],
                requirements[REQUIRED_LADDER],
                requirements[REQUIRED_TECHNICAL_V2]
            )
        }
        return result
    }

    /**
     * TODO
     *
     * @param list
     * @param requirements
     * @return
     */
    private fun filterByRequirements(
        list: MutableList<List<FireVehicle>>,
        requirements: List<Int>
    ): MutableList<List<FireVehicle>> {
        val filteredByStaff = list.filter { subset ->
            val staffSum = subset.sumOf { if (it.vehicleState == VehicleState.AT_BASE) it.staffCapacity else 0 }
            staffSum <= availableStaff
        }

        val filteredByTypeAmount = filteredByStaff.filter { subset ->
            val groups = subset.groupingBy { it.vehicleType }
                .eachCount()

            val actualWaterTrucks = groups[VehicleType.FIRE_TRUCK_WATER] ?: 0
            val actualTransporterTrucks = groups[VehicleType.FIREFIGHTER_TRANSPORTER] ?: 0
            val actualLadderTrucks = groups[VehicleType.FIRE_TRUCK_LADDER] ?: 0
            val actualTechnicalTrucks = groups[VehicleType.FIRE_TRUCK_TECHNICAL] ?: 0

            actualWaterTrucks <= requirements[0] &&
                actualTransporterTrucks <= requirements[1] &&
                actualLadderTrucks <= requirements[2] &&
                actualTechnicalTrucks <= requirements[REQUIRED_TECHNICAL]
        }

        val filteredByLadderLength = filteredByTypeAmount.filter { subset ->
            subset.none {
                it.vehicleType == VehicleType.FIRE_TRUCK_LADDER &&
                    it.ladderLength < requirements[REQUIRED_LADDER]
            }
        }

        val biggestSubsetSize = filteredByLadderLength.maxOfOrNull { it.size }
        val biggestSubsets = filteredByLadderLength.filter { it.size == biggestSubsetSize }

        return biggestSubsets.map { vehicles ->
            vehicles.sortedBy { it.vehicleId }
        }.toMutableList()
    }

    /**
     * TODO
     *
     * @param subset
     * @return
     */
    private fun totalWater(subset: MutableList<Vehicle>): Int {
        if (subset.isEmpty()) {
            return 0
        }

        var sum = 0
        for (vehicle in subset) {
            if (vehicle.vehicleType == VehicleType.FIRE_TRUCK_WATER && vehicle is FireVehicle) {
                sum += vehicle.waterCapacity
            }
        }
        return sum
    }

    /**
     * TODO
     *
     * @param subset
     * @return
     */
    private fun totalWaterFireVehicles(subset: List<FireVehicle>): Int {
        if (subset.isEmpty()) {
            return 0
        }

        var sum = 0
        for (vehicle in subset) {
            if (vehicle.vehicleType == VehicleType.FIRE_TRUCK_WATER) {
                sum += vehicle.waterCapacity
            }
        }
        return sum
    }

    override fun handleRequest(r: Request, currentTick: Int) {
        val collectedVehicles = collectVehicles(r.emergency, currentTick)

        r.listOfNearestBases?.takeIf { it.isNotEmpty() }?.removeFirst()

        r.emergency.addToAssignedVehicles(collectedVehicles.second)
        for (vehicle in collectedVehicles.second) {
            vehicle.prepareForEmergency(r.emergency)
        }

        if (r.emergency.compareFireStation()) {
            return
        }

        if (r.listOfNearestBases?.isEmpty() != false && !r.emergency.compareFireStation()) {
            Logger.logFailedRequest(r.emergency.id)
            return
        }

        if (!r.emergency.compareFireStation()) {
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
