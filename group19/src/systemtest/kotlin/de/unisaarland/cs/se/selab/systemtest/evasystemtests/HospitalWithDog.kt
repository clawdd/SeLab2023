package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val HWDMAXTICK = 10

/**
 * TODO
 *
 */
class HospitalWithDog : SystemTest() {

    override val name = "HospitalWithDog"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/hospital_with_dog_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = HWDMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: hospital_with_dog_assets.json invalid")
        assertEnd()
    }
}
