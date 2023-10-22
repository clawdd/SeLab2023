package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestG : SystemTest() {
    override val name = "BrokenMapTestG"

    override val map = "mapFiles/invalidMaps/no_main_street_on_map.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map: no_main_street_on_map.dot
        assertNextLine("Initialization Info: no_main_street_on_map.dot invalid")
        assertEnd()
    }
}
