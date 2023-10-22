package de.unisaarland.cs.se.selab.systemtest.maxsystemtest.simulationtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

const val SIMULATION_TICK_REALLOC = 12

/**
 * TODO
 *
 */
class ReallocationInBielefeld : SystemTest() {
    override val name = "ReallocationInBielefeld"

    override val map = "mapFiles/bielefeld_map.dot"
    override val assets = "assetsJsons/bielefeld_assets.json"
    override val scenario = "scenarioJsons/bielefeld_scenario.json"

    override val maxTicks = SIMULATION_TICK_REALLOC
    override suspend fun run() {
        assertNextLine("Initialization Info: bielefeld_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: bielefeld_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: bielefeld_scenario.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")

        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 0 assigned to 0")
        assertNextLine("Asset Allocation: 0 allocated to 0; 5 ticks to arrive.")
        assertNextLine("Asset Allocation: 1 allocated to 0; 5 ticks to arrive.")

        assertNextLine("Simulation Tick: 2")

        assertNextLine("Simulation Tick: 3")

        assertNextLine("Simulation Tick: 4")
        assertNextLine("Emergency Assignment: 1 assigned to 0")
        assertNextLine("Asset Allocation: 2 allocated to 1; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 3 allocated to 1; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 4 allocated to 1; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 5 allocated to 1; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 6 allocated to 1; 2 ticks to arrive.")
        assertNextLine("Asset Reallocation: 1 reallocated to 1.")
        assertNextLine("Asset Request: 1 sent to 1 for 1.")
        assertNextLine("Asset Allocation: 7 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Arrival: 1 arrived at 2.")

        assertNextLine("Simulation Tick: 5")
        assertNextLine("Asset Arrival: 7 arrived at 2.")
        assertNextLine("Event Triggered: 0 triggered.")

        assertNextLine("Simulation Tick: 6")
        assertNextLine("Emergency Assignment: 2 assigned to 2")
        assertNextLine("Asset Arrival: 0 arrived at 1.")
        assertNextLine("Asset Arrival: 2 arrived at 2.")
        assertNextLine("Asset Arrival: 3 arrived at 2.")
        assertNextLine("Asset Arrival: 4 arrived at 2.")
        assertNextLine("Asset Arrival: 5 arrived at 2.")
        assertNextLine("Asset Arrival: 6 arrived at 2.")
        assertNextLine("Emergency Handling Start: 1 handling started.")

        assertNextLine("Simulation Tick: 7")
        assertNextLine("Simulation Tick: 8")
        assertNextLine("Simulation Tick: 9")
        assertNextLine("Simulation Tick: 10")
        assertNextLine("Simulation Tick: 11")
        assertNextLine("Emergency Resolved: 1 resolved.")
        assertNextLine("Emergency Failed: 0 failed.")

        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 3 received emergencies.")
        assertNextLine("Simulation Statistics: 1 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 1 failed emergencies.")
        assertNextLine("Simulation Statistics: 1 resolved emergencies.")
        assertEnd()
    }
}
