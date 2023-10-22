package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

const val TENTICK = 10

/**
 * TODO
 *
 */
class DisconnectedRoad : SystemTest() {

    override val name = "DisconnectedRoad"

    override val map = "mapFiles/disconnected_road_map.dot"
    override val assets = "assetsJsons/standard_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = TENTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: disconnected_road_map.dot invalid")
        assertEnd()
    }
}
