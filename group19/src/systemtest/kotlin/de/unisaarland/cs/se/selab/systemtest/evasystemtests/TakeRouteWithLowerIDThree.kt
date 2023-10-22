package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val TRWLIDTHREEMAXTICK = 10

/**
 * TODO
 *
 */
class TakeRouteWithLowerIDThree : SystemTest() {

    override val name = "TakeRouteWithLowerIDThree"

    override val map = "mapFiles/take_route_with_lower_id_three_map.dot"
    override val assets = "assetsJsons/take_route_with_lower_id_one_assets.json"
    override val scenario = "scenarioJsons/take_route_with_lower_id_one_scenario.json"

    override val maxTicks = TRWLIDTHREEMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: take_route_with_lower_id_three_map.dot successfully parsed and validated")
        assertNextLine(
            "Initialization Info: take_route_with_lower_id_one_assets.json successfully parsed and validated"
        )
        assertNextLine(
            "Initialization Info: take_route_with_lower_id_one_scenario.json successfully parsed and validated"
        )
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Event Triggered: 0 triggered.")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 0 assigned to 0")
        assertNextLine("Asset Allocation: 0 allocated to 0; 3 ticks to arrive.")
        assertNextLine("Event Ended: 0 ended")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Asset Arrival: 0 arrived at 5.")
        assertNextLine("Emergency Handling Start: 0 handling started.")
        assertNextLine("Simulation Tick: 5")
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
