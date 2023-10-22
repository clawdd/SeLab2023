package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.logicerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class NoMainStreetInVillage : SystemTest() {
    override val name = "NoMainStreetInVillage"

    override val map = "mapFiles/invalidMaps/no_main_street_in_village.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: no_main_street_in_village.dot invalid")
        assertEnd()
    }
}
