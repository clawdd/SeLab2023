package de.unisaarland.cs.se.selab.systemtest.keensystemtest
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest
const val PRIORITY_TICK = 9
const val KEY_REQUEST_ONE_F = "Request Failed: 1 failed."

/**
 * fire emergencyid 0 level 1 at tick 1,handle 1, max = 3, at road toC
 * fire emergencyd 1 level 3 at tick 2,handle 2, max = 5, at road toB
 * reallo happened, no enough vehicle, failed
 *
 */
class TriangleMyPriorityIsHigher : SystemTest() {

    override val name = "TriangleMyPriorityIsHigher"
    override val map = "mapFiles/triangleMaps/triangle_my_priority_is_higher.dot"
    override val assets = "assetsJsons/triangleassetJsons/triangle_asset_priority_higher.json"
    override val scenario = "scenarioJsons/triangle_scenario/triangle_scenario_priority_higher.json"
    override val maxTicks = PRIORITY_TICK

    override suspend fun run() {
        assertNextLine("Initialization Info: triangle_my_priority_is_higher.dot successfully parsed and validated")
        assertNextLine("Initialization Info: triangle_asset_priority_higher.json successfully parsed and validated")
        assertNextLine("Initialization Info: triangle_scenario_priority_higher.json successfully parsed and validated")
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 0 assigned to 1")
        assertNextLine("Asset Allocation: 0 allocated to 0; 2 ticks to arrive.")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Emergency Assignment: 1 assigned to 1")
        assertNextLine("Asset Allocation: 3 allocated to 1; 3 ticks to arrive.")
        assertNextLine("Asset Reallocation: 0 reallocated to 1.")
        assertNextLine("Asset Request: 1 sent to 2 for 1.")
        assertNextLine("Asset Allocation: 7 allocated to 1; 2 ticks to arrive.")
        assertNextLine("Asset Allocation: 8 allocated to 1; 2 ticks to arrive.")
        assertNextLine(KEY_REQUEST_ONE_F)
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Asset Request: 2 sent to 2 for 1.")
        assertNextLine(KEY_REQUEST_ONE_F)
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Asset Request: 3 sent to 2 for 1.")
        assertNextLine(KEY_REQUEST_ONE_F)
        assertNextLine("Asset Arrival: 0 arrived at 0.")
        assertNextLine("Asset Arrival: 7 arrived at 3.")
        assertNextLine("Asset Arrival: 8 arrived at 3.")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Asset Request: 4 sent to 2 for 1.")
        assertNextLine(KEY_REQUEST_ONE_F)
        assertNextLine("Asset Arrival: 3 arrived at 0.")
        assertNextLine("Emergency Failed: 0 failed.")
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Simulation Tick: 7")
        assertNextLine("Simulation Tick: 8")
        assertNextLine("Emergency Failed: 1 failed.")
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 2 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 2 failed emergencies.")
        assertNextLine("Simulation Statistics: 0 resolved emergencies.")
        assertEnd()
    }
}
