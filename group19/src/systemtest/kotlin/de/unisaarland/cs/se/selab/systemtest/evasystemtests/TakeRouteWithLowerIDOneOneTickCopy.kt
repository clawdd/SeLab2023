package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val ONETICK = 1

/**
 * TODO
 *
 */
class TakeRouteWithLowerIDOneOneTickCopy : SystemTest() {

    override val name = "TakeRouteWithLowerIDOneOneTickCopy"

    override val map = "mapFiles/take_route_with_lower_id_one_map.dot"
    override val assets = "assetsJsons/take_route_with_lower_id_one_assets.json"
    override val scenario = "scenarioJsons/take_route_with_lower_id_one_scenario.json"

    override val maxTicks = ONETICK
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
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 0 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 0 resolved emergencies.")
        assertEnd()
    }
}
