package de.unisaarland.cs.se.selab.systemtest.keensystemtest
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest
const val NO_HURRY_TICK = 8

/**
 * fire id 0, level 1 start at tick 1, base only have 2 water truck with 600L*2 water
 * toA is only way home to refill water, but because of one way street, effect,
 * from tick 1 - 8,
 * fire id 1, level 1 start at tick 5, cannot fulfill, handle 1 maxduration 2
 * undefined, bug challenge
 * new version:
 * fire id 0, level 1 start at tick 1, base only have 2 water truck with 600L*2 water
 * fire id 1, level 1 start at tick 3, cannot fulfill, handle 1 maxduration 1
 * refill water need time!, cannot reach!
 */
class TriangleDoNotHurry : SystemTest() {

    override val name = "TriangleDoNotHurry"
    override val map = "mapFiles/triangleMaps/triangle_do_not_hurry.dot"
    override val assets = "assetsJsons/triangleassetJsons/triangle_asset_do_not_hurry.json"
    override val scenario = "scenarioJsons/triangle_scenario/triangle_scenario_do_not_hurry.json"
    override val maxTicks = NO_HURRY_TICK

    override suspend fun run() {
        assertNextLine("Initialization Info: triangle_do_not_hurry.dot successfully parsed and validated")
        assertNextLine("Initialization Info: triangle_asset_do_not_hurry.json successfully parsed and validated")
        assertNextLine(
            "Initialization Info: triangle_scenario_do_not_hurry.json invalid"
        )
        assertEnd()
    }
}
