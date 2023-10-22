import de.unisaarland.cs.se.selab.systemtest.api.SystemTest
import de.unisaarland.cs.se.selab.systemtest.keensystemtest.NOTHING_TICK

/**
 * no emergency, no event triggered, just end...
 *
 */
class TriangleB : SystemTest() {
    override val name = "TriangleB"
    override val map = "mapFiles/triangleMaps/triangle_b_b.dot"
    override val assets = "assetsJsons/triangleassetJsons/triangle_asset_nothing_happened.json"
    override val scenario = "scenarioJsons/triangle_scenario/triangle_scenario_nothing_happened.json"
    override val maxTicks = NOTHING_TICK

    override suspend fun run() {
        assertNextLine("Initialization Info: triangle_b_b.dot invalid")
        assertEnd()
    }
}
