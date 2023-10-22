package de.unisaarland.cs.se.selab.emergency

import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.vehicle.Vehicle
import de.unisaarland.cs.se.selab.vehicle.VehicleFireFactory
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import de.unisaarland.cs.se.selab.vehicle.VehicleMedicalFactory

const val MAGIC_NUMBER_FIVE = 5

/**
 * class documentation
 */
class MedicalEmergencyFactory {
    /**
     * creates a medical level one
     */
    fun createMedicalLevelOne(
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
            emergencyType = EmergencyType.MEDICAL
            requiredVehicles = List(1) { VehicleMedicalFactory().createAmbulance(VehicleIntParameters(-1, -1, -1, -1)) }
        }
    }

    /**
     * creates a medical level two
     */
    fun createMedicalLevelTwo(
        args: List<Int>,
        occurringVillage: String,
        roadName: String,
    ): Emergency {
        val requiredVehiclesList = mutableListOf<Vehicle>().apply {
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
            severity = 2
            emergencyType = EmergencyType.MEDICAL
            requiredPatients = 2
            requiredVehicles = requiredVehiclesList
        }
    }

    /**
     * creates a medical level three
     */
    fun createMedicalLevelThree(
        args: List<Int>,
        occurringVillage: String,
        roadName: String,
    ): Emergency {
        val requiredVehiclesList = mutableListOf<Vehicle>().apply {
            addAll(
                List(
                    MAGIC_NUMBER_FIVE
                ) { VehicleMedicalFactory().createAmbulance(VehicleIntParameters(-1, -1, -1, -1)) }
            )
            addAll(List(2) { VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(-1, -1, -1, -1)) })
            addAll(List(2) { VehicleFireFactory().createFireTruckTechnical(VehicleIntParameters(-1, -1, -1, -1)) })
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
            emergencyType = EmergencyType.MEDICAL
            requiredPatients = MAGIC_NUMBER_FIVE
            requiredVehicles = requiredVehiclesList
        }
    }
}
