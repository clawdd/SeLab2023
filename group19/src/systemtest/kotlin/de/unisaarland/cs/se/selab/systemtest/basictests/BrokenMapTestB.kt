package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestB : SystemTest() {
    override val name = "BrokenMapTestB"

    override val map = "mapFiles/invalidMaps/direct_connection_other_village.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map: direct_connection_other_village.dot
        assertNextLine("Initialization Info: direct_connection_other_village.dot invalid")
        assertEnd()
    }
}
