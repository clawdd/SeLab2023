package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val MAXTICKCRC = 22

/**
 * TODO
 *
 */
class CologneReroutingCapacity : SystemTest() {

    override val name = "CologneReroutingCapacity"

    override val map = "mapFiles/cologne_rerouting_capacity_map.dot"
    override val assets = "assetsJsons/cologne_rerouting_capacity_assets.json"
    override val scenario = "scenarioJsons/cologne_rerouting_capacity_scenario.json"

    override val maxTicks = MAXTICKCRC
    override suspend fun run() {
        assertNextLine("Initialization Info: cologne_rerouting_capacity_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: cologne_rerouting_capacity_assets.json successfully parsed and validated")
        assertNextLine(
            "Initialization Info: cologne_rerouting_capacity_scenario.json successfully parsed and validated"
        )
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
        assertNextLine("Asset Allocation: 12 allocated to 1; 3 ticks to arrive.")
        assertNextLine("Asset Allocation: 15 allocated to 1; 3 ticks to arrive.")
        assertNextLine("Asset Allocation: 16 allocated to 1; 3 ticks to arrive.")
        assertNextLine("Asset Allocation: 17 allocated to 1; 3 ticks to arrive.")
        assertNextLine("Asset Request: 1 sent to 3 for 1.")
        assertNextLine("Asset Allocation: 3 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Emergency Resolved: 0 resolved.")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Asset Arrival: 3 arrived at 3.")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Asset Arrival: 5 arrived at 4.")
        assertNextLine("Asset Arrival: 12 arrived at 4.")
        assertNextLine("Asset Arrival: 15 arrived at 4.")
        assertNextLine("Asset Arrival: 16 arrived at 4.")
        assertNextLine("Asset Arrival: 17 arrived at 4.")
        assertNextLine("Emergency Handling Start: 1 handling started.")
        assertNextLine("Simulation Tick: 7")
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
