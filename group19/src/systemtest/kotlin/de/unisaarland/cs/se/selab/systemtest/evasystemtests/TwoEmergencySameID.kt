package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

/**
 * TODO
 *
 */
class TwoEmergencySameID : SystemTest() {

    override val name = "TwoEventSameID"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/standard_assets.json"
    override val scenario = "scenarioJsons/two_emergency_same_id_scenario.json"

    override val maxTicks = TENTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: standard_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: two_emergency_same_id_scenario.json invalid")
        assertEnd()
    }
}
