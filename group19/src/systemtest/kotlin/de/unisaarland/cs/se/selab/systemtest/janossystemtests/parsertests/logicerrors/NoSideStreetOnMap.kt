package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.logicerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class NoSideStreetOnMap : SystemTest() {
    override val name = "NoSideStreetOnMap"

    override val map = "mapFiles/invalidMaps/no_side_street_on_map.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: no_side_street_on_map.dot invalid")
        assertEnd()
    }
}
