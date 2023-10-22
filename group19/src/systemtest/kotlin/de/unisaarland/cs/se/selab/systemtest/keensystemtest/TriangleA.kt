package de.unisaarland.cs.se.selab.systemtest.keensystemtest
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

/**
 * no emergency, no event triggered, just end...
 *
 */
class TriangleA : SystemTest() {
    override val name = "TriangleA"
    override val map = "mapFiles/triangleMaps/triangle_b_a.dot"
    override val assets = "assetsJsons/triangleassetJsons/triangle_asset_nothing_happened.json"
    override val scenario = "scenarioJsons/triangle_scenario/triangle_scenario_nothing_happened.json"
    override val maxTicks = NOTHING_TICK

    override suspend fun run() {
        assertNextLine("Initialization Info: triangle_b_a.dot invalid")
        assertEnd()
    }
}
