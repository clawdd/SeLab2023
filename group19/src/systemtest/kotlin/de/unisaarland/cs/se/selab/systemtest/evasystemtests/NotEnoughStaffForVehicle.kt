package de.unisaarland.cs.se.selab.systemtest.evasystemtests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

private const val NESFVMAXTICK = 10

/**
 * TODO
 *
 */
class NotEnoughStaffForVehicle : SystemTest() {

    override val name = "NotEnoughStaffForVehicle"

    override val map = "mapFiles/standard_map.dot"
    override val assets = "assetsJsons/not_enough_staff_for_vehicle_assets.json"
    override val scenario = "scenarioJsons/standard_scenario.json"

    override val maxTicks = NESFVMAXTICK
    override suspend fun run() {
        assertNextLine("Initialization Info: standard_map.dot successfully parsed and validated")
        assertNextLine("Initialization Info: not_enough_staff_for_vehicle_assets.json invalid")
        assertEnd()
    }
}
