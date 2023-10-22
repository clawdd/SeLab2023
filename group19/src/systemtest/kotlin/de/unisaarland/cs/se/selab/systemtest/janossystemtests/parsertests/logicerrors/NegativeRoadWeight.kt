package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.logicerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class NegativeRoadWeight : SystemTest() {
    override val name = "NegativeRoadWeight"

    override val map = "mapFiles/invalidMaps/negative_road_weight.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: negative_road_weight.dot invalid")
        assertEnd()
    }
}
