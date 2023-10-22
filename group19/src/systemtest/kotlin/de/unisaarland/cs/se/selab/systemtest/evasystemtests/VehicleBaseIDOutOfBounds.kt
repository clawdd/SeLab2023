package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val VBIDOOBMAXTICK = 10

/**
 * TODO
 *
 */
class VehicleBaseIDOutOfBounds : SystemTest() {

    override val name = "VehicleBaseIDOutOfBounds"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/vehicle_baseid_out_of_bounds_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = VBIDOOBMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: vehicle_base_id_out_of_bounds_assets.json invalid")
        assertEnd()
    }
}
