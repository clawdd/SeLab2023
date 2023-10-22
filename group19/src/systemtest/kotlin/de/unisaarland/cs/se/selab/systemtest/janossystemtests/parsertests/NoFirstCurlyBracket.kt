package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class NoFirstCurlyBracket : SystemTest() {
    // checks if the first "{" is not missing
    override val name = "NoFirstCurlyBracket"

    override val map = "mapFiles/invalidMaps/no_first_curly_bracket.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: no_first_curly_bracket.dot invalid")
        assertEnd()
    }
}
