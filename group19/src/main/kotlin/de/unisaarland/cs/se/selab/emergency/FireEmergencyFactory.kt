package de.unisaarland.cs.se.selab.emergency

import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.vehicle.Vehicle
import de.unisaarland.cs.se.selab.vehicle.VehicleFireFactory
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import de.unisaarland.cs.se.selab.vehicle.VehicleMedicalFactory

const val WATER_SEVERITY_ONE = 1200
const val WATER_SEVERITY_TWO = 3000
const val WATER_SEVERITY_THREE = 5400

// The fact that I even have to do this in the first place should be a
// massive sodding testament to how stupid this detekt config is.
const val FOUR = 4
const val SIX = 6
const val THIRTY = 30
const val FOURTY = 40

/**
 * factory
 */
class FireEmergencyFactory {
    /**
     * creates a fire level one
     */
    fun createFireLevelOne(
        args: List<Int>,
        occurringVillage: String,
        roadName: String,
    ): Emergency {
        return Emergency(
            id = args[0],
            occurringVillage = occurringVillage,
            roadName = roadName,
            occurringTick = args[1],
            handleTime = args[2],
            maxDuration = args[3],
        ).apply {
            severity = 1
            emergencyType = EmergencyType.FIRE
            requiredWater = WATER_SEVERITY_ONE
            requiredVehicles = List(2) {
                VehicleFireFactory().createFireTruckWater(
                    VehicleIntParameters(-1, -1, -1, -1),
                    -1
                )
            }
        }
    }

    /**
     * creates a fire level two
     */
    fun createFireLevelTwo(
        args: List<Int>,
        occurringVillage: String,
        roadName: String,
    ): Emergency {
        val requiredVehiclesList = mutableListOf<Vehicle>().apply {
            addAll(List(FOUR) { VehicleFireFactory().createFireTruckWater(VehicleIntParameters(-1, -1, -1, -1), -1) })
            add(VehicleFireFactory().createFireTruckLadder(VehicleIntParameters(-1, -1, -1, -1), THIRTY))
            add(VehicleFireFactory().createFirefigherTransporter(VehicleIntParameters(-1, -1, -1, -1)))
            add(VehicleMedicalFactory().createAmbulance(VehicleIntParameters(-1, -1, -1, -1)))
        }
        return Emergency(
            id = args[0],
            occurringVillage = occurringVillage,
            roadName = roadName,
            occurringTick = args[1],
            handleTime = args[2],
            maxDuration = args[3],
        ).apply {
            severity = 2
            emergencyType = EmergencyType.FIRE
            requiredWater = WATER_SEVERITY_TWO
            requiredPatients = 1
            requiredVehicles = requiredVehiclesList
        }
    }

    /**
     * creates a fire level three
     */
    fun createFireLevelThree(
        args: List<Int>,
        occurringVillage: String,
        roadName: String,
    ): Emergency {
        val requiredVehiclesList = mutableListOf<Vehicle>().apply {
            addAll(List(SIX) { VehicleFireFactory().createFireTruckWater(VehicleIntParameters(-1, -1, -1, -1), -1) })
            addAll(List(2) { VehicleFireFactory().createFireTruckLadder(VehicleIntParameters(-1, -1, -1, -1), FOURTY) })
            addAll(List(2) { VehicleFireFactory().createFirefigherTransporter(VehicleIntParameters(-1, -1, -1, -1)) })
            addAll(List(2) { VehicleMedicalFactory().createAmbulance(VehicleIntParameters(-1, -1, -1, -1)) })
            add(VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(-1, -1, -1, -1)))
        }
        return Emergency(
            id = args[0],
            occurringVillage = occurringVillage,
            roadName = roadName,
            occurringTick = args[1],
            handleTime = args[2],
            maxDuration = args[3],
        ).apply {
            severity = 3
            emergencyType = EmergencyType.FIRE
            requiredWater = WATER_SEVERITY_THREE
            requiredPatients = 2
            requiredVehicles = requiredVehiclesList
        }
    }
}
