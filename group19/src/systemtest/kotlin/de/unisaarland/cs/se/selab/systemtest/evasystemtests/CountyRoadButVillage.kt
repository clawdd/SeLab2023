package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

/**
 * TODO
 *
 */
class CountyRoadButVillage : SystemTest() {

    override val name = "CountyRoadButVillage"

    override val map = "mapFiles/county_road_but_village_name_map.dot"
    override val assets = "assetsJsons/standard_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = TENTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: county_road_but_village_name_map.dot invalid")
        assertEnd()
    }
}
