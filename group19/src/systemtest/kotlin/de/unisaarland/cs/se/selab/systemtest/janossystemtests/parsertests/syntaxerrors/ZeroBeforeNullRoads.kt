package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.syntaxerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class ZeroBeforeNullRoads : SystemTest() {
    override val name = "ZeroBeforeNullRoads"

    override val map = "src/systemtest/resources/mapFiles/invalidMaps/zero_before_number_roads.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: zero_before_number_roads.dot invalid")
        assertEnd()
    }
}
