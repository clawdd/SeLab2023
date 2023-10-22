package de.unisaarland.cs.se.selab.emergency

import de.unisaarland.cs.se.selab.enumtype.EmergencyState
import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.vehicle.FireVehicle
import de.unisaarland.cs.se.selab.vehicle.MedicalVehicle
import de.unisaarland.cs.se.selab.vehicle.PoliceVehicle
import de.unisaarland.cs.se.selab.vehicle.Vehicle

/**
 * Emergency Class
 */
class Emergency(
    val id: Int,
    val occurringVillage: String,
    val roadName: String,
    var occurringTick: Int,
    var handleTime: Int,
    val maxDuration: Int,
) : Comparable<Emergency> {

    var severity: Int = 0
    var emergencyType: EmergencyType = EmergencyType.FACTORY

    var emergencyState: EmergencyState = EmergencyState.PENDING
    val lastPossibleTick: Int = occurringTick + maxDuration

    var requiredVehicles: List<Vehicle> = emptyList()
    val assignedVehicles: MutableList<Vehicle> = mutableListOf<Vehicle>()

    var requiredPatients: Int = 0
    var requiredCriminals: Int = 0
    var requiredWater: Int = 0

    /**
     * updates the emergency status accordingly
     */
    fun updateEmergencyStatus(
        currentTick: Int,
        stateToID: MutableMap<Int, EmergencyState>
    ) {
        when (this.emergencyState) {
            EmergencyState.PENDING -> updatePendingState(currentTick, stateToID)
            EmergencyState.ALL_ASSETS_ASSIGNED -> updateAllAssetsAssignedState(currentTick, stateToID)
            EmergencyState.CURRENTLY_BEING_HANDLED ->
                updateCurrentlyBeingHandledState(currentTick, stateToID)
            else -> return
        }
    }

    /**
     * holds update logic for the PENDING_STATE state
     */
    private fun updatePendingState(
        currentTick: Int,
        stateToID: MutableMap<Int, EmergencyState>
    ) {
        if (lastPossibleTick < currentTick) {
            this.emergencyState = EmergencyState.FAIL
            stateToID.put(this.id, this.emergencyState)
            for (vehicle in assignedVehicles) {
                vehicle.sendBackToBase()
            }
            Map.findEmergencyRoad(this).emergencyHappeningOnRoad = false
            return
        } else {
            if (compare()) {
                emergencyState = EmergencyState.ALL_ASSETS_ASSIGNED
            }
        }
    }

    /**
     * holds update logic for the ALL_ASSETS_ASSIGNED state
     */
    private fun updateAllAssetsAssignedState(
        currentTick: Int,
        stateToID: MutableMap<Int, EmergencyState>
    ) {
        if (lastPossibleTick < currentTick) {
            this.emergencyState = EmergencyState.FAIL
            stateToID.put(this.id, this.emergencyState)
            for (vehicle in assignedVehicles) {
                vehicle.sendBackToBase()
            }
            Map.findEmergencyRoad(this).emergencyHappeningOnRoad = false
            return
        } else {
            if (!compare()) {
                this.emergencyState = EmergencyState.PENDING
                return
            }
            if (assignedVehicles.all { it.vehicleState == VehicleState.AT_EMERGENCY }) {
                this.emergencyState = EmergencyState.CURRENTLY_BEING_HANDLED
                stateToID.put(this.id, this.emergencyState)
            }
        }
    }

    /**
     * holds update logic for the CURRENTLY_BEING_HANDLED state
     */
    private fun updateCurrentlyBeingHandledState(
        currentTick: Int,
        stateToID: MutableMap<Int, EmergencyState>
    ) {
        if (lastPossibleTick < currentTick) {
            this.emergencyState = EmergencyState.FAIL
            stateToID.put(this.id, this.emergencyState)
            for (vehicle in assignedVehicles) {
                vehicle.sendBackToBase()
            }
            Map.findEmergencyRoad(this).emergencyHappeningOnRoad = false
        } else {
            handleTime--
            if (handleTime == 0) {
                this.consumeResources()
                this.emergencyState = EmergencyState.SUCCESSFUL
                stateToID.put(this.id, this.emergencyState)
                for (vehicle in assignedVehicles) {
                    vehicle.sendBackToBase()
                }
                Map.findEmergencyRoad(this).emergencyHappeningOnRoad = false
            }
        }
    }

    /**
     * returns what vehicles are needed to fulfill this emergency
     */
    fun inferRequiredVehicles(): List<Vehicle> {
        val inferredList: MutableList<Vehicle> = requiredVehicles.toMutableList()

        for (aVehicle in assignedVehicles) {
            for ((index, rVehicle) in inferredList.withIndex()) {
                if (rVehicle.vehicleType == aVehicle.vehicleType) {
                    inferredList.removeAt(index)
                    break
                }
            }
        }
        return inferredList
    }

    /**
     * compare the list with the assigned vehicles list
     */
    fun compare(): Boolean {
        val requiredVehicles: List<Vehicle> = inferRequiredVehicles()
        val sumOfWater: Int = assignedVehicles.sumOf {
            if (it is FireVehicle && it.vehicleType == VehicleType.FIRE_TRUCK_WATER) it.waterCapacity else 0
        }
        val sumOfCrimCapacity: Int = assignedVehicles.sumOf {
            if (it is PoliceVehicle && it.vehicleType == VehicleType.POLICE_CAR) it.criminalCapacity else 0
        }

        if (requiredVehicles.isEmpty()) {
            if (sumOfWater >= requiredWater && sumOfCrimCapacity >= requiredCriminals) {
                return true
            }
        }
        return false
    }

    /**
     * @param vl: List of vehicles
     */
    fun addToAssignedVehicles(vl: List<Vehicle>) {
        this.assignedVehicles.addAll(vl)
    }

    override fun compareTo(other: Emergency): Int {
        val cmp = this.severity.compareTo(other.severity)
        if (cmp != 0) {
            return cmp
        }
        return this.id.compareTo(other.id)
    }

    /**
     * Calculates and performs the resource decrementing on vehicles
     */
    private fun consumeResources() {
        val providers: List<Vehicle> = this.getProviders()

        for (provider in providers) {
            when (provider) {
                is FireVehicle -> this.consumeWater(provider)
                is PoliceVehicle -> this.consumeCriminalCapacity(provider)
                is MedicalVehicle -> this.consumePatientCapacity(provider)
            }
        }
    }

    /**
     * Consumes certain amounts of water from a fire truck
     */
    private fun consumeWater(provider: FireVehicle) {
        if (this.requiredWater == 0) {
            return
        }

        // if truck has exactly enough or less water than needed, subtract its entire stock
        if (this.requiredWater >= provider.waterLevel) {
            val entireStock: Int = provider.waterLevel
            provider.useWater(entireStock)
            this.requiredWater -= entireStock
            provider.recovering = true
            return
        }

        // if truck can provide more water than necessary, provide enough to put out fire
        if (this.requiredWater < provider.waterLevel) {
            provider.useWater(this.requiredWater)
            this.requiredWater = 0
            return
        }
    }

    /**
     * loads the appropriate amount of criminals into a police car
     */
    private fun consumeCriminalCapacity(provider: PoliceVehicle) {
        if (this.requiredCriminals == 0) {
            return
        }

        val freeSeats: Int = provider.criminalCapacity - provider.criminalsOnBoard
        // if police car has exactly or less capacity than needed, fill up every seat
        if (this.requiredCriminals >= freeSeats) {
            provider.loadCriminals(freeSeats)
            this.requiredCriminals -= freeSeats
            provider.recovering = true
            return
        }

        // if police car has more capacity than needed, load only as much criminals as necessary
        if (this.requiredCriminals < freeSeats) {
            provider.loadCriminals(this.requiredCriminals)
            this.requiredCriminals = 0
            return
        }
    }

    /**
     * Loads a patient into an ambulance, if necessary and possible
     */
    private fun consumePatientCapacity(provider: MedicalVehicle) {
        if (requiredPatients > 0 && !provider.patient) {
            provider.loadPatient()
            provider.recovering = true
        }
    }

    /**
     * Returns a list of all vehicles that can provide the required resources
     */
    private fun getProviders(): List<Vehicle> {
        val providers: MutableList<Vehicle> = mutableListOf()

        // if water is required, adds all currently assigned fire trucks to list
        if (this.requiredWater > 0) {
            providers += this.assignedVehicles.filter { it.vehicleType == VehicleType.FIRE_TRUCK_WATER }
        }

        // if criminal capacity is required, adds all police vehicles to list
        if (this.requiredCriminals > 0) {
            providers += this.assignedVehicles.filter { it.vehicleType == VehicleType.POLICE_CAR }
        }

        // if patient capacity is required, adds all amublances to list
        if (this.requiredPatients > 0) {
            providers += this.assignedVehicles.filter { it.vehicleType == VehicleType.AMBULANCE }
        }

        // sort list by vehicleID
        return providers.sortedBy { it.vehicleId }
    }

    /**
     * TODO
     *
     */
    fun compareMedicalStation(): Boolean {
        val requiredFilterByType = requiredVehicles.filter {
            it.vehicleType == VehicleType.AMBULANCE ||
                it.vehicleType == VehicleType.EMERGENCY_DOCTOR_CAR
        }

        val assignedFilterByType = assignedVehicles.filter {
            it.vehicleType == VehicleType.AMBULANCE ||
                it.vehicleType == VehicleType.EMERGENCY_DOCTOR_CAR
        }

        return requiredFilterByType.size == assignedFilterByType.size
    }

    /**
     * TODO
     *
     * @return
     */
    fun compareFireStation(): Boolean {
        val requiredFilterByType = requiredVehicles.filter {
            it.vehicleType == VehicleType.FIRE_TRUCK_TECHNICAL ||
                it.vehicleType == VehicleType.FIRE_TRUCK_LADDER ||
                it.vehicleType == VehicleType.FIRE_TRUCK_WATER ||
                it.vehicleType == VehicleType.FIREFIGHTER_TRANSPORTER
        }

        val assignedFilterByType = assignedVehicles.filter {
            it.vehicleType == VehicleType.FIRE_TRUCK_TECHNICAL ||
                it.vehicleType == VehicleType.FIRE_TRUCK_LADDER ||
                it.vehicleType == VehicleType.FIRE_TRUCK_WATER ||
                it.vehicleType == VehicleType.FIREFIGHTER_TRANSPORTER
        }
        val sumOfWater: Int = assignedVehicles.sumOf {
            if (it is FireVehicle && it.vehicleType == VehicleType.FIRE_TRUCK_WATER) it.waterCapacity else 0
        }
        if (assignedFilterByType.size == requiredFilterByType.size) {
            if (sumOfWater >= requiredWater) {
                return true
            }
        }
        return false
    }

    /**
     * TODO
     *
     * @return
     */
    fun comparePolice(): Boolean {
        val requiredFilterByType = requiredVehicles.filter {
            it.vehicleType == VehicleType.POLICE_CAR ||
                it.vehicleType == VehicleType.K9_POLICE_CAR ||
                it.vehicleType == VehicleType.POLICE_MOTORCYCLE
        }

        val assignedFilterByType = assignedVehicles.filter {
            it.vehicleType == VehicleType.POLICE_CAR ||
                it.vehicleType == VehicleType.K9_POLICE_CAR ||
                it.vehicleType == VehicleType.POLICE_MOTORCYCLE
        }

        val sumOfCrimCapacity: Int = assignedVehicles.sumOf {
            if (it is PoliceVehicle && it.vehicleType == VehicleType.POLICE_CAR) it.criminalCapacity else 0
        }
        if (assignedFilterByType.size == requiredFilterByType.size) {
            if (sumOfCrimCapacity >= requiredCriminals) {
                return true
            }
        }
        return false
    }
}
