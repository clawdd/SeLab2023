package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BrokenMapTestE : SystemTest() {
    override val name = "BrokenMapTestE"

    override val map = "mapFiles/invalidMaps/negativeIndexVertices.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 1

    override suspend fun run() {
        // broken map: negativeIndexVertices.dot
        assertNextLine("Initialization Info: negativeIndexVertices.dot invalid")
        assertEnd()
    }
}
