package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val BVOOBMAXTICK = 10

/**
 * TODO
 *
 */
class BaseVertexOutOfBounds : SystemTest() {

    override val name = "BaseVertexOutOfBounds"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/base_vertex_out_of_bounds_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = BVOOBMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: base_vertex_out_of_bounds_assets.json invalid")
        assertEnd()
    }
}
