package maxunittests
/*
import de.unisaarland.cs.se.selab.base.BaseFactory
import de.unisaarland.cs.se.selab.base.FireStation
import de.unisaarland.cs.se.selab.emergency.FireEmergencyFactory
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.VehicleFireFactory
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

/**
 * TODO
 *
 */

class FireStationTests {
    /**
     * TODO
     *
     * @param: list
     * @retur
     */

    /*
    private fun totalWater(list: List<FireVehicle>): Int {
        var sum = 0
        for (v in list) {
            if (v.vehicleType == VehicleType.FIRE_TRUCK_WATER) {
                sum += v.waterCapacity
            }
        }
        return sum
    }
     */
    /**
     * TODO test the collection of vehicles for fire level 1
     * - everything provided
     */

    private val vertex = Vertex(5)
    val currentTick = 0

    @Test
    fun testCollection1() {
        val vehicleList = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(0, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(2, 123, 6, 1), 600)
        )

        val fireStation: FireStation = BaseFactory().createFireStation(
            123,
            vertex,
            30
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 1, 1), "", "")

        val result = fireStation.collectVehicles(emergency, currentTick)
        // println("Vehicle IDs: ${result.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result)}")
        assertEquals(0, result[0].vehicleId)
        assertEquals(1, result[1].vehicleId)
        assertEquals(2, result.size)
    }

    /**
     * TODO test collection for fire emergency level two
     * - wrong ladder length
     */
    @Test
    fun testCollection2() {
        val vehicleList = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(0, 123, 6, 1), 1200),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(2, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(3, 123, 6, 1), 600),
            VehicleFireFactory().createFirefigherTransporter(VehicleIntParameters(4, 123, 6, 1)),
            VehicleFireFactory().createFireTruckLadder(VehicleIntParameters(5, 123, 6, 1), 20)
        )

        val fireStation: FireStation = BaseFactory().createFireStation(123, vertex, 36).apply {
            assignedVehicles = vehicleList
        }

        val emergency = FireEmergencyFactory().createFireLevelTwo(listOf(1, 1, 1, 1), "", "")

        val result = fireStation.collectVehicles(emergency, currentTick)
        // println("Vehicle IDs: ${result.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result)}")
        assertEquals(5, result.size)
    }

    /**
     * TODO bigger subset size matter
     *
     */
    @Test
    fun testCollectionEdgeCase() {
        val vehicleList = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(0, 123, 24, 1), 1200),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(2, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(3, 123, 6, 1), 600)
        )

        val fireStation: FireStation = BaseFactory().createFireStation(
            123,
            vertex,
            30
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = FireEmergencyFactory().createFireLevelTwo(listOf(1, 1, 1, 1), "", "")

        val result = fireStation.collectVehicles(emergency, currentTick)
        // println("Vehicle IDs: ${result.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result)}")
        assertEquals(3, result.size)
    }

    /**
     * TODO fire severity level 3 not enough water
     *
     */
    @Test
    fun testReallocation() {
        val vehicleList = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(0, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(2, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(3, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(4, 123, 6, 1), 600)
        )

        val vehiclesEnRoute = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(5, 123, 6, 1), 2400).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(6, 123, 6, 1), 2400).apply {
                vehicleState = VehicleState.EN_ROUTE
            }
        )

        val fireStation: FireStation = BaseFactory().createFireStation(
            123,
            vertex,
            30
        ).apply {
            assignedVehicles = vehicleList
        }

        fireStation.assignedVehicles.addAll(vehiclesEnRoute)

        val emergencyLower = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 1, 1), "", "")
        emergencyLower.addToAssignedVehicles(vehiclesEnRoute)
        fireStation.assignEmergencyToBase(emergencyLower)
        val emergency = FireEmergencyFactory().createFireLevelThree(listOf(1, 1, 1, 1), "", "")

        val result = fireStation.collectVehicles(emergency, currentTick)
        // println("Vehicle IDs: ${result.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result)}")
        assertEquals(5, result.size)

        emergency.addToAssignedVehicles(result)

        val result2 = fireStation.reallocatePreviouslyAssigned(emergency, currentTick)
        // println("Vehicle IDs: ${result2.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result2)}")
        assertEquals(1, result2.size)
    }

    /**
     * TODO fire severity level 3 not enough
     *
     */
    @Test
    fun testReallocation2() {
        val vehicleList = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(0, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(2, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(3, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(4, 123, 6, 1), 600)
        )

        val vehiclesEnRoute = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(5, 123, 6, 1), 1200).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(6, 123, 6, 1), 2400).apply {
                vehicleState = VehicleState.EN_ROUTE
            }
        )

        val fireStation: FireStation = BaseFactory().createFireStation(
            123,
            vertex,
            36
        ).apply {
            assignedVehicles = vehicleList
        }

        fireStation.assignedVehicles.addAll(vehiclesEnRoute)

        val emergencyLower = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 1, 1), "", "")
        emergencyLower.addToAssignedVehicles(vehiclesEnRoute)
        fireStation.assignEmergencyToBase(emergencyLower)
        val emergency = FireEmergencyFactory().createFireLevelThree(listOf(1, 1, 1, 1), "", "")

        val result = fireStation.collectVehicles(emergency, currentTick)
        // println("Vehicle IDs: ${result.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result)}")
        assertEquals(5, result.size)

        emergency.addToAssignedVehicles(result)

        val result2 = fireStation.reallocatePreviouslyAssigned(emergency, currentTick)
        // println("Vehicle IDs: ${result2.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result2)}")
        assertEquals(1, result2.size)
    }

    @Test
    fun testReallocation3() {
        val vehicleList = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(0, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(2, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(3, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(4, 123, 6, 1), 600)
        )

        val vehiclesEnRoute = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(5, 123, 6, 1), 600).apply {
                vehicleState = VehicleState.EN_ROUTE
            },
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(6, 123, 6, 1), 600).apply {
                vehicleState = VehicleState.EN_ROUTE
            }
        )

        val fireStation: FireStation = BaseFactory().createFireStation(
            123,
            vertex,
            36
        ).apply {
            assignedVehicles = vehicleList
        }

        fireStation.assignedVehicles.addAll(vehiclesEnRoute)

        val emergencyLower = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 1, 1), "", "")
        emergencyLower.addToAssignedVehicles(vehiclesEnRoute)
        fireStation.assignEmergencyToBase(emergencyLower)
        val emergency = FireEmergencyFactory().createFireLevelThree(listOf(1, 1, 1, 1), "", "")

        val result = fireStation.collectVehicles(emergency, currentTick)
        // println("Vehicle IDs: ${result.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result)}")
        assertEquals(5, result.size)

        emergency.addToAssignedVehicles(result)

        val result2 = fireStation.reallocatePreviouslyAssigned(emergency, currentTick)
        // println("Vehicle IDs: ${result2.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result2)}")
        assertEquals(0, result2.size)
    }

    /**
     * TODO only one vehicle
     *
     */
    @Test
    fun testCollection4() {
        val vehicleList = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(0, 123, 6, 1), 600)
        )

        val fireStation: FireStation = BaseFactory().createFireStation(
            123,
            vertex,
            30
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 1, 1), "", "")

        val result = fireStation.collectVehicles(emergency, currentTick)
        // println("Vehicle IDs: ${result.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result)}")
        assertEquals(1, result.size)
    }

    /**
     * TODO same size but higher id is correct
     *
     */
    @Test
    fun testCollection5() {
        val vehicleList = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(0, 123, 6, 1), 300),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(2, 123, 6, 1), 600)
        )

        val fireStation: FireStation = BaseFactory().createFireStation(
            123,
            vertex,
            30
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 1, 1), "", "")

        val result = fireStation.collectVehicles(emergency, currentTick)
        // println("Vehicle IDs: ${result.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result)}")
        assertEquals(2, result.size)
    }

    /**
     * TODO no vehicle there at all
     *
     */
    @Test
    fun testCollection6() {
        val fireStation: FireStation = BaseFactory().createFireStation(
            123,
            vertex,
            30
        ).apply {
            assignedVehicles = mutableListOf()
        }

        val emergency = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 1, 1), "", "")

        val result = fireStation.collectVehicles(emergency, currentTick)
        // println("Vehicle IDs: ${result.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result)}")
        assertEquals(0, result.size)
    }

    /**
     * TODO
     *
     */
    @Test
    fun testCollection7() {
        val vehicleList = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(0, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(2, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(3, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(4, 123, 6, 1), 1200),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(5, 123, 6, 1), 600),
            VehicleFireFactory().createFireTruckLadder(VehicleIntParameters(6, 123, 6, 1), 40),
            VehicleFireFactory().createFireTruckLadder(VehicleIntParameters(7, 123, 6, 1), 30),
            VehicleFireFactory().createFirefigherTransporter(VehicleIntParameters(8, 123, 6, 1))
        )

        val fireStation: FireStation = BaseFactory().createFireStation(
            123,
            vertex,
            60
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = FireEmergencyFactory().createFireLevelThree(listOf(1, 1, 1, 1), "", "")

        val result = fireStation.collectVehicles(emergency, currentTick)
        // println("Vehicle IDs: ${result.map { it.vehicleId }}")
        // println("Total Water: ${totalWater(result)}")
        assertEquals(7, result.size)
    }

    @Test
    fun testCollection8() {
        val vehicleList = mutableListOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(5, 123, 5, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(2, 123, 5, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(3, 123, 5, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 123, 5, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(4, 123, 5, 1), 600),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(0, 123, 5, 1), 600),
            VehicleFireFactory().createFireTruckLadder(VehicleIntParameters(6, 123, 5, 1), 40),
            VehicleFireFactory().createFireTruckLadder(VehicleIntParameters(7, 123, 5, 1), 40),
        )

        val fireStation: FireStation = BaseFactory().createFireStation(
            123,
            vertex,
            100
        ).apply {
            assignedVehicles = vehicleList
        }

        val emergency = FireEmergencyFactory().createFireLevelTwo(listOf(1, 1, 1, 1), "", "")

        val result = fireStation.collectVehicles(emergency, currentTick)
        assertEquals(4, result.size)
    }
}
*/
