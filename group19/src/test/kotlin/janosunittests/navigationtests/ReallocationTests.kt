package janosunittests.navigationtests

import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.parser.DotParser
import de.unisaarland.cs.se.selab.vehicle.MedicalVehicle
import kotlin.test.Test
import kotlin.test.assertEquals

class ReallocationTests {

    @Test
    fun reallocateOnVertex() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
            )
        dotParser.parseMap()
        val dummyEmergency: Emergency = Emergency(
            45001,
            "villageA",
            "roadDG",
            0,
            0,
            0
        )
        val dummyRealloc: Emergency = Emergency(
            45001,
            "villageB",
            "roadIL",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.MEDICAL
        val dummyAmbulance: MedicalVehicle = MedicalVehicle(
            80001,
            50001,
            VehicleType.AMBULANCE,
            3,
            2
        )
        dummyAmbulance.route.add(Map.vertices[5])
        dummyAmbulance.vehicleState = VehicleState.AT_BASE
        // tick 1, assign ambulance to emergency
        dummyAmbulance.prepareForEmergency(dummyEmergency)
        // tick 2, set to en route
        dummyAmbulance.updateVehicleStatus()
        // tick 4, arrive at 6
        dummyAmbulance.updateVehicleStatus()
        // tick 3, realloc ambulance to realloc EM
        dummyAmbulance.realloc(dummyRealloc)
        assertEquals(
            dummyAmbulance.route,
            listOf(
                Map.vertices[6],
                Map.vertices[2],
                Map.vertices[3],
                Map.vertices[4],
                Map.vertices[11],
                Map.vertices[10],
                Map.vertices[9],
            )
        )
        // tick 3 - 12
        repeat(10) {
            dummyAmbulance.updateVehicleStatus()
        }
        assertEquals(dummyAmbulance.getCurrentLocation(), Map.vertices[9])
    }

    @Test
    fun reallocateOnOneWay() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps/one_way_blocks_return.dot"
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
        val dummyRealloc: Emergency = Emergency(
            45001,
            "exampleVillage",
            "roadDO",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.MEDICAL
        val dummyAmbulance: MedicalVehicle = MedicalVehicle(
            80001,
            50001,
            VehicleType.AMBULANCE,
            3,
            2
        )
        dummyAmbulance.route.add(Map.vertices[1])
        dummyAmbulance.vehicleState = VehicleState.AT_BASE
        // tick 1, assign ambulance to emergency
        dummyAmbulance.prepareForEmergency(dummyEmergency)
        // tick 2, set to en route
        dummyAmbulance.updateVehicleStatus()
        // tick 4, on route to 2
        dummyAmbulance.updateVehicleStatus()
        // tick 3, realloc ambulance to realloc EM
        dummyAmbulance.realloc(dummyRealloc)
        assertEquals(
            dummyAmbulance.route,
            listOf(
                Map.vertices[1],
                Map.vertices[2],
                Map.vertices[3],
                Map.vertices[4],
            )
        )
    }

    @Test
    fun reallocateTurnOnRoute() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
            )
        dotParser.parseMap()
        val dummyEmergency: Emergency = Emergency(
            45001,
            "villageA",
            "roadEF",
            0,
            0,
            0
        )
        val dummyRealloc: Emergency = Emergency(
            45001,
            "villageC",
            "roadOhP",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.MEDICAL
        val dummyAmbulance: MedicalVehicle = MedicalVehicle(
            80001,
            50001,
            VehicleType.AMBULANCE,
            3,
            2
        )
        dummyAmbulance.route.add(Map.vertices[13])
        dummyAmbulance.vehicleState = VehicleState.AT_BASE
        // tick 1, assign ambulance to emergency
        dummyAmbulance.prepareForEmergency(dummyEmergency)
        // tick 2, set to en route
        dummyAmbulance.updateVehicleStatus()
        // tick 4, tick 5, tick 6, on road to vertex 0
        dummyAmbulance.updateVehicleStatus()
        dummyAmbulance.updateVehicleStatus()
        dummyAmbulance.updateVehicleStatus()
        // tick 3, realloc ambulance to realloc EM
        dummyAmbulance.realloc(dummyRealloc)
        assertEquals(
            dummyAmbulance.route,
            listOf(
                Map.vertices[0],
                Map.vertices[13],
                Map.vertices[15]
            )
        )
        // tick 3 - 12
        repeat(4) {
            dummyAmbulance.updateVehicleStatus()
        }
        assertEquals(dummyAmbulance.getCurrentLocation(), Map.vertices[13]) // 15
    }

    @Test
    fun reallocateOnVertexUTurn() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
            )
        dotParser.parseMap()
        val dummyEmergency: Emergency = Emergency(
            45001,
            "villageA",
            "roadCH",
            0,
            0,
            0
        )
        val dummyRealloc: Emergency = Emergency(
            45001,
            "villageB",
            "roadIJ",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.MEDICAL
        val dummyAmbulance: MedicalVehicle = MedicalVehicle(
            80001,
            50001,
            VehicleType.AMBULANCE,
            3,
            2
        )
        dummyAmbulance.route.add(Map.vertices[11])
        dummyAmbulance.vehicleState = VehicleState.AT_BASE
        // tick 1, assign ambulance to emergency
        dummyAmbulance.prepareForEmergency(dummyEmergency)
        // tick 2, set to en route
        dummyAmbulance.updateVehicleStatus()
        // tick 4, tick 5, tick 6, arrive at 4
        dummyAmbulance.updateVehicleStatus()
        dummyAmbulance.updateVehicleStatus()
        dummyAmbulance.updateVehicleStatus()
        // tick 3, realloc ambulance to realloc EM
        dummyAmbulance.realloc(dummyRealloc)
        assertEquals(dummyAmbulance.getCurrentLocation(), Map.vertices[4])
        assertEquals(dummyAmbulance.distanceToSource, 0)
        assertEquals(dummyAmbulance.distanceToTarget, 3)
        assertEquals(
            dummyAmbulance.route,
            listOf(
                Map.vertices[4],
                Map.vertices[11],
                Map.vertices[10],
            )
        )
        // ticks.elapse
        repeat(4) {
            dummyAmbulance.updateVehicleStatus()
        }
        assertEquals(dummyAmbulance.getCurrentLocation(), Map.vertices[11])
    }
}
