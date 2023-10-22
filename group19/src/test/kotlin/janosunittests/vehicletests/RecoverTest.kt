/* package janosunittests.vehicletests

import de.unisaarland.cs.se.selab.base.FireStation
import de.unisaarland.cs.se.selab.base.PoliceStation
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.parser.DotParser
import de.unisaarland.cs.se.selab.vehicle.FireVehicle
import de.unisaarland.cs.se.selab.vehicle.PoliceVehicle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RecoverTest {
    @Test
    fun recoverOnTransporter() {
        val fireTransporter = FireVehicle(0, 0, VehicleType.FIREFIGHTER_TRANSPORTER, 2, 2)
        fireTransporter.recover()
        assertFalse(fireTransporter.recovering)
    }

    @Test
    fun recoverWaterOneTick() {
        val waterTruck = FireVehicle(80001, 50001, VehicleType.FIRE_TRUCK_WATER, 3, 2)
        waterTruck.waterCapacity = 600
        waterTruck.waterLevel = 200
        waterTruck.recover()
        assertEquals(waterTruck.waterLevel, 500)
        waterTruck.recover()
        assertEquals(waterTruck.waterLevel, 600)
    }

    @Test
    fun unloadCriminals() {
        val policeCar1 = PoliceVehicle(1, 0, VehicleType.POLICE_CAR, 0, 0)
        policeCar1.criminalCapacity = 2
        policeCar1.criminalsOnBoard = 0

        policeCar1.vehicleState = VehicleState.AT_EMERGENCY
        policeCar1.loadCriminals(2)
        assertEquals(policeCar1.criminalsOnBoard, 2)
        assertTrue(policeCar1.recovering)

        // elapse
        policeCar1.vehicleState = VehicleState.AT_BASE
        policeCar1.recover()
        assertTrue(policeCar1.recovering)
        assertEquals(policeCar1.criminalsOnBoard, 2)
        assertEquals(policeCar1.unloadTimer, 2)

        // elapse
        policeCar1.recover()
        assertEquals(policeCar1.criminalsOnBoard, 2)
        assertTrue(policeCar1.recovering)
        assertEquals(policeCar1.unloadTimer, 1)

        // elapse
        policeCar1.recover()
        assertEquals(policeCar1.criminalsOnBoard, 0)
        assertEquals(policeCar1.unloadTimer, -1)
        assertFalse(policeCar1.recovering)
    }

    @Test
    fun returnStaffAndK9ToBase() {
        val policeStation1 = PoliceStation(
            0,
            Vertex(0),
            0,
            mutableListOf(),
            mutableListOf(),
            0,
            BaseType.POLICE_STATION
        )
        Navigation.initNavi(listOf(policeStation1))
        val policeCar1 = PoliceVehicle(1, 0, VehicleType.K9_POLICE_CAR, 0, 2)
        policeCar1.dog = true

        policeCar1.vehicleState = VehicleState.AT_BASE
        policeCar1.recover()
        assertFalse(policeCar1.recovering)

        policeCar1.returnAssets()
        assertFalse(policeCar1.dog)
        assertEquals(policeStation1.availableK9s, 1)
        assertEquals(policeStation1.availableStaff, 2)
    }

    /**
     * Tests if refilling of a completely emptied fire truck works
     */
    @Test
    fun testFullyRecoverFireTruck() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps/one_way_blocks_return.dot"
            )
        dotParser.parseMap()
        val dummyFireStation: FireStation = FireStation(
            50001,
            Map.vertices[2],
            10,
            mutableListOf(),
            mutableListOf(),
            BaseType.FIRE_STATION
        )
        val dummyEmergency: Emergency = Emergency(
            45001,
            "exampleVillage",
            "road40",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.FIRE
        Navigation.initNavi(listOf(dummyFireStation))
        val dummyFireTruck: FireVehicle = FireVehicle(
            80001,
            50001,
            VehicleType.FIRE_TRUCK_WATER,
            3,
            2
        )
        dummyFireTruck.waterCapacity = 600
        dummyFireTruck.waterLevel = 600
        dummyFireTruck.currentLocation = dummyFireStation.location
        dummyFireTruck.vehicleState = VehicleState.AT_BASE
        dummyFireTruck.prepareForEmergency(dummyEmergency)
        dummyFireTruck.updateVehicleStatus()
        // tick 2 - 12
        repeat(10) {
            dummyFireTruck.updateVehicleStatus()
        }
        // consume all water at emergency and return
        dummyFireTruck.useWater(600)
        assertTrue(dummyFireTruck.recovering)
        dummyFireTruck.sendBackToBase()
        repeat(10) {
            dummyFireTruck.updateVehicleStatus() // tick 13 - 23
        }
        assertEquals(dummyFireTruck.currentLocation, Map.vertices[2])
        assertEquals(dummyFireTruck.vehicleState, VehicleState.AT_BASE)
        assertTrue(dummyFireTruck.recovering)
        assertEquals(dummyFireTruck.waterLevel, 0)
        // tick 24, updateVehicle should increase water capacity by 300 liters
        dummyFireTruck.updateVehicleStatus()
        assertEquals(dummyFireTruck.waterLevel, 300)
        // tick 25, updateVehicle should inncrease water capacity by 300 liters and completely fill truck
        dummyFireTruck.updateVehicleStatus()
        assertEquals(dummyFireTruck.waterLevel, dummyFireTruck.waterCapacity)
        assertFalse(dummyFireTruck.recovering)
    }

    @Test
    fun testNotRecoveringIfNotEmpty() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps/one_way_blocks_return.dot"
            )
        dotParser.parseMap()
        val dummyFireStation: FireStation = FireStation(
            50001,
            Map.vertices[2],
            10,
            mutableListOf(),
            mutableListOf(),
            BaseType.FIRE_STATION
        )
        val dummyEmergency: Emergency = Emergency(
            45001,
            "exampleVillage",
            "road40",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.FIRE
        Navigation.initNavi(listOf(dummyFireStation))
        val dummyFireTruck: FireVehicle = FireVehicle(
            80001,
            50001,
            VehicleType.FIRE_TRUCK_WATER,
            3,
            2
        )
        dummyFireTruck.waterCapacity = 600
        dummyFireTruck.waterLevel = 600
        dummyFireTruck.currentLocation = dummyFireStation.location
        dummyFireTruck.vehicleState = VehicleState.AT_BASE
        dummyFireTruck.prepareForEmergency(dummyEmergency)
        dummyFireTruck.updateVehicleStatus()
        // tick 2 - 12
        repeat(10) {
            dummyFireTruck.updateVehicleStatus()
        }
        // consume all water at emergency and return
        dummyFireTruck.useWater(500)
        assertFalse(dummyFireTruck.recovering)
    }
}
*/
