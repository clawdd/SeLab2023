package de.unisaarland.cs.se.selab.systemtest.amitSystemTests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class Frankfurt : SystemTest() {

    override val name = "SimpleMapTest"

    override val map = "mapFiles/Frankfurt.dot"
    override val assets = "assetsJsons/frankfurt_assets.json"
    override val scenario = "scenarioJsons/frankfurt_scenario.json"
    override val maxTicks = 15

    override suspend fun run() {
        assertNextLine("Initialization Info: Frankfurt.dot successfully parsed and validated")
        assertNextLine("Initialization Info: frankfurt_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: frankfurt_scenario.json successfully parsed and validated")

        assertNextLine("Simulation starts")

        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Simulation Tick: 2")

        assertNextLine("Emergency Assignment: 1 assigned to 1")
        assertNextLine("Asset Allocation: 1 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 2 allocated to 1; 1 ticks to arrive.")

        assertNextLine("Simulation Tick: 3")

        assertNextLine("Asset Arrival: 1 arrived at 3.")
        assertNextLine("Asset Arrival: 2 arrived at 3.")

        assertNextLine("Simulation Tick: 4")

        assertNextLine("Event Triggered: 1 triggered.")

        assertNextLine("Simulation Tick: 5")

        assertNextLine("Event Ended: 1 ended.")

        assertNextLine("Simulation Tick: 6")
        assertNextLine("Simulation Tick: 7")

        assertNextLine("Emergency Handling Start: 1 handling started.")

        assertNextLine("Emergency Resolved: 1 resolved.")

        assertNextLine("Simulation End")

        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 1 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 1 resolved emergencies.")

        assertEnd()
    }
}
