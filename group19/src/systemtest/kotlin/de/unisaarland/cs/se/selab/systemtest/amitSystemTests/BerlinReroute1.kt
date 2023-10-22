package de.unisaarland.cs.se.selab.systemtest.amitSystemTests

/*
class BerlinReroute1 : SystemTest() {

    override val name = "Berlin Reroute Test 1"

    override val map = "mapFiles/Berlin.dot"
    override val assets = "assetsJsons/berlin_assets.json"
    override val scenario = "scenarioJsons/berlin_scenario4.json"
    override val maxTicks = 15

    override suspend fun run() {
        assertNextLine(
            "Initialization Info: Berlin.dot successfully parsed and validated"
        )
        assertNextLine("Initialization Info: berlin_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: berlin_scenario4.json successfully parsed and validated")

        assertNextLine("Simulation starts")

        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Simulation Tick: 2")

        assertNextLine("Emergency Assignment: 1 assigned to 4")
        assertNextLine("Asset Allocation: 20 allocated to 1; 2 ticks to arrive.")

        assertNextLine("Event Triggered: 3 triggered.")

        assertNextLine("Simulation Tick: 3")

        assertNextLine("Emergency Assignment: 2 assigned to 4")

        assertNextLine("Asset Allocation: 21 allocated to 2; 1 ticks to arrive.")
        assertNextLine("Asset Reallocation: 20 reallocated to 2.")
        assertNextLine("Asset Request: 1 sent to 5 for 2.")
        assertNextLine("Asset Allocation: 26 allocated to 2; 3 ticks to arrive.")
        assertNextLine("Asset Allocation: 30 allocated to 2; 3 ticks to arrive.")

        assertNextLine("Asset Request: 2 sent to 6 for 2.")
        assertNextLine("Asset Allocation: 32 allocated to 2; 3 ticks to arrive.")

        assertNextLine("Asset Arrival: 20 arrived at 19.")

        assertNextLine("Simulation Tick: 4")

        assertNextLine("Asset Arrival: 21 arrived at 19.")
        assertNextLine("Event Triggered: 1 triggered.")

        assertNextLine("Simulation Tick: 5")
        assertNextLine("Simulation Tick: 6")

        assertNextLine("Event Triggered: 2 triggered.")

        assertNextLine("Simulation Tick: 7")
        assertNextLine("Simulation Tick: 8")

        assertNextLine("Asset Arrival: 32 arrived at 18.")
        assertNextLine("Event Ended: 3 ended.")

        assertNextLine("Simulation Tick: 9")

        assertNextLine("Asset Arrival: 26 arrived at 18.")
        assertNextLine("Asset Arrival: 30 arrived at 18.")
        assertNextLine("Emergency Handling Start: 2 handling started.")

        assertNextLine("Simulation Tick: 10")
        assertNextLine("Simulation Tick: 11")

        assertNextLine("Event Ended: 2 ended.")

        assertNextLine("Simulation Tick: 12")
        assertNextLine("Emergency Failed: 1 failed.")
        assertNextLine("Emergency Resolved: 2 resolved.")

        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 2 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 1 failed emergencies.")
        assertNextLine("Simulation Statistics: 1 resolved emergencies.")

        assertEnd()
    }
}
*/
