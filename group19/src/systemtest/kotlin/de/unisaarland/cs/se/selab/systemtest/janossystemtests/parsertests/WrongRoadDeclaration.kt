package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class WrongRoadDeclaration : SystemTest() {
    override val name = "WrongRoadDeclaration"

    override val map = "mapFiles/invalidMaps/wrong_road_declaration.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: wrong_road_declaration.dot invalid")
        assertEnd()
    }
}
