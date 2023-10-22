package de.unisaarland.cs.se.selab.systemtest.keensystemtest
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest
const val NOT_ENOUGH_VEHICLE_TICK = 8
const val KEY_REQUEST_FAILED_ZERO = "Request Failed: 0 failed."

/**
 * 1 fire emergency with severity 3 happened at tick 1,
 * but not enough car for that. no reallo, just request and request failed,end
 */
class TriangleNotEnoughVehicles : SystemTest() {

    override val name = "TriangleNotEnoughVehicles"
    override val map = "mapFiles/triangleMaps/triangleMap.dot"
    override val assets = "assetsJsons/triangleassetJsons/triangle_asset_nothing_happened.json"
    override val scenario = "scenarioJsons/triangle_scenario/triangle_scenario_not_enough_vehicle.json"
    override val maxTicks = NOT_ENOUGH_VEHICLE_TICK

    override suspend fun run() {
        assertNextLine("Initialization Info: triangleMap.dot successfully parsed and validated")
        assertNextLine("Initialization Info: triangle_asset_nothing_happened.json successfully parsed and validated")
        assertNextLine(
            "Initialization Info: triangle_scenario_not_enough_vehicle.json successfully parsed and validated"
        )
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 0 assigned to 1")
        assertNextLine("Asset Allocation: 0 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 3 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Asset Request: 1 sent to 2 for 0.")
        assertNextLine("Asset Allocation: 7 allocated to 0; 1 ticks to arrive.")
        assertNextLine("Asset Allocation: 8 allocated to 0; 1 ticks to arrive.")
        assertNextLine(KEY_REQUEST_FAILED_ZERO)
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Asset Request: 2 sent to 2 for 0.")
        assertNextLine(KEY_REQUEST_FAILED_ZERO)
        assertNextLine("Asset Arrival: 0 arrived at 1.")
        assertNextLine("Asset Arrival: 3 arrived at 1.")
        assertNextLine("Asset Arrival: 7 arrived at 1.")
        assertNextLine("Asset Arrival: 8 arrived at 1.")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Asset Request: 3 sent to 2 for 0.")
        assertNextLine(KEY_REQUEST_FAILED_ZERO)
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Asset Request: 4 sent to 2 for 0.")
        assertNextLine(KEY_REQUEST_FAILED_ZERO)
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Asset Request: 5 sent to 2 for 0.")
        assertNextLine(KEY_REQUEST_FAILED_ZERO)
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Simulation Tick: 7")
        assertNextLine("Emergency Failed: 0 failed.")
        assertNextLine("Simulation End")
        assertNextLine("Simulation Statistics: 0 assets rerouted.")
        assertNextLine("Simulation Statistics: 1 received emergencies.")
        assertNextLine("Simulation Statistics: 0 ongoing emergencies.")
        assertNextLine("Simulation Statistics: 1 failed emergencies.")
        assertNextLine("Simulation Statistics: 0 resolved emergencies.")
        assertEnd()
    }
}
