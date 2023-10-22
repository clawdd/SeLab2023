package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestJ : SystemTest() {
    override val name = "BrokenMapTestJ"

    override val map = "mapFiles/invalidMaps/road_name_in_village_not_unique.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map: road_name_in_village_not_unique.dot
        assertNextLine("Initialization Info: road_name_in_village_not_unique.dot invalid")
        assertEnd()
    }
}
