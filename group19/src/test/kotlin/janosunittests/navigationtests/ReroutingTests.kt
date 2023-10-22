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

class ReroutingTests {

    @Test
    fun rerouteOldRouteUnavailable() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
            )
        dotParser.parseMap()
        val dummyPoliceStation: PoliceStation = PoliceStation(
            50001,
            Map.vertices[11],
            10,
            mutableListOf(),
            mutableListOf(),
            1,
            BaseType.POLICE_STATION
        )
        val dummyEmergency: Emergency = Emergency(
            45001,
            "villageA",
            "roadFH",
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
        // assigns dummy car to emergency and updates vehicle status ; tick 1
        dummyPoliceCar.prepareForEmergency(dummyEmergency)
        dummyPoliceCar.updateVehicleStatus()
        // tick2, drive to emergency
        dummyPoliceCar.updateVehicleStatus()
        // tick3, drive to emergency, event occurs that disables road34
        dummyPoliceCar.updateVehicleStatus()
        Map.findRoadByVertex(Map.vertices[3], Map.vertices[4]).weight = Double.MAX_VALUE
        dummyPoliceCar.reroute()
        assertEquals(dummyPoliceCar.distanceToTarget, 1)
        assertEquals(dummyPoliceCar.route, listOf(Map.vertices[11], Map.vertices[4], Map.vertices[7], Map.vertices[8]))
        // tick 4, arrives at vertex 4
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[4])
        // tick 5 - 11, drives to vertex 8
        repeat(6) {
            dummyPoliceCar.updateVehicleStatus()
        }
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[8])
    }

    @Test
    fun rerouteOldRouteLonger() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
            )
        dotParser.parseMap()
        val dummyEmergency: Emergency = Emergency(
            45001,
            "villageC",
            "roadMOh",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.CRIME
        val dummyPoliceCar: PoliceVehicle = PoliceVehicle(
            80001,
            50001,
            VehicleType.POLICE_CAR,
            3,
            2
        )
        dummyPoliceCar.route.add(Map.vertices[11])
        dummyPoliceCar.vehicleState = VehicleState.AT_BASE
        // assigns dummy car to emergency and updates vehicle status ; tick 1
        dummyPoliceCar.prepareForEmergency(dummyEmergency)
        assertEquals(
            dummyPoliceCar.route,
            listOf(
                Map.vertices[11],
                Map.vertices[10],
                Map.vertices[9],
                Map.vertices[14],
                Map.vertices[15]
            )
        )
        dummyPoliceCar.updateVehicleStatus()
        // tick2, drive to vertex10
        dummyPoliceCar.updateVehicleStatus()
        // tick3, drive to vertex9, event occurs that triples weight of road1415
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[9])
        Map.findRoadByVertex(Map.vertices[14], Map.vertices[15]).weight *= 3
        dummyPoliceCar.reroute()
        assertEquals(
            dummyPoliceCar.route,
            listOf(
                Map.vertices[14],
                Map.vertices[16],
                Map.vertices[15]
            )
        )
        repeat(9) {
            dummyPoliceCar.updateVehicleStatus()
        }
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[15])
    }

    @Test
    fun rerouteRerouteLonger() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
            )
        dotParser.parseMap()
        val dummyEmergency: Emergency = Emergency(
            45001,
            "villageA",
            "roadFH",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.CRIME
        val dummyPoliceCar: PoliceVehicle = PoliceVehicle(
            80001,
            50001,
            VehicleType.POLICE_CAR,
            3,
            2
        )
        dummyPoliceCar.route.add(Map.vertices[4])
        dummyPoliceCar.vehicleState = VehicleState.AT_BASE
        // assigns dummy car to emergency and updates vehicle status ; tick 1
        dummyPoliceCar.prepareForEmergency(dummyEmergency)
        dummyPoliceCar.updateVehicleStatus()
        // tick2, drive to vertex3
        dummyPoliceCar.updateVehicleStatus()
        // tick3, drive to vertex8, event occurs that triples weight of road23
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[8])
        Map.findRoadByVertex(Map.vertices[2], Map.vertices[3]).weight *= 3
        dummyPoliceCar.reroute()
        assertEquals(
            dummyPoliceCar.route,
            listOf(
                Map.vertices[8]
            )
        )
        repeat(2) {
            dummyPoliceCar.updateVehicleStatus()
        }
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[8])
    }

    @Test
    fun rerouteRerouteUTurnNoOneWay() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps/one_way_blocks_return.dot"
            )
        dotParser.parseMap()
        val dummyEmergency: Emergency = Emergency(
            45001,
            "exampleVillage",
            "roadAB",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.CRIME
        val dummyPoliceCar: PoliceVehicle = PoliceVehicle(
            80001,
            50001,
            VehicleType.POLICE_CAR,
            3,
            2
        )
        dummyPoliceCar.route.add(Map.vertices[4])
        dummyPoliceCar.vehicleState = VehicleState.AT_BASE
        // assigns dummy car to emergency and updates vehicle status ; tick 1
        dummyPoliceCar.prepareForEmergency(dummyEmergency)
        assertEquals(dummyPoliceCar.route, listOf(Map.vertices[4], Map.vertices[0], Map.vertices[1]))
        dummyPoliceCar.updateVehicleStatus()
        // tick2, one tick into road40
        dummyPoliceCar.updateVehicleStatus()
        // tick3, two ticks into road40
        dummyPoliceCar.updateVehicleStatus()
        // tick4, three ticks into road40, event occurs that increases weight of road01 tenfold
        dummyPoliceCar.updateVehicleStatus()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[4])
        Map.findRoadByVertex(Map.vertices[0], Map.vertices[1]).weight *= 10
        dummyPoliceCar.reroute()
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[0])
        assertEquals(dummyPoliceCar.route, listOf(Map.vertices[0], Map.vertices[4], Map.vertices[1]))
        repeat(11) {
            dummyPoliceCar.updateVehicleStatus()
        }
        assertEquals(dummyPoliceCar.getCurrentLocation(), Map.vertices[1])
    }

    @Test
    fun rerouteRerouteUTurnOnOneWay() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps/uturn_on_oneway.dot"
            )
        dotParser.parseMap()
        val dummyEmergency: Emergency = Emergency(
            45001,
            "exampleVillage",
            "roadBC",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.CRIME
        val dummyPoliceCar: PoliceVehicle = PoliceVehicle(
            80001,
            50001,
            VehicleType.POLICE_CAR,
            3,
            2
        )
        dummyPoliceCar.route.add(Map.vertices[0])
        dummyPoliceCar.vehicleState = VehicleState.AT_BASE
        // assigns dummy car to emergency and updates vehicle status ; tick 1
        dummyPoliceCar.prepareForEmergency(dummyEmergency)
        assertEquals(dummyPoliceCar.route, listOf(Map.vertices[0], Map.vertices[1], Map.vertices[3]))
        dummyPoliceCar.updateVehicleStatus()
        // move one tick into road01, then event triggers that increases weight of road13 sevenfold
        dummyPoliceCar.updateVehicleStatus()
        Map.findRoadByVertex(Map.vertices[1], Map.vertices[3]).weight *= 7
        dummyPoliceCar.reroute()
        assertEquals(dummyPoliceCar.route, listOf(Map.vertices[0], Map.vertices[1], Map.vertices[3]))
    }
}
