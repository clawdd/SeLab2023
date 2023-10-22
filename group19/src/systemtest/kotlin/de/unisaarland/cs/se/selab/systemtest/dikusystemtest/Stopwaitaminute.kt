package de.unisaarland.cs.se.selab.systemtest.dikusystemtest

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

const val SIMULATION_TICK_STOP = 15

/**
 * Tests basic event handling, but also rush hour procrastination.
 * Rush hour should only trigger when the side street event has ended.
 * Add a version that has an event that only triggers after the whole sim is over
 */
class Stopwaitaminute : SystemTest() {

    override val name = "Stopwaitaminute"

    override val map = "mapFiles/dikumapFiles/diku_extended_map.dot"

    override val assets = "assetsJsons/dikuassetsJsons/diku_beeegrushhour_assets.json"

    override val scenario = "scenarioJsons/dikuscenarioJsons/stopwaitamin_scenario.json"

    override val maxTicks = SIMULATION_TICK_STOP
    override suspend fun run() {
        assertNextLine("Initialization Info: diku_extended_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: diku_beeegrushhour_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: stopwaitamin_scenario.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Event Triggered: 0 triggered.")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 0 assigned to 2")
        assertNextLine("Asset Allocation: 6 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Asset Arrival: 6 arrived at 5.")
        assertNextLine("Emergency Handling Start: 0 handling started.")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Emergency Resolved: 0 resolved.")
        assertNextLine("Assets Rerouted: 1")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Event Ended: 0 ended.")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Simulation Tick: 7")
        assertNextLine("Simulation Tick: 8")
        assertNextLine("Simulation Tick: 9")
        assertNextLine("Simulation Tick: 10")
        assertNextLine("Simulation Tick: 11")
        assertNextLine("Simulation Tick: 12")
        assertNextLine("Simulation Tick: 13")
        assertNextLine("Simulation Tick: 14")
        assertNextLine("Emergency Assignment: 1 assigned to 2")
        assertNextLine("Asset Allocation: 6 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 1 assets rerouted.")
        assertNextLine("Simulation Statistics: 2 received emergencies.")
        assertNextLine("Simulation Statistics: 1 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 1 resolved emergencies.")
        assertEnd()
    }
}
