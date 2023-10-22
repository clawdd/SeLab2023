package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

/**
 * TODO
 *
 */
class TwoVehicleSameID : SystemTest() {

    override val name = "TwoVehicleSameID"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/two_vehicle_same_id_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = TENTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: two_vehicle_same_id_assets.json invalid")
        assertEnd()
    }
}
