package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val PSWDMAXTICK = 10

/**
 * TODO
 *
 */
class PoliceStationWithDoctor : SystemTest() {

    override val name = "PoliceStationWithDoctor"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/police_station_with_doctor_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = PSWDMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: police_station_with_doctor_assets.json invalid")
        assertEnd()
    }
}
