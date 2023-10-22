package de.unisaarland.cs.se.selab.systemtest.dikusystemtest

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

const val SIMULATION_TICK = 20

/**
 * Tests basic event handling, but also rush hour procrastination.
 * Rush hour should only trigger when the side street event has ended.
 */
class FakeFireTest : SystemTest() {

    override val name = "FakeFireTest"

    override val map = "mapFiles/dikumapFiles/diku_std_map.dot"

    override val assets = "assetsJsons/dikuassetsJsons/diku_std_assets.json"

    override val scenario = "scenarioJsons/dikuscenarioJsons/diku_std_scenario.json"
    // override val assets = "assetsJsons/berlin_fire_assets.json"
    // override val scenario = "scenarioJsons/berlin_fire_scenario.json"

    override val maxTicks = SIMULATION_TICK
    override suspend fun run() {
        assertNextLine("Initialization Info: diku_std_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: diku_std_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: diku_std_scenario.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Event Triggered: 0 triggered.")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Event Triggered: 2 triggered.")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Event Ended: 0 ended.")
        assertNextLine("Event Triggered: 1 triggered.")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Event Ended: 2 ended.")
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Simulation Tick: 7")
        assertNextLine("Simulation Tick: 8")
        assertNextLine("Event Ended: 1 ended.")
        assertNextLine("Simulation Tick: 9")
        assertNextLine("Simulation Tick: 10")
        assertNextLine("Simulation Tick: 11")
        assertNextLine("Simulation Tick: 12")
        assertNextLine("Simulation Tick: 13")
        assertNextLine("Simulation Tick: 14")
        assertNextLine("Simulation Tick: 15")
        assertNextLine("Simulation Tick: 16")
        assertNextLine("Simulation Tick: 17")
        assertNextLine("Simulation Tick: 18")
        assertNextLine("Simulation Tick: 19")
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 0 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 0 resolved emergencies.")
        assertEnd()
    }
}
