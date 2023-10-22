package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestM : SystemTest() {
    override val name = "BrokenMapTestM"

    override val map = "mapFiles/invalidMaps/vertex_to_itself.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map: vertex_to_itself.dot
        assertNextLine("Initialization Info: vertex_to_itself.dot invalid")
        assertEnd()
    }
}
