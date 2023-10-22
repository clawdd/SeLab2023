package de.unisaarland.cs.se.selab.systemtest.keensystemtest
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest
const val NOTHING_TICK = 5

/**
 * no emergency, no event triggered, just end...
 *
 */
class TriangleNothingHappened : SystemTest() {

    override val name = "TriangleNothingHappened"
    override val map = "mapFiles/triangleMaps/triangleMap.dot"
    override val assets = "assetsJsons/triangleassetJsons/triangle_asset_nothing_happened.json"
    override val scenario = "scenarioJsons/triangle_scenario/triangle_scenario_nothing_happened.json"
    override val maxTicks = NOTHING_TICK

    override suspend fun run() {
        assertNextLine("Initialization Info: triangleMap.dot successfully parsed and validated")
        assertNextLine("Initialization Info: triangle_asset_nothing_happened.json successfully parsed and validated")
        assertNextLine("Initialization Info: triangle_scenario_nothing_happened.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 0 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 0 failed emergencies.")
        assertNextLine("Simulation Statistics: 0 resolved emergencies.")
        assertEnd()
    }
}
