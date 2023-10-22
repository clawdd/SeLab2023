package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestA : SystemTest() {
    override val name = "BrokenMapTestA"

    override val map = "mapFiles/invalidMaps/county_road_name_not_county_name.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map! county_road_name_not_county_name.dot
        assertNextLine("Initialization Info: county_road_name_not_county_name.dot invalid")
        assertEnd()
    }
}
