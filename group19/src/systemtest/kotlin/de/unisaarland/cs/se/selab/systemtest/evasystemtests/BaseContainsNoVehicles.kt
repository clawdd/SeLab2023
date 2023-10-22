package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val BCNVMAXTICK = 10

/**
 * TODO
 *
 */
class BaseContainsNoVehicles : SystemTest() {

    override val name = "BaseContainsNoVehicles"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/base_contains_no_vehicles_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = BCNVMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: base_contains_no_vehicles_assets.json invalid")
        assertEnd()
    }
}
