package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.syntaxerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class BracketsMising : SystemTest() {
    override val name = "BracketsMissing"

    override val map = "mapFiles/invalidMaps/brackets_missing.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: brackets_missing.dot invalid")
        assertEnd()
    }
}
