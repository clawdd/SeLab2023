package de.unisaarland.cs.se.selab.systemtest.maxsystemtest.simulationtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

const val SIMULATION_TICK = 11

/**
 * TODO
 *
 */
class BerlinFireTest : SystemTest() {

    override val name = "BerlinFireTest"

    override val map = "mapFiles/berlin_fire_map.dot"
    override val assets = "assetsJsons/berlin_fire_assets.json"
    override val scenario = "scenarioJsons/berlin_fire_scenario.json"

    override val maxTicks = SIMULATION_TICK
    override suspend fun run() {
        assertNextLine("Initialization Info: berlin_fire_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: berlin_fire_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: berlin_fire_scenario.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Emergency Assignment: 0 assigned to 0")
        assertNextLine("Asset Allocation: 1 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 2 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 3 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Asset Request: 1 sent to 1 for 0.")
        assertNextLine("Asset Allocation: 4 allocated to 0; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 5 allocated to 0; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 6 allocated to 0; 2 ticks to arrive.")
        assertNextLine("Asset Request: 2 sent to 2 for 0.")
        assertNextLine("Asset Allocation: 7 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Request Failed: 0 failed.")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Asset Arrival: 1 arrived at 1.")
        assertNextLine("Asset Arrival: 2 arrived at 1.")
        assertNextLine("Asset Arrival: 3 arrived at 1.")
        assertNextLine("Asset Arrival: 7 arrived at 0.")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Asset Arrival: 4 arrived at 0.")
        assertNextLine("Asset Arrival: 5 arrived at 0.")
        assertNextLine("Asset Arrival: 6 arrived at 0.")
        assertNextLine("Emergency Handling Start: 0 handling started.")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Simulation Tick: 7")
        assertNextLine("Simulation Tick: 8")
        assertNextLine("Simulation Tick: 9")
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
