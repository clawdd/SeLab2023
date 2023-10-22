package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

/**
 * TODO
 *
 */
class TwoRoadsSameVertices : SystemTest() {

    override val name = "TwoRoadsSameVertices"

    override val map = "mapFiles/two_roads_same_vertices_map.dot"
    override val assets = "assetsJsons/standard_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = TENTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: two_roads_same_vertices_map.dot invalid")
        assertEnd()
    }
}
