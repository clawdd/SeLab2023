package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val VSIZMAXTICK = 10

/**
 * TODO
 *
 */
class VehicleStaffIsZero : SystemTest() {

    override val name = "VehicleStaffIsZero"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/vehicle_staff_is_zero_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = VSIZMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: vehicle_staff_is_zero_assets.json invalid")
        assertEnd()
    }
}
