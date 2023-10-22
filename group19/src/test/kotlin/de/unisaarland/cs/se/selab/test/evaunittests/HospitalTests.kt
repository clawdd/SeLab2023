package de.unisaarland.cs.se.selab.test.evaunittests
/*
import de.unisaarland.cs.se.selab.base.BaseFactory
import de.unisaarland.cs.se.selab.base.Hospital
import de.unisaarland.cs.se.selab.emergency.MedicalEmergencyFactory
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import de.unisaarland.cs.se.selab.vehicle.VehicleMedicalFactory
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HospitalTests {
    val vertex = Vertex(5)
    val currentTick = 0

    @Test
    fun testCollection1() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(0, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(2, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 123, 5, 1)),
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            10,
            0
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = MedicalEmergencyFactory().createMedicalLevelOne(listOf(1, 1, 1, 1), "", "")

        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(0, result[0].vehicleId)
        assertEquals(1, result.size)
    }

    @Test
    fun testCollection2_1() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(0, 123, 100, 1))
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            10,
            0
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = MedicalEmergencyFactory().createMedicalLevelOne(listOf(1, 1, 1, 1), "", "")

        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(0, result.size)
    }

    @Test
    fun testCollection2_2() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(0, 123, 5, 1))
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            0,
            0
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = MedicalEmergencyFactory().createMedicalLevelOne(listOf(1, 1, 1, 1), "", "")

        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(0, result.size)
    }

    @Test
    fun testReallocation() {
        val vehiclesEnRouteToEmergency = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(0, 123, 5, 1)).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
        )

        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(2, 123, 4, 1))
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            30,
            2
        ).apply {
            assignedVehicles = (vehicleList + vehiclesEnRouteToEmergency).toMutableList()
        }

        val emergencyLower = MedicalEmergencyFactory().createMedicalLevelOne(listOf(1, 1, 1, 1), "", "")
        emergencyLower.addToAssignedVehicles(vehiclesEnRouteToEmergency)
        hospital.assignEmergencyToBase(emergencyLower)
        val emergency = MedicalEmergencyFactory().createMedicalLevelTwo(listOf(1, 1, 1, 1), "", "")

        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(2, result.size)

        emergency.addToAssignedVehicles(result)

        val result2 = hospital.reallocatePreviouslyAssigned(emergency, currentTick)
        assertEquals(1, result2.size)
    }

    @Test
    fun testCollection3() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(0, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(2, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(3, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(4, 123, 4, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(5, 123, 4, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(6, 123, 4, 1)),
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            100,
            10
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = MedicalEmergencyFactory().createMedicalLevelTwo(listOf(1, 1, 1, 1), "", "")

        val result = hospital.collectVehicles(emergency, currentTick)

        assertEquals(3, result.size)
        assertEquals(0, result[0].vehicleId)
        assertEquals(1, result[1].vehicleId)
        assertEquals(4, result[2].vehicleId)
    }

    @Test
    fun testCollection4() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(0, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(2, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(3, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(4, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(5, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(6, 123, 3, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(7, 123, 3, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(8, 123, 3, 1)),
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            100,
            10
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = MedicalEmergencyFactory().createMedicalLevelThree(listOf(1, 1, 1, 1), "", "")
        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(7, result.size)
        assertEquals(0, result[0].vehicleId)
        assertEquals(1, result[1].vehicleId)
        assertEquals(2, result[2].vehicleId)
        assertEquals(3, result[3].vehicleId)
        assertEquals(4, result[4].vehicleId)
        assertEquals(6, result[5].vehicleId)
        assertEquals(7, result[6].vehicleId)
    }

    @Test
    fun testReallocation1() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(0, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(2, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(3, 123, 3, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(4, 123, 3, 1)),
        )
        val vehiclesEnRoute = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(10, 123, 5, 1)).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            100,
            10
        ).apply {
            assignedVehicles = vehicleList
        }

        hospital.assignedVehicles.addAll(vehiclesEnRoute)

        val emergencyLower = MedicalEmergencyFactory().createMedicalLevelOne(listOf(1, 1, 1, 1), "", "")
        emergencyLower.addToAssignedVehicles(vehiclesEnRoute)
        hospital.assignEmergencyToBase(emergencyLower)
        val emergency = MedicalEmergencyFactory().createMedicalLevelThree(listOf(1, 1, 1, 1), "", "")
        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(5, result.size)

        emergency.addToAssignedVehicles(result)

        val result2 = hospital.reallocatePreviouslyAssigned(emergency, currentTick)
        assertEquals(1, result2.size)
    }

    @Test
    fun biggestSubsetPossibleTest() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(3, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(2, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(15, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(6, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(12, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(4, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(8, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(13, 123, 3, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(11, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(5, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(10, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(14, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(0, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(7, 123, 5, 1)),
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            100,
            20
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = MedicalEmergencyFactory().createMedicalLevelThree(listOf(1, 1, 1, 1), "", "")
        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(7, result.size)
        assertEquals(0, result[0].vehicleId)
        assertEquals(1, result[1].vehicleId)
        assertEquals(2, result[2].vehicleId)
        assertEquals(3, result[3].vehicleId)
        assertEquals(4, result[4].vehicleId)
        assertEquals(10, result[5].vehicleId)
        assertEquals(11, result[6].vehicleId)
    }

    @Test
    fun biggestSubsetPossible2Test() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(2, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(15, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(6, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(12, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(4, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(8, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(13, 123, 3, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(11, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(5, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(10, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(14, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(0, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(7, 123, 5, 1)),
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            100,
            20
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = MedicalEmergencyFactory().createMedicalLevelThree(listOf(1, 1, 1, 1), "", "")
        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(7, result.size)
        assertEquals(0, result[0].vehicleId)
        assertEquals(1, result[1].vehicleId)
        assertEquals(2, result[2].vehicleId)
        assertEquals(4, result[3].vehicleId)
        assertEquals(5, result[4].vehicleId)
        assertEquals(10, result[5].vehicleId)
        assertEquals(11, result[6].vehicleId)
    }

    @Test
    fun biggestSubsetPossible3Test() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(15, 123, 3, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(12, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(4, 123, 5000, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(8, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(13, 123, 3, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(11, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(5, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(10, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(14, 123, 3, 1)),
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            100,
            20
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = MedicalEmergencyFactory().createMedicalLevelThree(listOf(1, 1, 1, 1), "", "")
        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(5, result.size)
        assertEquals(1, result[0].vehicleId)
        assertEquals(5, result[1].vehicleId)
        assertEquals(8, result[2].vehicleId)
        assertEquals(10, result[3].vehicleId)
        assertEquals(11, result[4].vehicleId)
    }

    @Test
    fun biggestSubsetPossible4Test() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(15, 123, 3, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(12, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(4, 123, 5, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(8, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(13, 123, 3, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(11, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(5, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(10, 123, 3, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 123, 90, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(14, 123, 3, 1)),
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            100,
            20
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = MedicalEmergencyFactory().createMedicalLevelThree(listOf(1, 1, 1, 1), "", "")
        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(5, result.size)
        assertEquals(4, result[0].vehicleId)
        assertEquals(5, result[1].vehicleId)
        assertEquals(8, result[2].vehicleId)
        assertEquals(10, result[3].vehicleId)
        assertEquals(11, result[4].vehicleId)
    }

    @Test
    fun testReallocation2() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(0, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(1, 123, 3, 1)),
        )
        val vehiclesEnRoute = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(10, 123, 5, 1)).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(11, 123, 5, 1)).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(12, 123, 3, 1)).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            100,
            10
        ).apply {
            assignedVehicles = vehicleList
        }

        hospital.assignedVehicles.addAll(vehiclesEnRoute)

        val emergencyLower = MedicalEmergencyFactory().createMedicalLevelOne(listOf(1, 1, 1, 1), "", "")
        emergencyLower.addToAssignedVehicles(vehiclesEnRoute)
        hospital.assignEmergencyToBase(emergencyLower)
        val emergency = MedicalEmergencyFactory().createMedicalLevelThree(listOf(1, 1, 1, 1), "", "")
        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(2, result.size)
        assertEquals(0, result[0].vehicleId)
        assertEquals(1, result[1].vehicleId)

        emergency.addToAssignedVehicles(result)

        val result2 = hospital.reallocatePreviouslyAssigned(emergency, currentTick)
        assertEquals(3, result2.size)
        assertEquals(10, result2[0].vehicleId)
        assertEquals(11, result2[1].vehicleId)
        assertEquals(12, result2[2].vehicleId)
    }

    @Test
    fun testReallocNoLowerSeverityEM() {
        val vehicleList = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(0, 123, 5, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(1, 123, 3, 1)),
        )
        val vehiclesEnRoute = mutableListOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(10, 123, 5, 1)).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(11, 123, 5, 1)).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(12, 123, 3, 1)).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
        )

        val hospital: Hospital = BaseFactory().createHospital(
            123,
            vertex,
            100,
            10
        ).apply {
            assignedVehicles = vehicleList
        }

        hospital.assignedVehicles.addAll(vehiclesEnRoute)

        val emergencyHigher = MedicalEmergencyFactory().createMedicalLevelThree(listOf(1, 1, 1, 1), "", "")
        emergencyHigher.addToAssignedVehicles(vehiclesEnRoute)
        hospital.assignEmergencyToBase(emergencyHigher)
        val emergency = MedicalEmergencyFactory().createMedicalLevelThree(listOf(1, 1, 1, 1), "", "")
        val result = hospital.collectVehicles(emergency, currentTick)
        assertEquals(2, result.size)
        assertEquals(0, result[0].vehicleId)
        assertEquals(1, result[1].vehicleId)

        emergency.addToAssignedVehicles(result)

        val result2 = hospital.reallocatePreviouslyAssigned(emergency, currentTick)
        assertEquals(0, result2.size)
    }
}
*/
