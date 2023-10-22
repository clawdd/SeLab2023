package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val TWOTICK = 2

/**
 * TODO
 *
 */
class TakeRouteWithLowerIDOneTwoTickCopy : SystemTest() {

    override val name = "TakeRouteWithLowerIDOneTwoTickCopy"

    override val map = "mapFiles/take_route_with_lower_id_one_map.dot"
    override val assets = "assetsJsons/take_route_with_lower_id_one_assets.json"
    override val scenario = "scenarioJsons/take_route_with_lower_id_one_scenario.json"

    override val maxTicks = TWOTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: take_route_with_lower_id_one_map.dot successfully parsed and validated")
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
        assertNextLine("Event Ended: 0 ended.")
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 1 received emergencies.")
        assertNextLine("Simulation Statistics: 1 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 0 resolved emergencies.")
        assertEnd()
    }
}
