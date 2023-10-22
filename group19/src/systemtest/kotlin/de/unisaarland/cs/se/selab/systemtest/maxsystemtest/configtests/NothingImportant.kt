package de.unisaarland.cs.se.selab.systemtest.maxsystemtest.configtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

/**
 * TODO no main street
 *
 */
class NothingImportant : SystemTest() {
    override val name = "NothingImportant"

    override val map = "mapFiles/nothing_important_map.dot"
    override val assets = "assetsJsons/nothing_important_assets.json"
    override val scenario = "scenarioJsons/nothing_important_scenario.json"

    override val maxTicks = 0
    override suspend fun run() {
        assertNextLine("Initialization Info: nothing_important_map.dot invalid")
        assertEnd()
    }
}
