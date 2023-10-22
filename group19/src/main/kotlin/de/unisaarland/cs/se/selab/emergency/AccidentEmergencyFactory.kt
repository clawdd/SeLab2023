package de.unisaarland.cs.se.selab.emergency

import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.vehicle.Vehicle
import de.unisaarland.cs.se.selab.vehicle.VehicleFireFactory
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import de.unisaarland.cs.se.selab.vehicle.VehicleMedicalFactory
import de.unisaarland.cs.se.selab.vehicle.VehiclePoliceFactory

const val MAGIC_NUMBER_4 = 4

/**
 *documentation...
 */
class AccidentEmergencyFactory {
    /**
     * creates an accident level one
     */
    fun createAccidentLevelOne(
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
            emergencyType = EmergencyType.ACCIDENT
            requiredVehicles =
                List(1) {
                    VehicleFireFactory().createFireTruckTechnical(
                        VehicleIntParameters(-1, -1, -1, -1)
                    )
                }
        }
    }

    /**
     * creates an accident level two
     */
    fun createAccidentLevelTwo(
        args: List<Int>,
        occurringVillage: String,
        roadName: String,
    ): Emergency {
        val requiredVehiclesList = mutableListOf<Vehicle>().apply {
            addAll(List(2) { VehicleFireFactory().createFireTruckTechnical(VehicleIntParameters(-1, -1, -1, -1)) })
            add(VehiclePoliceFactory().createPoliceMotorcycle(VehicleIntParameters(-1, -1, -1, -1)))
            add(VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(-1, -1, -1, -1), -1))
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
            emergencyType = EmergencyType.ACCIDENT
            requiredPatients = 1
            requiredVehicles = requiredVehiclesList
        }
    }

    /**
     * creates an accident level three
     */
    fun createAccidentLevelThree(
        args: List<Int>,
        occurringVillage: String,
        roadName: String,
    ): Emergency {
        val requiredVehiclesList = mutableListOf<Vehicle>().apply {
            addAll(
                List(
                    MAGIC_NUMBER_4
                ) { VehicleFireFactory().createFireTruckTechnical(VehicleIntParameters(-1, -1, -1, -1)) }
            )
            addAll(List(2) { VehiclePoliceFactory().createPoliceMotorcycle(VehicleIntParameters(-1, -1, -1, -1)) })
            addAll(
                List(
                    MAGIC_NUMBER_4
                ) { VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(-1, -1, -1, -1), -1) }
            )
            addAll(List(3) { VehicleMedicalFactory().createAmbulance(VehicleIntParameters(-1, -1, -1, -1)) })
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
            emergencyType = EmergencyType.ACCIDENT
            requiredPatients = 2
            requiredVehicles = requiredVehiclesList
        }
    }
}
