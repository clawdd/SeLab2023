package de.unisaarland.cs.se.selab.systemtest.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

const val KEY_REQUEST_TWO_FAILED = "Request Failed: 2 failed."
class ExampleTestContinue : SystemTest() {
    override val name = "ExampleTest"

    override val map = "mapFiles/example_map.dot"
    override val assets = "assetsJsons/example_assets.json"
    override val scenario = "scenarioJsons/example_scenario.json"
    override val maxTicks = 15

    override suspend fun run() {
        // everything is parsed and validated
        firstParts()
        secondParts()
        thirdParts()
        //
        assertNextLine("Simulation End")
        // Statistics
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 3 received emergencies.")
        assertNextLine("Simulation Statistics: 1 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 2 resolved emergencies.")
        // end of file is reached
        assertEnd()
    }

    private suspend fun ExampleTestContinue.thirdParts() {
        assertNextLine("Simulation Tick: 10")
        assertNextLine("Asset Request: 13 sent to 1 for 2.")
        assertNextLine("Asset Request: 14 sent to 4 for 2.")
        assertNextLine("Request Failed: 2 failed.")
        assertNextLine("Asset Arrival: 37 arrived at 11.")
        assertNextLine("Asset Arrival: 12 arrived at 14.")
        assertNextLine("Asset Arrival: 13 arrived at 14.")
        assertNextLine("Asset Arrival: 14 arrived at 14.")
        assertNextLine("Asset Arrival: 15 arrived at 14.")
        assertNextLine("Asset Arrival: 16 arrived at 14.")
        assertNextLine("Asset Arrival: 17 arrived at 14.")
        assertNextLine("Emergency Resolved: 1 resolved.")
        assertNextLine("Simulation Tick: 11")
        assertNextLine("Asset Request: 15 sent to 1 for 2.")
        assertNextLine("Asset Request: 16 sent to 4 for 2.")
        assertNextLine(KEY_REQUEST_TWO_FAILED)
        assertNextLine("Asset Arrival: 6 arrived at 9.")
        assertNextLine("Asset Arrival: 8 arrived at 9.")
        assertNextLine("Asset Arrival: 46 arrived at 10.")
        assertNextLine("Event Ended: 2 ended.")
        assertNextLine("Simulation Tick: 12")
        assertNextLine("Asset Request: 17 sent to 1 for 2.")
        assertNextLine("Asset Request: 18 sent to 4 for 2.")
        assertNextLine(KEY_REQUEST_TWO_FAILED)
        assertNextLine("Simulation Tick: 13")
        assertNextLine("Asset Request: 19 sent to 1 for 2.")
        assertNextLine("Asset Request: 20 sent to 4 for 2.")
        assertNextLine(KEY_REQUEST_TWO_FAILED)
        assertNextLine("Simulation Tick: 14")
        assertNextLine("Asset Arrival: 50 arrived at 11.")
        assertNextLine("Asset Arrival: 52 arrived at 11.")
    }

    private suspend fun ExampleTestContinue.secondParts() {
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Event Ended: 0 ended.")
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Emergency Resolved: 0 resolved.")
        assertNextLine("Event Triggered: 1 triggered.")
        assertNextLine("Event Triggered: 2 triggered.")
        assertNextLine("Simulation Tick: 7")
        assertNextLine("Asset Arrival: 18 arrived at 2.")
        assertNextLine("Asset Arrival: 22 arrived at 2.")
        assertNextLine("Event Ended: 1 ended.")
        assertNextLine("Simulation Tick: 8")
        assertNextLine("Simulation Tick: 9")
        assertNextLine("Emergency Assignment: 2 assigned to 7")
        assertNextLine("Asset Allocation: 12 allocated to 2; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 13 allocated to 2; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 14 allocated to 2; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 15 allocated to 2; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 16 allocated to 2; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 17 allocated to 2; 1 ticks to arrive.")
        assertNextLine("Asset Request: 5 sent to 1 for 2.")
        assertNextLine("Asset Request: 6 sent to 4 for 2.")
        assertNextLine("Asset Request: 7 sent to 6 for 2.")
        assertNextLine("Asset Allocation: 37 allocated to 2; 1 ticks to arrive.")
        assertNextLine("Asset Request: 8 sent to 0 for 2.")
        assertNextLine("Asset Request: 9 sent to 3 for 2.")
        assertNextLine("Asset Request: 10 sent to 8 for 2.")
        assertNextLine("Asset Allocation: 50 allocated to 2; 4 ticks to arrive.")
        assertNextLine("Asset Allocation: 52 allocated to 2; 4 ticks to arrive.")
        assertNextLine("Asset Request: 11 sent to 2 for 2.")
        assertNextLine("Asset Request: 12 sent to 5 for 2.")
        assertNextLine(KEY_REQUEST_TWO_FAILED)
    }

    private suspend fun ExampleTestContinue.firstParts() {
        assertNextLine("Initialization Info: example_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: example_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: example_scenario.json successfully parsed and validated")
        // The Simulation starts with tick 0
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Emergency Assignment: 0 assigned to 0")
        assertNextLine("Emergency Assignment: 1 assigned to 3")
        assertNextLine("Asset Allocation: 18 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 22 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 27 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 31 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Request: 1 sent to 4 for 1.")
        assertNextLine("Asset Allocation: 6 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 8 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Request: 2 sent to 1 for 1.")
        assertNextLine("Asset Request: 3 sent to 7 for 1.")
        assertNextLine("Asset Request: 4 sent to 5 for 1.")
        assertNextLine("Asset Allocation: 46 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Asset Arrival: 18 arrived at 2.")
        assertNextLine("Asset Arrival: 22 arrived at 2.")
        assertNextLine("Asset Arrival: 27 arrived at 9.")
        assertNextLine("Asset Arrival: 31 arrived at 9.")
        assertNextLine("Asset Arrival: 6 arrived at 9.")
        assertNextLine("Asset Arrival: 8 arrived at 9.")
        assertNextLine("Asset Arrival: 46 arrived at 10.")
        assertNextLine("Emergency Handling Start: 0 handling started.")
        assertNextLine("Emergency Handling Start: 1 handling started.")
        assertNextLine("Event Triggered: 0 triggered.")
    }
}
