package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.syntaxerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class RoadsBeforeVertices : SystemTest() {
    override val name = "RoadsBeforeVertices"

    override val map = "mapFiles/invalidMaps/roads_before_vertices.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: roads_before_vertices.dot invalid")
        assertEnd()
    }
}
