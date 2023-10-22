package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.logicerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class RoadNameNotUnique : SystemTest() {
    override val name = "RoadNameNotUnique"

    override val map = "mapFiles/invalidMaps/road_name_in_village_not_unique.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: road_name_in_village_not_unique.dot invalid")
        assertEnd()
    }
}
