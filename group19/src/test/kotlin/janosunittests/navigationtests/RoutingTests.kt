package janosunittests.navigationtests

import de.unisaarland.cs.se.selab.base.PoliceStation
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.parser.DotParser
import de.unisaarland.cs.se.selab.vehicle.PoliceVehicle
import kotlin.test.Test
import kotlin.test.assertEquals

class RoutingTests {

    @Test
    fun driveToEmergencyAndBack() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
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
            "villageA",
            "roadBC",
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
        dummyPoliceCar.route.add(dummyPoliceStation.location)
        dummyPoliceCar.vehicleState = VehicleState.AT_BASE

        // assigns dummy car to emergency ; tick 1
        dummyPoliceCar.prepareForEmergency(dummyEmergency)
        // updatePhase of tick1, vehicle should still be at base
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[0])
        // updatePhase of tick2 , vehicle should be at vertex 1
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[1])
        // updatePhase of tick3, vehicle should be at vertex 2
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[2])
        assertEquals(dummyPoliceCar.vehicleState, VehicleState.AT_EMERGENCY)
        // emergency is handled, vehicles is set to return
        dummyPoliceCar.sendBackToBase()
        // updatePhase of tick4, vehicle should be at vertex 1
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[1])
        // updatePhase of tick5, vehicle should be at vertex 0
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[0])
        assertEquals(dummyPoliceCar.vehicleState, VehicleState.AT_BASE)
    }

    @Test
    fun returnRouteBlockedByOneWay() {
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
            "roadBC",
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
        dummyPoliceCar.route.add(dummyPoliceStation.location)
        dummyPoliceCar.vehicleState = VehicleState.AT_BASE
        // assigns dummy car to emergency ; tick 1
        dummyPoliceCar.prepareForEmergency(dummyEmergency)
        // updatePhase of tick1, vehicle should still be at base
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[0])
        // tick 2, vehicle should be at location 0
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[0])
        assertEquals(dummyPoliceCar.distanceToSource, 1)
        assertEquals(dummyPoliceCar.distanceToTarget, 2)
        // tick 3, vehicle should still be at location 0
        dummyPoliceCar.updateVehicleStatus()
        // tick 3, vehicle should be at location 1
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[1])
        // tick 4, tick 5, tick 6, tick 7
        dummyPoliceCar.updateVehicleStatus()
        dummyPoliceCar.updateVehicleStatus()
        dummyPoliceCar.updateVehicleStatus()
        dummyPoliceCar.updateVehicleStatus()
        // vehicle should be at EM now
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[2])
        // tick 8, calculate return route
        dummyPoliceCar.sendBackToBase()
        // check that return route equals [2,3,4,0]
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[2])
        assertEquals(dummyPoliceCar.route, listOf(Map.vertices[2], Map.vertices[3], Map.vertices[4], Map.vertices[0]))
        // 15 ticks elapse
        repeat(15) {
            dummyPoliceCar.updateVehicleStatus()
        }
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[0])
        assertEquals(dummyPoliceCar.vehicleState, VehicleState.AT_BASE)
    }

    @Test
    fun testBaseVertexEMOccurs() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
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
            "villageA",
            "roadOA",
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
        dummyPoliceCar.route.add(Map.vertices[0])
        // assigns dummy car to emergency ; tick 1 elapses, vehicle should still be at base
        dummyPoliceCar.prepareForEmergency(dummyEmergency)
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[0])
        assertEquals(dummyPoliceCar.distanceToTarget, 0)
        // tick 2 elapses
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[0])
        assertEquals(dummyPoliceCar.vehicleState, VehicleState.AT_EMERGENCY)
    }
}
