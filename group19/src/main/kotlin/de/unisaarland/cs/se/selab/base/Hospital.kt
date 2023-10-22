package de.unisaarland.cs.se.selab.base

import de.unisaarland.cs.se.selab.Logger
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.DISTANCE_TO_DURATION_FACTOR
import de.unisaarland.cs.se.selab.vehicle.MedicalVehicle
import de.unisaarland.cs.se.selab.vehicle.Vehicle
import de.unisaarland.cs.se.selab.vehicle.generateAllSubsets
import de.unisaarland.cs.se.selab.vehicle.searchForLowestIDSubset
import kotlin.math.ceil

/**
 * documentation
 */
class Hospital(
    baseID: Int,
    location: Vertex,
    availableStaff: Int,
    assignedEmergencies: MutableList<Emergency>,
    var assignedVehicles: MutableList<MedicalVehicle>,
    var availableDoctors: Int,
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

        val requiredAmbulances = group[VehicleType.AMBULANCE] ?: 0
        val requiredDocCars = group[VehicleType.EMERGENCY_DOCTOR_CAR] ?: 0

        val assignedFiltered = assignedVehicles
            .filter { it.vehicleState == VehicleState.AT_BASE && !it.unavailability && !it.recovering }

        val staffableVehicles = assignedFiltered.filter {
            it.staffCapacity <= availableStaff
        }.toMutableList()

        staffableVehicles.removeAll {
            it.vehicleType == VehicleType.AMBULANCE &&
                requiredAmbulances == 0
        }
        staffableVehicles.removeAll {
            it.vehicleType == VehicleType.EMERGENCY_DOCTOR_CAR &&
                requiredDocCars == 0
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
            requiredAmbulances,
            requiredDocCars
        )

        val usedStaff = subset.sumOf { it.staffCapacity }
        availableStaff - usedStaff
        availableDoctors - subset.count { it.vehicleType == VehicleType.EMERGENCY_DOCTOR_CAR }
        return Pair(staffableVehicles, subset)
    }

    /*
    override fun collectVehicles(e: Emergency): List<MedicalVehicle> {
        val allSubsets = availableVehicles.generateAllSubsets()
        return allSubsets.getCorrectSubset(
            e,
            e.inferRequiredVehicles().size,
            // e.inferRequiredVehicles().size
        )
    }
     */

    override fun reallocatePreviouslyAssigned(e: Emergency, currentTick: Int): List<MedicalVehicle> {
        val group = e.inferRequiredVehicles()
            .groupingBy { it.vehicleType }
            .eachCount()

        val remainingTime = e.lastPossibleTick - e.handleTime - currentTick

        val requiredAmbulances = group[VehicleType.AMBULANCE] ?: 0
        val requiredDocCars = group[VehicleType.EMERGENCY_DOCTOR_CAR] ?: 0

        val severity = e.severity

        val readyForRealloc = assignedEmergencies
            .filter { it.severity < severity }
            .flatMap { it.assignedVehicles }
            .filterIsInstance<MedicalVehicle>()
            .filter { it.vehicleState == VehicleState.EN_ROUTE }.toMutableList()

        readyForRealloc.addAll(
            assignedVehicles.filter {
                it.vehicleState == VehicleState.RETURNING && !it.patient
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
            requiredAmbulances,
            requiredDocCars
        )
    }

    private fun List<List<MedicalVehicle>>.getCorrectSubset(
        emergency: Emergency,
        requiredAmbulances: Int,
        requiredDocCars: Int
    ): List<MedicalVehicle> {
        /*
        val group = emergency.requiredVehicles
            .groupingBy { it.vehicleType }
            .eachCount()
         */

        val requiredVehicles = emergency.inferRequiredVehicles()

        var requiredDoctors = 0

        for (vehicle in requiredVehicles) {
            if (vehicle.vehicleType == VehicleType.EMERGENCY_DOCTOR_CAR && vehicle is MedicalVehicle) {
                requiredDoctors += 1
            }
        }

        val filteredSubsets = filteredByRequirements(
            this.toMutableList(),
            listOf(
                requiredAmbulances,
                requiredDocCars,
                requiredDoctors
            )
        )

        /**
         * ?????????????????????????????????????????????
         */
        return searchForLowestIDSubset(filteredSubsets)
    }

    private fun filteredByRequirements(
        list: MutableList<List<MedicalVehicle>>,
        requirements: List<Int>
    ): MutableList<List<MedicalVehicle>> {
        val filteredByStaff = list.filter { subset ->
            val staffSum = subset.sumOf { if (it.vehicleState == VehicleState.AT_BASE) it.staffCapacity else 0 }
            staffSum <= availableStaff
        }

        val filteredByDocs = filteredByStaff.filter { subset ->
            val docSum = subset.count {
                it.vehicleType == VehicleType.EMERGENCY_DOCTOR_CAR &&
                    it.vehicleState == VehicleState.AT_BASE
            }
            docSum <= availableDoctors
        }

        val filteredByTypeAmount = filteredByDocs.filter { subset ->
            val groups = subset.groupingBy { it.vehicleType }
                .eachCount()

            val actualAmbulances = groups[VehicleType.AMBULANCE] ?: 0
            val actualDocCars = groups[VehicleType.EMERGENCY_DOCTOR_CAR] ?: 0

            actualAmbulances <= requirements[0] &&
                actualDocCars <= requirements[1]
        }

        val biggestSubsetSize = filteredByTypeAmount.maxOfOrNull { it.size }
        val biggestSubsets = filteredByTypeAmount.filter { it.size == biggestSubsetSize }

        return biggestSubsets.map { vehicles ->
            vehicles.sortedBy { it.vehicleId }
        }.toMutableList()
    }
    override fun handleRequest(r: Request, currentTick: Int) {
        val collectedVehicles = collectVehicles(r.emergency, currentTick)

        r.listOfNearestBases?.takeIf { it.isNotEmpty() }?.removeFirst()

        r.emergency.addToAssignedVehicles(collectedVehicles.second)
        for (vehicle in collectedVehicles.second) {
            vehicle.prepareForEmergency(r.emergency)
        }

        if (r.emergency.compareMedicalStation()) {
            return
        }

        if (r.listOfNearestBases?.isEmpty() != false && !r.emergency.compareMedicalStation()) {
            Logger.logFailedRequest(r.emergency.id)
            return
        }

        if (!r.emergency.compareMedicalStation()) {
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
