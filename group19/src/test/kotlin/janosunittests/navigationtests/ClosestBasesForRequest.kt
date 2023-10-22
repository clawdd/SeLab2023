package janosunittests.navigationtests

import de.unisaarland.cs.se.selab.base.PoliceStation
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.parser.DotParser
import kotlin.test.Test
import kotlin.test.assertTrue

class ClosestBasesForRequest {

    @Test
    fun requestBaseChain1() {
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
        Navigation.initNavi(listOf(dummyPoliceStation1, dummyPoliceStation2, dummyPoliceStation3))

        val sortedBases = Navigation.findNearestBasesRequest(dummyPoliceStation3, BaseType.POLICE_STATION)
        assertTrue { sortedBases == listOf(dummyPoliceStation2, dummyPoliceStation1) }
    }
}
