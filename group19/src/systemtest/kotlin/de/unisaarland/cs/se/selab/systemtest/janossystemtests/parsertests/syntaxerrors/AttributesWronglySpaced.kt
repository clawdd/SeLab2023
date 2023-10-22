package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.syntaxerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class AttributesWronglySpaced : SystemTest() {
    override val name = "AttributesWronglySpaced"

    override val map = "mapFiles/invalidMaps/attributes_wrongly_spaced.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: attributes_wrongly_spaced.dot invalid")
        assertEnd()
    }
}
