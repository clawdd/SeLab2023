package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.syntaxerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class SemicolonMissing : SystemTest() {
    // checks if the ";" after every number is there
    override val name = "SemicolonMissing"

    override val map = "mapFiles/invalidMaps/semicolon_missing.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: semicolon_missing.dot invalid")
        assertEnd()
    }
}
