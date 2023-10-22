package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.syntaxerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class VerticesRoadsAlternately : SystemTest() {
    override val name = "VerticesRoadsAlternately"

    override val map = "mapFiles/invalidMaps/vertices_roads_alternately.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: vertices_roads_alternately.dot invalid")
        assertEnd()
    }
}
