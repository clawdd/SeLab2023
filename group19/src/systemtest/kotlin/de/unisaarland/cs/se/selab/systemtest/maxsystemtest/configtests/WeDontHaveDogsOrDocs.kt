package de.unisaarland.cs.se.selab.systemtest.maxsystemtest.configtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class WeDontHaveDogsOrDocs : SystemTest() {

    override val name = "WeDontHaveDogsOrDocs"

    override val map = "mapFiles/berlin_fire_map.dot"
    override val assets = "assetsJsons/wedonthavedogsordocs.json"
    override val scenario = "scenarioJsons/berlin_fire_scenario.json"

    override val maxTicks = 0
    override suspend fun run() {
        assertNextLine("Initialization Info: berlin_fire_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: wedonthavedogsordocs.json invalid")
        assertEnd()
    }
}
