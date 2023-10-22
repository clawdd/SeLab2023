package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.syntaxerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class WrongAttributeDeclarations : SystemTest() {
    override val name = "WrongAttributeDeclarations"

    override val map = "mapFiles/invalidMaps/wrong_attribute_declarations.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: wrong_attribute_declarations.dot invalid")
        assertEnd()
    }
}
