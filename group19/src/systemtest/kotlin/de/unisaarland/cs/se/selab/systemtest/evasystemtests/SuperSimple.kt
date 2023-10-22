package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

const val SUPERSIMPLETICK = 11

/**
 * TODO
 *
 */
class SuperSimple : SystemTest() {

    override val name = "SuperSimple"

    override val map = "mapFiles/eva_supersimple_map.dot"
    override val assets = "assetsJsons/eva_supersimple_assets.json"
    override val scenario = "scenarioJsons/eva_supersimple_scenario.json"

    override val maxTicks = SUPERSIMPLETICK
    override suspend fun run() {
        assertNextLine("Initialization Info: eva_supersimple_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: eva_supersimple_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: eva_supersimple_scenario.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Event Triggered: 1 triggered.")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 0 assigned to 0")
        assertNextLine("Asset Allocation: 0 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Event Ended: 1 ended.")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Asset Arrival: 0 arrived at 1.")
        assertNextLine("Emergency Handling Start: 0 handling started.")
        assertNextLine("Simulation Tick: 3")
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
