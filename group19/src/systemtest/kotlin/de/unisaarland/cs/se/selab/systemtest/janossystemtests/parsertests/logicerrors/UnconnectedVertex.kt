package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.logicerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class UnconnectedVertex : SystemTest() {
    override val name = "UnconnectedVertex"

    override val map = "mapFiles/invalidMaps/unconnected_vertex.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: unconnected_vertex.dot invalid")
        assertEnd()
    }
}
