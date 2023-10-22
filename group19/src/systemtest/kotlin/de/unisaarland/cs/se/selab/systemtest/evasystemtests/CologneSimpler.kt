package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val MAXTICKCS = 20

/**
 * TODO
 *
 */
class CologneSimpler : SystemTest() {

    override val name = "CologneSimpler"

    override val map = "mapFiles/cologne_simpler_map.dot"
    override val assets = "assetsJsons/cologne_simpler_assets.json"
    override val scenario = "scenarioJsons/cologne_simpler_scenario.json"

    override val maxTicks = MAXTICKCS
    override suspend fun run() {
        assertNextLine("Initialization Info: cologne_simpler_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: cologne_simpler_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: cologne_simpler_scenario.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Event Triggered: 0 triggered.")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 0 assigned to 0")
        assertNextLine("Asset Allocation: 2 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Asset Arrival: 2 arrived at 1.")
        assertNextLine("Emergency Handling Start: 0 handling started.")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Emergency Assignment: 1 assigned to 0")
        assertNextLine("Asset Allocation: 5 allocated to 1; 3 ticks to arrive.")
        assertNextLine("Asset Allocation: 17 allocated to 1; 3 ticks to arrive.")
        assertNextLine("Asset Request: 1 sent to 3 for 1.")
        assertNextLine("Asset Allocation: 3 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Request: 2 sent to 5 for 1.")
        assertNextLine("Asset Request: 3 sent to 4 for 1.")
        assertNextLine("Asset Allocation: 10 allocated to 1; 7 ticks to arrive.")
        assertNextLine("Asset Allocation: 11 allocated to 1; 7 ticks to arrive.")
        assertNextLine("Request Failed: 1 failed.")
        assertNextLine("Emergency Resolved: 0 resolved.")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Asset Arrival: 3 arrived at 3.")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Asset Allocation: 2 allocated to 1; 3 ticks to arrive.")
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Asset Arrival: 5 arrived at 4.")
        assertNextLine("Asset Arrival: 17 arrived at 4.")
        assertNextLine("Simulation Tick: 7")
        assertNextLine("Simulation Tick: 8")
        assertNextLine("Asset Arrival: 2 arrived at 4.")
        assertNextLine("Simulation Tick: 9")
        assertNextLine("Simulation Tick: 10")
        assertNextLine("Asset Arrival: 10 arrived at 3.")
        assertNextLine("Asset Arrival: 11 arrived at 3.")
        assertNextLine("Emergency Handling Start: 1 handling started.")
        assertNextLine("Event Ended: 0 ended.")
        assertNextLine("Simulation Tick: 11")
        assertNextLine("Emergency Resolved: 1 resolved.")
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 2 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 2 resolved emergencies.")
        assertEnd()
    }
}
