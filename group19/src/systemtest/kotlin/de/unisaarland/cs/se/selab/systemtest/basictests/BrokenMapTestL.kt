package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestL : SystemTest() {
    override val name = "BrokenMapTestL"

    override val map = "mapFiles/invalidMaps/unconnected_vertex.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map: unconnected_vertex.dot
        assertNextLine("Initialization Info: unconnected_vertex.dot invalid")
        assertEnd()
    }
}
