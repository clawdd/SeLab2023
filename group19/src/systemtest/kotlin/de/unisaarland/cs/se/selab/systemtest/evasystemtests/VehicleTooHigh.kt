package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

/**
 * TODO
 *
 */
class VehicleTooHigh : SystemTest() {

    override val name = "VehicleTooHigh"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/vehicle_too_high_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = TENTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: vehicle_too_high_assets.json invalid")
        assertEnd()
    }
}
