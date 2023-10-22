package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.syntaxerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class DiagraphTypo : SystemTest() {
    override val name = "InvalidCountyName"

    override val map = "mapFiles/invalidMaps/diagraph_typo.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: diagraph_typo.dot invalid")
        assertEnd()
    }
}
