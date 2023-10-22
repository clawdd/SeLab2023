package de.unisaarland.cs.se.selab.systemtest.amitSystemTests

// private const val MSG1: String = "Request Failed: 1 failed."
// private const val MSG2: String = "Request Failed: 3 failed."
/*
class BerlinHard : SystemTest() {

    override val name = "Berlin Hard Test"

    override val map = "mapFiles/Berlin.dot"
    override val assets = "assetsJsons/berlin_assets.json"
    override val scenario = "scenarioJsons/berlin_scenario3.json"
    override val maxTicks = 20

    override suspend fun run() {
        extracted()

        assertNextLine("Asset Request: 5 sent to 5 for 1.")
        assertNextLine("Asset Request: 6 sent to 6 for 1.")

        assertNextLine(MSG1)

        assertNextLine("Asset Arrival: 20 arrived at 31.")
        assertNextLine("Asset Arrival: 21 arrived at 31.")

        assertNextLine("Simulation Tick: 6")

        assertNextLine("Asset Request: 7 sent to 5 for 1.")
        assertNextLine("Asset Request: 8 sent to 6 for 1.")
        assertNextLine(MSG1)
        assertNextLine("Event Triggered: 2 triggered.")

        assertNextLine("Simulation Tick: 7")

        assertNextLine("Emergency Assignment: 2 assigned to 5")
        assertNextLine("Asset Request: 9 sent to 5 for 1.")
        assertNextLine("Asset Request: 10 sent to 6 for 1.")
        assertNextLine(MSG1)

        assertNextLine("Simulation Tick: 8")

        assertNextLine("Asset Request: 11 sent to 5 for 1.")
        assertNextLine("Asset Request: 12 sent to 6 for 1.")
        assertNextLine(MSG1)

        assertNextLine("Simulation Tick: 9")

        assertNextLine("Asset Request: 13 sent to 5 for 1.")
        assertNextLine("Asset Request: 14 sent to 6 for 1.")
        assertNextLine(MSG1)

        assertNextLine("Simulation Tick: 10")

        assertNextLine("Emergency Assignment: 3 assigned to 6")
        assertNextLine("Asset Allocation: 35 allocated to 3; 2 ticks to arrive.")
        assertNextLine(MSG2)

        assertNextLine("Simulation Tick: 11")

        assertNextLine(MSG2)
        assertNextLine("Event Ended: 2 ended.")

        assertNextLine("Simulation Tick: 12")

        assertNextLine(MSG2)

        assertNextLine("Simulation Tick: 13")

        assertNextLine(MSG2)
        assertNextLine("Asset Arrival: 35 arrived at 29.")
        assertNextLine("Emergency Failed: 1 failed.")

        assertNextLine("Simulation Tick: 14")

        assertNextLine("Asset Arrival: 26 arrived at 32.")
        assertNextLine("Asset Arrival: 27 arrived at 32.")
        assertNextLine("Asset Arrival: 30 arrived at 32.")
        assertNextLine("Asset Arrival: 31 arrived at 32.")

        assertNextLine("Event Ended: 1 ended.")
        assertNextLine("Event Ended: 3 ended.")

        assertNextLine("Simulation Tick: 15")
        assertNextLine("Simulation Tick: 16")
        assertNextLine("Simulation Tick: 17")
        assertNextLine("Simulation Tick: 18")
        assertNextLine("Emergency Failed: 2 failed.")

        assertNextLine("Simulation Tick: 19")

        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 3 received emergencies.")
        assertNextLine("Simulation Statistics: 1 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 2 failed emergencies.")
        assertNextLine("Simulation Statistics: 0 resolved emergencies.")

        assertEnd()
    }

    private suspend fun BerlinHard.extracted() {
        assertNextLine(
            "Initialization Info: Berlin.dot successfully parsed and validated"
        )
        assertNextLine("Initialization Info: berlin_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: berlin_scenario3.json successfully parsed and validated")

        assertNextLine("Simulation starts")

        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Simulation Tick: 2")

        assertNextLine("Event Triggered: 3 triggered.")

        assertNextLine("Simulation Tick: 3")

        assertNextLine("Emergency Assignment: 1 assigned to 4")
        assertNextLine("Asset Allocation: 20 allocated to 1; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 21 allocated to 1; 2 ticks to arrive.")

        assertNextLine("Asset Request: 1 sent to 5 for 1.")

        assertNextLine("Asset Allocation: 26 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 27 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 30 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 31 allocated to 1; 1 ticks to arrive.")

        assertNextLine("Asset Request: 2 sent to 6 for 1.")

        assertNextLine("Asset Allocation: 32 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 33 allocated to 1; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 34 allocated to 1; 1 ticks to arrive.")

        assertNextLine(MSG1)

        assertNextLine("Simulation Tick: 4")

        assertNextLine("Asset Request: 3 sent to 5 for 1.")
        assertNextLine("Asset Request: 4 sent to 6 for 1.")
        assertNextLine(MSG1)

        assertNextLine("Asset Arrival: 26 arrived at 32.")
        assertNextLine("Asset Arrival: 27 arrived at 32.")
        assertNextLine("Asset Arrival: 30 arrived at 32.")
        assertNextLine("Asset Arrival: 31 arrived at 32.")
        assertNextLine("Asset Arrival: 32 arrived at 32.")
        assertNextLine("Asset Arrival: 33 arrived at 32.")
        assertNextLine("Asset Arrival: 34 arrived at 32.")
        assertNextLine("Event Triggered: 1 triggered.")
        assertNextLine("Simulation Tick: 5")
    }
}
*/
