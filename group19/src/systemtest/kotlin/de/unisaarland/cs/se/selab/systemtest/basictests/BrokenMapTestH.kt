package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestH : SystemTest() {
    override val name = "BrokenMapTestH"

    override val map = "mapFiles/invalidMaps/no_side_street_in_village.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map: no_side_street_in_village.dot
        assertNextLine("Initialization Info: no_side_street_in_village.dot invalid")
        assertEnd()
    }
}
