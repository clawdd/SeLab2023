import de.unisaarland.cs.se.selab.systemtest.api.SystemTest
import de.unisaarland.cs.se.selab.systemtest.keensystemtest.NOTHING_TICK

/**
 * no emergency, no event triggered, just end...
 *
 */
class TriangleE : SystemTest() {
    override val name = "TriangleE"
    override val map = "mapFiles/triangleMaps/triangle_b_e.dot"
    override val assets = "assetsJsons/triangleassetJsons/triangle_asset_nothing_happened.json"
    override val scenario = "scenarioJsons/triangle_scenario/triangle_scenario_nothing_happened.json"
    override val maxTicks = NOTHING_TICK

    override suspend fun run() {
        assertNextLine("Initialization Info: triangle_b_e.dot invalid")
        assertEnd()
    }
}
