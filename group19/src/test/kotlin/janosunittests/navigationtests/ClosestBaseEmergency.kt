package janosunittests.navigationtests

import de.unisaarland.cs.se.selab.base.PoliceStation
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.parser.DotParser
import kotlin.test.Test
import kotlin.test.assertTrue

class ClosestBaseEmergency {

    /**
     * In this test, only a single base is on the entire map
     */
    @Test
    fun closestBaseSimple() {
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

        assertTrue { dummyPoliceStation == Navigation.calculateClosestBase(dummyEmergency) }
    }

    @Test
    fun twoBasesSameDistance() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
            )
        dotParser.parseMap()

        val dummyPoliceStation1: PoliceStation = PoliceStation(
            50013,
            Map.vertices[1],
            10,
            mutableListOf(),
            mutableListOf(),
            1,
            BaseType.POLICE_STATION
        )

        val dummyPoliceStation2: PoliceStation = PoliceStation(
            50022,
            Map.vertices[3],
            10,
            mutableListOf(),
            mutableListOf(),
            1,
            BaseType.POLICE_STATION
        )

        val dummyEmergency: Emergency = Emergency(
            45001,
            "villageA",
            "roadEF",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.CRIME
        Navigation.initNavi(listOf(dummyPoliceStation1, dummyPoliceStation2))

        assertTrue { dummyPoliceStation1 == Navigation.calculateClosestBase(dummyEmergency) }
    }

    @Test
    fun threeBasesSameDistance() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
            )
        dotParser.parseMap()

        val dummyPoliceStation1: PoliceStation = PoliceStation(
            50013,
            Map.vertices[1],
            10,
            mutableListOf(),
            mutableListOf(),
            1,
            BaseType.POLICE_STATION
        )

        val dummyPoliceStation2: PoliceStation = PoliceStation(
            50022,
            Map.vertices[3],
            10,
            mutableListOf(),
            mutableListOf(),
            1,
            BaseType.POLICE_STATION
        )

        val dummyPoliceStation3: PoliceStation = PoliceStation(
            50004,
            Map.vertices[7],
            10,
            mutableListOf(),
            mutableListOf(),
            1,
            BaseType.POLICE_STATION
        )

        val dummyEmergency: Emergency = Emergency(
            45001,
            "villageA",
            "roadEF",
            0,
            0,
            0
        )
        dummyEmergency.emergencyType = EmergencyType.CRIME
        Navigation.initNavi(listOf(dummyPoliceStation1, dummyPoliceStation2, dummyPoliceStation3))

        assertTrue { dummyPoliceStation3 == Navigation.calculateClosestBase(dummyEmergency) }
    }
}
