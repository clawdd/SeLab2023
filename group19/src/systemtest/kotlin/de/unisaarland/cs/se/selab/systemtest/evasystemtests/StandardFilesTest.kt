package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val STANDARDMAXTICK = 10

/**
 * TODO
 *
 */
class StandardFilesTest : SystemTest() {

    override val name = "StandardFilesTest"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/standard_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = STANDARDMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: standard_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: standard_scenario.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Event Triggered: 0 triggered.")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 0 assigned to 0")
        assertNextLine("Asset Allocation: 1 allocated to 0; 4 ticks to arrive.")
        assertNextLine("Event Ended: 0 ended.")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Asset Arrival: 1 arrived at 1.")
        assertNextLine("Emergency Handling Start: 0 handling started.")
        assertNextLine("Simulation Tick: 6")
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
