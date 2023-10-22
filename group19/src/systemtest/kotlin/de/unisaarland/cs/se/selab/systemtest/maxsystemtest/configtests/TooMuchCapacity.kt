package de.unisaarland.cs.se.selab.systemtest.maxsystemtest.configtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

/**
 * TODO
 *
 */
class TooMuchCapacity : SystemTest() {
    override val name = "TooMuchCapacity"

    override val map = "mapFiles/berlin_fire_map.dot"
    override val assets = "assetsJsons/tooMuchCapacity.json"
    override val scenario = "scenarioJsons/berlin_fire_scenario.json"

    override val maxTicks = 0
    override suspend fun run() {
        assertNextLine("Initialization Info: berlin_fire_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: tooMuchCapacity.json invalid")
        assertEnd()
    }
}
