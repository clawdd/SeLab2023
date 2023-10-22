package de.unisaarland.cs.se.selab.systemtest.janossystemtests.scenariotests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

const val SIMULATION_TICK_DIS = 0

class DisabledDuringFire : SystemTest() {

    override val name = "BerlinFireTest"

    override val map = "src/systemtest/resources/mapFiles/janosScenariosMaps/disabled_during_fire.dot"
    override val assets = "src/systemtest/resources/assetsJsons/janosScenariosAssets/disabled_during_fire_assets.json"
    override val scenario = "src/systemtest/resources/scenarioJsons/janosScenarios/disabled_during_fire_scen.json"

    override val maxTicks = SIMULATION_TICK_DIS
    override suspend fun run() {
        assertNextLine("Initialization Info: disabled_during_fire.dot successfully parsed and validated")
        assertNextLine("Initialization Info: disabled_during_fire_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: disabled_during_fire_scen.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 45001 assigned to 0")
        assertNextLine("Asset Allocation: 56461 allocated to 45001; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 56462 allocated to 45001; 2 ticks to arrive.")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Asset Arrival: 56461 arrived at 45001.")
        assertNextLine("Asset Arrival: 56462 arrived at 45001.")
        assertNextLine("Emergency Handling Start: 45001 handling started.")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Emergency Resolved: 45001 resolved.")
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Simulation Tick: 7")
        assertNextLine("Simulation Tick: 8")
        assertNextLine("Simulation Tick: 9")
        assertNextLine("Simulation Tick: 10")
        assertNextLine("Event Ended: 69420 triggered.")
        assertNextLine("Simulation Tick: 11")
        assertNextLine("Event Ended: 69420 ended.")
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 1 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 1 resolved emergencies.")
        assertEnd()
    }
}
