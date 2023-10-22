package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.logicerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class NewConnectionBetweenVertices : SystemTest() {
    override val name = "NewConnectionBetweenVertices"

    override val map = "mapFiles/invalidMaps/new_road_between_two_verties.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: new_road_between_two_verties.dot invalid")
        assertEnd()
    }
}
