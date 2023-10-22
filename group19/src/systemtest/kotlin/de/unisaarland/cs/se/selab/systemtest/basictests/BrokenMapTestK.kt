package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestK : SystemTest() {
    override val name = "BrokenMapTestK"

    override val map = "mapFiles/invalidMaps/tunnel_larger_three.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map: tunnel_larger_three.dot
        assertNextLine("Initialization Info: tunnel_larger_three.dot invalid")
        assertEnd()
    }
}
