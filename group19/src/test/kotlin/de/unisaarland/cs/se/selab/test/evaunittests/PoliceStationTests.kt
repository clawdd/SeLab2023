package de.unisaarland.cs.se.selab.test.evaunittests
/*
import de.unisaarland.cs.se.selab.base.BaseFactory
import de.unisaarland.cs.se.selab.base.Hospital
import de.unisaarland.cs.se.selab.base.PoliceStation
import de.unisaarland.cs.se.selab.emergency.CrimeEmergencyFactory
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import de.unisaarland.cs.se.selab.vehicle.VehicleMedicalFactory
import de.unisaarland.cs.se.selab.vehicle.VehiclePoliceFactory
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PoliceStationTests {
    private val vertex1 = Vertex(5)
    private val vertex2 = Vertex(6)

    val currentTick = 0

    @Test
    fun testCollection1() {
        val vehicleList = mutableListOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(0, 123, 6, 1), 2)
        )

        val policeStation: PoliceStation = BaseFactory().createPoliceStation(
            123,
            vertex1,
            30,
            0
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = CrimeEmergencyFactory().createCrimeLevelOne(listOf(1, 1, 1, 1), "", "")

        val result = policeStation.collectVehicles(emergency, currentTick)
        assertEquals(0, result[0].vehicleId)
        assertEquals(1, result.size)
    }

    @Test
    fun testCollection2() {
        val vehicleList = mutableListOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(0, 123, 5, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 123, 5, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(2, 123, 5, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(3, 123, 5, 1), 1),
        )

        val ambulance1 = VehicleMedicalFactory().createAmbulance(VehicleIntParameters(4, 321, 5, 1))

        val policeStation: PoliceStation = BaseFactory().createPoliceStation(
            baseID = 123,
            vertex1,
            30,
            0
        ).apply {
            assignedVehicles = vehicleList
        }

        val hospital: Hospital = BaseFactory().createHospital(
            baseID = 321,
            vertex2,
            10,
            0
        ).apply {
            assignedVehicles.add(ambulance1)
        }

        val emergency = CrimeEmergencyFactory().createCrimeLevelTwo(listOf(1, 1, 1, 1), "", "")

        val result1 = policeStation.collectVehicles(emergency, currentTick)
        val result2 = hospital.collectVehicles(emergency, currentTick)

        assertEquals(4, result1.size)
        assertEquals(1, result2.size)
    }

    @Test
    fun testReallocation() {
        val vehicleList = mutableListOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(0, 123, 5, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 123, 5, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(2, 123, 5, 1), 1),
        )

        val ambulance1 = VehicleMedicalFactory().createAmbulance(VehicleIntParameters(4, 321, 5, 1))

        val vehiclesEnRoute = mutableListOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(3, 123, 5, 1), 1).apply {
                vehicleState = VehicleState.EN_ROUTE
            }
        )

        val hospital: Hospital = BaseFactory().createHospital(
            baseID = 321,
            vertex2,
            10,
            0
        ).apply {
            assignedVehicles.add(ambulance1)
        }

        val policeStation: PoliceStation = BaseFactory().createPoliceStation(
            baseID = 123,
            vertex1,
            30,
            0
        ).apply {
            assignedVehicles = vehicleList
        }

        policeStation.assignedVehicles.addAll(vehiclesEnRoute)

        val emergencyLower = CrimeEmergencyFactory().createCrimeLevelOne(listOf(1, 1, 1, 1), "", "")
        emergencyLower.addToAssignedVehicles(vehiclesEnRoute)
        policeStation.assignEmergencyToBase(emergencyLower)
        val emergency = CrimeEmergencyFactory().createCrimeLevelTwo(listOf(1, 1, 1, 1), "", "")

        val result = policeStation.collectVehicles(emergency, currentTick)
        assertEquals(3, result.size)

        emergency.addToAssignedVehicles(result)

        val result2 = policeStation.reallocatePreviouslyAssigned(emergency, currentTick)
        assertEquals(1, result2.size)

        val result3 = hospital.collectVehicles(emergency, currentTick)
        assertEquals(1, result3.size)
    }

    /**
     * can only allocate/collect 1 vehicle because staff is not enough for more
     */
    @Test
    fun testReallocation2() {
        val vehicleList = mutableListOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(0, 123, 5, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 123, 5, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(2, 123, 5, 1), 1),
        )

        val ambulance1 = VehicleMedicalFactory().createAmbulance(VehicleIntParameters(4, 321, 5, 1))

        val vehiclesEnRoute = mutableListOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(3, 123, 5, 1), 1).apply {
                vehicleState = VehicleState.EN_ROUTE
            }
        )

        val hospital: Hospital = BaseFactory().createHospital(
            baseID = 321,
            vertex2,
            10,
            0
        ).apply {
            assignedVehicles.add(ambulance1)
        }

        val policeStation: PoliceStation = BaseFactory().createPoliceStation(
            baseID = 123,
            vertex1,
            9,
            0
        ).apply {
            assignedVehicles = vehicleList
        }

        policeStation.assignedVehicles.addAll(vehiclesEnRoute)

        val emergencyLower = CrimeEmergencyFactory().createCrimeLevelOne(listOf(1, 1, 1, 1), "", "")
        emergencyLower.addToAssignedVehicles(vehiclesEnRoute)
        policeStation.assignEmergencyToBase(emergencyLower)
        val emergency = CrimeEmergencyFactory().createCrimeLevelTwo(listOf(1, 1, 1, 1), "", "")

        val result = policeStation.collectVehicles(emergency, currentTick)
        assertEquals(1, result.size)

        emergency.addToAssignedVehicles(result)

        val result2 = policeStation.reallocatePreviouslyAssigned(emergency, currentTick)
        assertEquals(1, result2.size)

        val result3 = hospital.collectVehicles(emergency, currentTick)
        assertEquals(1, result3.size)
    }

    /**
     * cannot reallocate because after adding all vehicles but one, the needed criminals > vehicle en route capacity
     */
    @Test
    fun testReallocation3() {
        val vehicleList = mutableListOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(0, 123, 5, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 123, 5, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(2, 123, 5, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(3, 123, 5, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(4, 123, 5, 1), 1),
        )

        val ambulance1 = VehicleMedicalFactory().createAmbulance(VehicleIntParameters(4, 321, 5, 1))

        val vehiclesEnRoute = mutableListOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(3, 123, 5, 1), 0).apply {
                vehicleState = VehicleState.EN_ROUTE
            }
        )

        val hospital: Hospital = BaseFactory().createHospital(
            baseID = 321,
            vertex2,
            30,
            0
        ).apply {
            assignedVehicles.add(ambulance1)
        }

        val policeStation: PoliceStation = BaseFactory().createPoliceStation(
            baseID = 123,
            vertex1,
            30,
            0
        ).apply {
            assignedVehicles = vehicleList
        }

        policeStation.assignedVehicles.addAll(vehiclesEnRoute)

        val emergencyLower = CrimeEmergencyFactory().createCrimeLevelOne(listOf(1, 1, 1, 1), "", "")
        emergencyLower.addToAssignedVehicles(vehiclesEnRoute)
        policeStation.assignEmergencyToBase(emergencyLower)
        val emergency = CrimeEmergencyFactory().createCrimeLevelThree(listOf(1, 1, 1, 1), "", "")

        val result = policeStation.collectVehicles(emergency, currentTick)
        assertEquals(5, result.size)

        emergency.addToAssignedVehicles(result)

        val result2 = policeStation.reallocatePreviouslyAssigned(emergency, currentTick)
        assertEquals(0, result2.size)

        val result3 = hospital.collectVehicles(emergency, currentTick)
        assertEquals(1, result3.size)
    }

    @Test
    fun testCollection4() {
        val vehicleList = mutableListOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(0, 123, 6, 1), 0),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 123, 5, 1), 2)
        )

        val policeStation: PoliceStation = BaseFactory().createPoliceStation(
            123,
            vertex1,
            10,
            0
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = CrimeEmergencyFactory().createCrimeLevelOne(listOf(1, 1, 1, 1), "", "")

        val result = policeStation.collectVehicles(emergency, currentTick)
        assertEquals(1, result.size)
    }

    @Test
    fun testCollection6() {
        val policeStation: PoliceStation = BaseFactory().createPoliceStation(
            123,
            vertex1,
            30,
            2
        ).apply {
            assignedVehicles = mutableListOf()
        }

        val emergency = CrimeEmergencyFactory().createCrimeLevelOne(listOf(1, 1, 1, 1), "", "")

        val result = policeStation.collectVehicles(emergency, currentTick)
        assertEquals(0, result.size)
    }

    @Test
    fun testCollection7() {
        val vehicleList = mutableListOf(
            VehiclePoliceFactory().createPoliceMotorcycle(VehicleIntParameters(7, 123, 2, 1)),
            VehiclePoliceFactory().createK9PoliceCar(VehicleIntParameters(15, 123, 2, 1)),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(4, 123, 5, 1), 2),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(10, 123, 5, 1), 2),
            VehiclePoliceFactory().createK9PoliceCar(VehicleIntParameters(14, 123, 2, 1)),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(11, 123, 5, 1), 2),
            VehiclePoliceFactory().createPoliceMotorcycle(VehicleIntParameters(6, 123, 2, 1)),
            VehiclePoliceFactory().createPoliceMotorcycle(VehicleIntParameters(12, 123, 2, 1)),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 123, 5, 1), 2),
            VehiclePoliceFactory().createPoliceMotorcycle(VehicleIntParameters(13, 123, 2, 1)),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(5, 123, 5, 1), 2),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(0, 123, 5, 1), 2),
            VehiclePoliceFactory().createK9PoliceCar(VehicleIntParameters(8, 123, 2, 1)),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(3, 123, 5, 1), 2),
            VehiclePoliceFactory().createK9PoliceCar(VehicleIntParameters(9, 123, 2, 1)),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(2, 123, 5, 1), 2),
        )

        val policeStation: PoliceStation = BaseFactory().createPoliceStation(
            123,
            vertex1,
            200,
            20
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = CrimeEmergencyFactory().createCrimeLevelThree(listOf(1, 1, 1, 1), "", "")

        val result = policeStation.collectVehicles(emergency, currentTick)

        assertEquals(10, result.size)
        assertEquals(0, result[0].vehicleId)
        assertEquals(1, result[1].vehicleId)
        assertEquals(2, result[2].vehicleId)
        assertEquals(3, result[3].vehicleId)
        assertEquals(4, result[4].vehicleId)
        assertEquals(5, result[5].vehicleId)
        assertEquals(6, result[6].vehicleId)
        assertEquals(7, result[7].vehicleId)
        assertEquals(8, result[8].vehicleId)
        assertEquals(9, result[9].vehicleId)
    }

    @Test
    fun testReallocNoLowerEM() {
        val vehicleList = mutableListOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(0, 123, 5, 1), 0),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 123, 5, 1), 0),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(2, 123, 95, 1), 0),
        )

        val vehiclesEnRoute = mutableListOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(10, 123, 5, 1), 1).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(11, 123, 5, 1), 1).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
            VehiclePoliceFactory().createK9PoliceCar(VehicleIntParameters(12, 123, 5, 1)).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
        )

        val policeStation: PoliceStation = BaseFactory().createPoliceStation(
            123,
            vertex1,
            100,
            10
        ).apply {
            assignedVehicles = vehicleList
        }

        policeStation.assignedVehicles.addAll(vehiclesEnRoute)

        val emergencySame = CrimeEmergencyFactory().createCrimeLevelTwo(listOf(1, 1, 1, 1), "", "")
        emergencySame.addToAssignedVehicles(vehiclesEnRoute)
        policeStation.assignEmergencyToBase(emergencySame)
        val emergency = CrimeEmergencyFactory().createCrimeLevelTwo(listOf(1, 1, 1, 1), "", "")

        val result = policeStation.collectVehicles(emergency, currentTick)
        assertEquals(2, result.size)
        assertEquals(0, result[0].vehicleId)
        assertEquals(1, result[1].vehicleId)

        emergency.addToAssignedVehicles(result)

        val result2 = policeStation.reallocatePreviouslyAssigned(emergency, currentTick)
        assertEquals(0, result2.size)
    }
}
*/
