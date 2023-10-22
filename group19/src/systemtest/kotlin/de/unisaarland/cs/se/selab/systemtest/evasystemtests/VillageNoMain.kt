package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

/**
 * TODO
 *
 */
class VillageNoMain : SystemTest() {

    override val name = "VillageNoMain"

    override val map = "mapFiles/village_no_main_map.dot"
    override val assets = "assetsJsons/standard_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = TENTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: village_no_main_map.dot invalid")
        assertEnd()
    }
}
