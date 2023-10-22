package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestI : SystemTest() {
    override val name = "BrokenMapTestI"

    override val map = "mapFiles/invalidMaps/road_height_below_one.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map: road_height_below_one.dot
        assertNextLine("Initialization Info: road_height_below_one.dot invalid")
        assertEnd()
    }
}
