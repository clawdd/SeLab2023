package de.unisaarland.cs.se.selab.systemtest.maxsystemtest.configtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class LocationDoesNotExist2 : SystemTest() {
    override val name = "LocationDoesNotExist"

    override val map = "mapFiles/berlin_fire_map.dot"
    override val assets = "assetsJsons/berlin_fire_assets.json"
    override val scenario = "scenarioJsons/location_wrong2.json"

    override val maxTicks = 0
    override suspend fun run() {
        assertNextLine("Initialization Info: berlin_fire_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: berlin_fire_assets.json successfully parsed and validated")
        assertNextLine("Initialization Info: location_wrong2.json invalid")
        assertEnd()
    }
}
