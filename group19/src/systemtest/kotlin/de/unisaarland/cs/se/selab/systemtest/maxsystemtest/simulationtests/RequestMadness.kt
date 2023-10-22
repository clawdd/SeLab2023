package de.unisaarland.cs.se.selab.systemtest.maxsystemtest.simulationtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

const val SIMULATION_TICK_RM = 15

/**
 * TODO check if request assignment is in correct order
 *
 */
class RequestMadness : SystemTest() {

    override val name = "RequestMadness"

    override val map = "mapFiles/request_madness.dot"
    override val assets = "assetsJsons/request_madness_assets.json"
    override val scenario = "scenarioJsons/request_madness_scenario.json"
    override val maxTicks = SIMULATION_TICK_RM

    override suspend fun run() {
        assertNextLine("Initialization Info: request_madness.dot successfully parsed and validated")
        assertNextLine("Initialization Info: request_madness_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: request_madness_scenario.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 0 assigned to 1")
        assertNextLine("Asset Allocation: 2 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 3 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Asset Request: 1 sent to 0 for 0.")
        assertNextLine("Asset Allocation: 0 allocated to 0; 4 ticks to arrive.")
        assertNextLine("Asset Request: 2 sent to 3 for 0.")
        assertNextLine("Asset Allocation: 6 allocated to 0; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 7 allocated to 0; 2 ticks to arrive.")
        assertNextLine("Asset Request: 3 sent to 2 for 0.")
        assertNextLine("Asset Allocation: 4 allocated to 0; 3 ticks to arrive.")
        assertNextLine("Asset Allocation: 5 allocated to 0; 3 ticks to arrive.")
        assertNextLine("Request Failed: 0 failed.")
        assertNextLine("Request Failed: 0 failed.")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Asset Arrival: 2 arrived at 7.")
        assertNextLine("Asset Arrival: 3 arrived at 7.")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Asset Arrival: 6 arrived at 7.")
        assertNextLine("Asset Arrival: 7 arrived at 7.")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Asset Arrival: 4 arrived at 0.")
        assertNextLine("Asset Arrival: 5 arrived at 0.")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Asset Arrival: 0 arrived at 7.")
        assertNextLine("Emergency Handling Start: 0 handling started.")
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Simulation Tick: 7")
        assertNextLine("Simulation Tick: 8")
        assertNextLine("Simulation Tick: 9")
        assertNextLine("Simulation Tick: 10")
        assertNextLine("Emergency Resolved: 0 resolved.")
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 1 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 1 resolved emergencies.")
        assertEnd()
    }
}
