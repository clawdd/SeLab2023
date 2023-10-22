package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val BSIZMAXTICK = 10

/**
 * TODO
 *
 */
class BaseStaffIsZero : SystemTest() {

    override val name = "BaseStaffIsZero"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/base_staff_is_zero_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = BSIZMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: base_staff_is_zero_assets.json invalid")
        assertEnd()
    }
}
