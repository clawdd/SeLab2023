package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.syntaxerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class NoSecondCurlyBracket : SystemTest() {
    // checks if the closing "}" is not missing
    override val name = "NoSecondCurlyBracket"

    override val map = "mapFiles/invalidMaps/no_second_curly_bracket.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: no_second_curly_bracket.dot invalid")
        assertEnd()
    }
}
