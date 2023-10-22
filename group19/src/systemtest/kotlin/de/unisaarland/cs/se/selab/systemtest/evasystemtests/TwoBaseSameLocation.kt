package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val TBSLMAXTICK = 10

/**
 * TODO
 *
 */
class TwoBaseSameLocation : SystemTest() {

    override val name = "TwoBaseSameLocation"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/two_base_same_location_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = TBSLMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: two_base_same_location_assets.json invalid")
        assertEnd()
    }
}
