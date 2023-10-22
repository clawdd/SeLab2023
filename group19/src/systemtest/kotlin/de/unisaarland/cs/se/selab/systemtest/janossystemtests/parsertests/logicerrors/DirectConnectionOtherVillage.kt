package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.logicerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class DirectConnectionOtherVillage : SystemTest() {
    override val name = "DirectConnectionOtherVillage"

    override val map = "mapFiles/invalidMaps/direct_connection_other_village.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: direct_connection_other_village.dot invalid")
        assertEnd()
    }
}
