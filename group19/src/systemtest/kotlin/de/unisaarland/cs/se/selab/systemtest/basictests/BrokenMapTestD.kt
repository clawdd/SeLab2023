package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestD : SystemTest() {
    override val name = "BrokenMapTestD"

    override val map = "mapFiles/invalidMaps/negative_road_weight.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map: negative_road_weight.dot
        assertNextLine("Initialization Info: negative_road_weight.dot invalid")
        assertEnd()
    }
}
