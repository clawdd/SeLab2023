package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val NOOEBMAXTICK = 10

/**
 * TODO
 *
 */
class NotOneOfEveryBase : SystemTest() {

    override val name = "NotOneOfEveryBase"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/not_one_of_every_base_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = NOOEBMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: not_one_of_every_base_assets.json invalid")
        assertEnd()
    }
}
