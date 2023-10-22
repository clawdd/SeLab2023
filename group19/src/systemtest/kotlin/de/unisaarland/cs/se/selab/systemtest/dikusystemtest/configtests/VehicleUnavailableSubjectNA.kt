package de.unisaarland.cs.se.selab.systemtest.dikusystemtest.configtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

/**
 * Tests whether you can put in a Vehicle Unavailable event that affects a vehicle ID that isn't there.
 */
class VehicleUnavailableSubjectNA : SystemTest() {

    override val name = "VehicleUnavailableSubjectNA"

    override val map = "mapFiles/dikumapFiles/diku_std_map.dot"
    override val assets = "assetsJsons/dikuassetsJsons/diku_std_assets.json"
    override val scenario = "scenarioJsons/dikuscenarioJsons/noeventvehiclescenario.json"

    override val maxTicks = 12
    override suspend fun run() {
        assertNextLine("Initialization Info: diku_std_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: diku_std_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: noeventvehiclescenario.json invalid")
        assertEnd()
    }
}
