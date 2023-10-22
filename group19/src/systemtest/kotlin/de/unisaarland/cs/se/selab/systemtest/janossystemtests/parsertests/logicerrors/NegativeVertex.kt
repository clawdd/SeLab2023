package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.logicerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class NegativeVertex : SystemTest() {
    override val name = "NegativeVertex"

    override val map = "mapFiles/invalidMaps/negative_vertices.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: negative_vertices.dot invalid")
        assertEnd()
    }
}
