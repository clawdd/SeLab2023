package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.logicerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class NewConnectionSwapped : SystemTest() {
    override val name = "NewConnectionSwapped"

    override val map = "mapFiles/invalidMaps/new_connection_source_target_swapped.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: new_connection_source_target_swapped.dot invalid")
        assertEnd()
    }
}
