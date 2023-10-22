package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.logicerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class RoadHeightBelowOne : SystemTest() {
    override val name = "RoadHeightBelowOne"

    override val map = "mapFiles/invalidMaps/road_height_below_one.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: road_height_below_one.dot invalid")
        assertEnd()
    }
}
