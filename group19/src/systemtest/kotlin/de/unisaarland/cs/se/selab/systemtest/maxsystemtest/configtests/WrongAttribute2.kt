package de.unisaarland.cs.se.selab.systemtest.maxsystemtest.configtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

/**
 * TODO
 *
 */
class WrongAttribute2 : SystemTest() {
    override val name = "WrongAttribute2"

    override val map = "mapFiles/berlin_fire_map.dot"
    override val assets = "assetsJsons/wrongAttribute2.json"
    override val scenario = "scenarioJsons/berlin_fire_scenario.json"

    override val maxTicks = 0
    override suspend fun run() {
        assertNextLine("Initialization Info: berlin_fire_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: wrongAttribute2.json invalid")
        assertEnd()
    }
}
