package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestF : SystemTest() {
    override val name = "BrokenMapTestF"

    override val map = "mapFiles/invalidMaps/new_road_between_two_verties.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map: new_road_between_two_verties.dot
        assertNextLine("Initialization Info: new_road_between_two_verties.dot invalid")
        assertEnd()
    }
}
