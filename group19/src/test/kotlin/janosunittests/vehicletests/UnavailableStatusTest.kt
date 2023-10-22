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
import de.unisaarland.cs.se.selab.parser.DotParser
import de.unisaarland.cs.se.selab.vehicle.FireVehicle
import de.unisaarland.cs.se.selab.vehicle.PoliceVehicle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UnavailableStatusTest {

    @Test
    fun testVehicleUnavailableOnRoute() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps/one_way_blocks_return.dot"
            )
        dotParser.parseMap()
        val dummyPoliceStation: PoliceStation = PoliceStation(
            50001,
            Map.vertices[0],
            10,
            mutableListOf(),
            mutableListOf(),
            1,
            BaseType.POLICE_STATION
        )
        val dummyEmergency: Emergency = Emergency(
            45001,
            "exampleVillage",
            "road12",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.CRIME
        Navigation.initNavi(listOf(dummyPoliceStation))
        val dummyPoliceCar: PoliceVehicle = PoliceVehicle(
            80001,
            50001,
            VehicleType.POLICE_CAR,
            3,
            2
        )
        dummyPoliceCar.currentLocation = dummyPoliceStation.location
        dummyPoliceCar.vehicleState = VehicleState.AT_BASE
        dummyPoliceCar.prepareForEmergency(dummyEmergency)
        dummyPoliceCar.updateVehicleStatus()
        // tick 2, tick 3, tick 4
        dummyPoliceCar.updateVehicleStatus()
        dummyPoliceCar.setunavailableFor(2, 96)
        dummyPoliceCar.updateVehicleStatus()
        dummyPoliceCar.updateVehicleStatus()
        // emergency handled
        dummyPoliceCar.sendBackToBase()
        // tick 5, tick 6, tick 7
        dummyPoliceCar.updateVehicleStatus()
        dummyPoliceCar.updateVehicleStatus()
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.currentLocation, Map.vertices[0])
        assertEquals(dummyPoliceCar.vehicleState, VehicleState.AT_BASE)
        assertTrue(dummyPoliceCar.unavailability)
        assertEquals(dummyPoliceCar.unavailableTimer, 2)
        // tick 8, unavailable timer should equal 1
        dummyPoliceCar.updateVehicleStatus()
        assertTrue(dummyPoliceCar.unavailability)
        assertEquals(dummyPoliceCar.unavailableTimer, 1)
        // tick 9, vehicle should be available again
        dummyPoliceCar.updateVehicleStatus()
        assertFalse(dummyPoliceCar.unavailability)
    }

    /**
     * Tests if the vehicle first refills even though it has been set unavailable
     */
    @Test
    fun testUnavailableBeforeRecovering() {
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
        dummyFireTruck.setunavailableFor(5, 402)
        // tick 2 - 12
        repeat(10) {
            dummyFireTruck.updateVehicleStatus()
        }
        // consume all water at emergency and return
        dummyFireTruck.useWater(600)
        assertTrue(dummyFireTruck.recovering)
        assertTrue(dummyFireTruck.unavailability)
        dummyFireTruck.sendBackToBase()
        // tick 13 - 23
        repeat(10) {
            dummyFireTruck.updateVehicleStatus()
        }
        // tick 24
        dummyFireTruck.updateVehicleStatus()
        // tick 25, refill ending, unavailable triggered, but not yet started
        dummyFireTruck.updateVehicleStatus()
        assertFalse(dummyFireTruck.recovering)
        assertTrue(dummyFireTruck.unavailability)
        assertEquals(dummyFireTruck.unavailableTimer, 5)
        // tick 26 - 30
        repeat(5) {
            dummyFireTruck.updateVehicleStatus()
        }
        assertFalse(dummyFireTruck.unavailability)
    }

    /**
     * Tests if the vehicle behaves correctly, if it is set to unavailable during recovering
     */
    @Test
    fun testUnavailableDuringRecovering() {
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
        // tick 13 - 23
        repeat(10) {
            dummyFireTruck.updateVehicleStatus()
        }
        // tick 24, refill 1
        dummyFireTruck.updateVehicleStatus()
        dummyFireTruck.setunavailableFor(5, 338)
        // tick 25, refill ending, unavailable triggered, but not yet started
        dummyFireTruck.updateVehicleStatus()
        assertFalse(dummyFireTruck.recovering)
        assertTrue(dummyFireTruck.unavailability)
        assertEquals(dummyFireTruck.unavailableTimer, 5)
        // tick 26 - 30
        repeat(5) {
            dummyFireTruck.updateVehicleStatus()
        }
        assertFalse(dummyFireTruck.unavailability)
    }
}
*/
