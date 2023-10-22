package janosunittests.navigationtests

import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.parser.DotParser
import kotlin.test.Test
import kotlin.test.assertTrue

class DijkstraTests {

    @Test
    fun correctDistanceAllPathsSameLength() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps/" +
                    "dijkstraTestScenarios/dijkstra_same_distance_path.dot"
            )
        dotParser.parseMap()

        val dijkstraInf = Navigation.dijkstraAlgorithm(Map.vertices[0], Map.vertices[7], Map.adjacencyMatrix)
        val distance = Navigation.calculateDistance(dijkstraInf)

        assertTrue { distance == 30.0 }
    }

    @Test
    fun correctRouteAllPathsSameLength() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps/" +
                    "dijkstraTestScenarios/dijkstra_same_distance_path.dot"
            )
        dotParser.parseMap()

        val dijkstraInf = Navigation.dijkstraAlgorithm(Map.vertices[0], Map.vertices[7], Map.adjacencyMatrix)
        val route = Navigation.calculateRoute(Map.vertices[0], Map.vertices[7], dijkstraInf)

        assertTrue { route == listOf(Map.vertices[0], Map.vertices[1], Map.vertices[4], Map.vertices[7]) }
    }

    @Test
    fun correctDistanceSpecExample() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/spec_example.dot"
            )
        dotParser.parseMap()

        val dijkstraInf = Navigation.dijkstraAlgorithm(Map.vertices[1], Map.vertices[5], Map.adjacencyMatrix)
        val distance = Navigation.calculateDistance(dijkstraInf)

        assertTrue { distance == 60.0 }
    }

    @Test
    fun correctPathSpecExample() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/spec_example.dot"
            )
        dotParser.parseMap()

        val dijkstraInf = Navigation.dijkstraAlgorithm(Map.vertices[1], Map.vertices[5], Map.adjacencyMatrix)
        val route = Navigation.calculateRoute(Map.vertices[1], Map.vertices[5], dijkstraInf)

        assertTrue {
            route == listOf(
                Map.vertices[1],
                Map.vertices[3],
                Map.vertices[0],
                Map.vertices[4],
                Map.vertices[5]
            )
        }
    }

    @Test
    fun correctDistanceComplicated() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
            )
        dotParser.parseMap()

        val dijkstraInf = Navigation.dijkstraAlgorithm(Map.vertices[0], Map.vertices[12], Map.adjacencyMatrix)
        val distance = Navigation.calculateDistance(dijkstraInf)

        assertTrue { distance == 80.0 }
    }

    @Test
    fun correctRouteComplicated() {
        val dotParser: DotParser =
            DotParser(
                "src/systemtest/resources/mapFiles/navigationTestMaps" +
                    "/dijkstraTestScenarios/more_complicated.dot"
            )
        dotParser.parseMap()

        val dijkstraInf = Navigation.dijkstraAlgorithm(Map.vertices[12], Map.vertices[10], Map.adjacencyMatrix)
        val route = Navigation.calculateRoute(Map.vertices[12], Map.vertices[10], dijkstraInf)

        assertTrue { route == listOf(Map.vertices[12], Map.vertices[9], Map.vertices[10]) }
    }
}
