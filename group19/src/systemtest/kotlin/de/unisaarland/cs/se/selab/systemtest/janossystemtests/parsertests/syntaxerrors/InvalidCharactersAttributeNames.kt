package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.syntaxerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class InvalidCharactersAttributeNames : SystemTest() {
    override val name = "InvalidCharactersAttributeNames"

    override val map = "mapFiles/invalidMaps/invalid_characters_attribute_names.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: invalid_characters_attribute_names.dot invalid")
        assertEnd()
    }
}
