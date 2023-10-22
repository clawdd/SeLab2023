package de.unisaarland.cs.se.selab.systemtest.amitSystemTests

/*
class BerlinModerate : SystemTest() {

    override val name = "Berlin Moderate Test"

    override val map = "mapFiles/Berlin.dot"
    override val assets = "assetsJsons/berlin_assets.json"
    override val scenario = "scenarioJsons/berlin_scenario2.json"
    override val maxTicks = 15

    override suspend fun run() {
        assertNextLine(
            "Initialization Info: Berlin.dot successfully parsed and validated"
        )
        assertNextLine("Initialization Info: berlin_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: berlin_scenario2.json successfully parsed and validated")

        assertNextLine("Simulation starts")

        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Simulation Tick: 2")

        assertNextLine("Event Triggered: 3 triggered.")

        assertNextLine("Simulation Tick: 3")

        assertNextLine("Emergency Assignment: 1 assigned to 4")
        assertNextLine("Asset Allocation: 20 allocated to 1; 2 ticks to arrive.")

        assertNextLine("Simulation Tick: 4")
        assertNextLine("Simulation Tick: 5")

        assertNextLine("Asset Arrival: 20 arrived at 31.")
        assertNextLine("Emergency Handling Start: 1 handling started.")

        assertNextLine("Simulation Tick: 6")

        assertNextLine("Event Triggered: 2 triggered.")

        assertNextLine("Simulation Tick: 7")

        assertNextLine("Emergency Assignment: 2 assigned to 5")
        assertNextLine("Asset Allocation: 26 allocated to 2; 1 ticks to arrive.")

        assertNextLine("Simulation Tick: 8")

        assertNextLine("Asset Arrival: 26 arrived at 33.")
        assertNextLine("Emergency Resolved: 1 resolved.")
        assertNextLine("Emergency Handling Start: 2 handling started.")

        assertNextLine("Simulation Tick: 9")

        assertNextLine("Event Ended: 3 ended.")

        assertNextLine("Simulation Tick: 10")

        assertNextLine("Emergency Resolved: 2 resolved.")

        assertNextLine("Simulation Tick: 11")

        assertNextLine("Event Ended: 2 ended.")

        assertNextLine("Simulation Tick: 12")
        assertNextLine("Simulation Tick: 13")
        assertNextLine("Simulation Tick: 14")

        assertNextLine("Simulation End")

        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 2 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 2 resolved emergencies.")

        assertEnd()
    }
}
*/
