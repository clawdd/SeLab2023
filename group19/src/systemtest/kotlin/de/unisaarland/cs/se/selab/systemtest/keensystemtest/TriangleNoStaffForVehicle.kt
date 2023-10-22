package de.unisaarland.cs.se.selab.systemtest.keensystemtest

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

const val NO_STAFF = 9
const val KEY_R_FIVE_F = "Request Failed: 5 failed."

/**
 * give crimeid 4, level 1, at tick 1, at toA handle=1,maxD=4
 * give crimeid 5, level 2, at tick 2, at AB handle=2,maxD=4
 * cannot fulfill since both of them been assigned to the same base
 * with only enough staffs for at most 3 vehicles
 */
class TriangleNoStaffForVehicle : SystemTest() {
    override val name = "TriangleNoStaffForVehicle"
    override val map = "mapFiles/triangleMaps/triangle_no_staff_for_vehicle.dot"
    override val assets = "assetsJsons/triangleassetJsons/triangle_asset_no_staff_for_vehicle.json"
    override val scenario =
        "scenarioJsons/triangle_scenario/triangle_scenario_no_staff_for_vehicle.json"
    override val maxTicks = NO_STAFF

    override suspend fun run() {
        assertNextLine("Initialization Info: triangle_no_staff_for_vehicle.dot successfully parsed and validated")
        assertNextLine(
            "Initialization Info: triangle_asset_no_staff_for_vehicle.json successfully parsed and validated"
        )
        assertNextLine(
            "Initialization Info: triangle_scenario_no_staff_for_vehicle.json successfully parsed and validated"
        )
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 4 assigned to 3")
        assertNextLine("Asset Allocation: 4 allocated to 4; 3 ticks to arrive.")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Emergency Assignment: 5 assigned to 3")
        assertNextLine("Asset Allocation: 5 allocated to 5; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 9 allocated to 5; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 10 allocated to 5; 2 ticks to arrive.")
        assertNextLine("Asset Reallocation: 4 reallocated to 5.")
        assertNextLine("Asset Request: 1 sent to 2 for 5.")
        assertNextLine("Asset Allocation: 7 allocated to 5; 1 ticks to arrive.")
        assertNextLine("Asset Request: 2 sent to 4 for 5.")
        assertNextLine(KEY_R_FIVE_F)
        assertNextLine(KEY_R_FIVE_F)
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Asset Request: 3 sent to 4 for 5.")
        assertNextLine(KEY_R_FIVE_F)
        assertNextLine("Asset Arrival: 4 arrived at 2.")
        assertNextLine("Asset Arrival: 7 arrived at 2.")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Asset Request: 4 sent to 4 for 5.")
        assertNextLine(KEY_R_FIVE_F)
        assertNextLine("Asset Arrival: 5 arrived at 2.")
        assertNextLine("Asset Arrival: 9 arrived at 2.")
        assertNextLine("Asset Arrival: 10 arrived at 2.")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Emergency Failed: 4 failed.")
        assertNextLine("Simulation Tick: 7")
        assertNextLine("Emergency Failed: 5 failed.")
        assertNextLine("Simulation Tick: 8")
        assertNextLine("Asset Arrival: 7 arrived at 2.")
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 2 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 2 failed emergencies.")
        assertNextLine("Simulation Statistics: 0 resolved emergencies.")
        assertEnd()
    }
}
