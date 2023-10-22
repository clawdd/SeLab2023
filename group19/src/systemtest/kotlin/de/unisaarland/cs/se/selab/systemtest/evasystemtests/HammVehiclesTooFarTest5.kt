package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest
import de.unisaarland.cs.se.selab.systemtest.keensystemtest.KEY_REQUEST_FAILED_ZERO

private const val MAXTICK = 5

class HammVehiclesTooFarTest5 : SystemTest() {
    override val name = "HammVehiclesTooFar5"
    override val map = "mapFiles/hamm_vehicles_too_far_map.dot"
    override val assets = "assetsJsons/hamm_vehicles_too_far_assets.json"
    override val scenario = "scenarioJsons/hamm_vehicles_too_far_scenario.json"
    override val maxTicks = MAXTICK

    override suspend fun run() {
        assertNextLine("Initialization Info: hamm_vehicles_too_far_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: hamm_vehicles_too_far_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: hamm_vehicles_too_far_scenario.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Event Triggered: 0 triggered.")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 0 assigned to 1")
        assertNextLine("Asset Allocation: 0 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Asset Request: 1 sent to 2 for 0.")
        assertNextLine(KEY_REQUEST_FAILED_ZERO)
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Asset Request: 2 sent to 2 for 0.")
        assertNextLine(KEY_REQUEST_FAILED_ZERO)
        assertNextLine("Asset Arrival: 0 arrived at 0.")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Asset Request: 3 sent to 2 for 0.")
        assertNextLine(KEY_REQUEST_FAILED_ZERO)
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Asset Request: 4 sent to 2 for 0.")
        assertNextLine(KEY_REQUEST_FAILED_ZERO)
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 1 received emergencies.")
        assertNextLine("Simulation Statistics: 1 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 0 resolved emergencies.")
        assertEnd()
    }
}
