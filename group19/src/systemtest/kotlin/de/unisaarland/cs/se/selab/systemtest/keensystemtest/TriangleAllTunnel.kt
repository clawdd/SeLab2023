package de.unisaarland.cs.se.selab.systemtest.keensystemtest
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest
const val ALL_TUNNEL_TICK = 8

/**
 * all the vehicles are too high for the tunnel
 * fire emergency id 0 and accident emergency id 1, both severity 1, cannot find road to go.
 */
class TriangleAllTunnel : SystemTest() {

    override val name = "TriangleAllTunnel"
    override val map = "mapFiles/triangleMaps/triangle_all_tunnel.dot"
    override val assets = "assetsJsons/triangleassetJsons/triangle_all_tunnel_high_vehicle.json"
    override val scenario = "scenarioJsons/triangle_scenario/triangle_scenario_all_tunnel.json"
    override val maxTicks = ALL_TUNNEL_TICK

    override suspend fun run() {
        assertNextLine("Initialization Info: triangle_all_tunnel.dot successfully parsed and validated")
        assertNextLine("Initialization Info: triangle_all_tunnel_high_vehicle.json successfully parsed and validated")
        assertNextLine(
            "Initialization Info: triangle_scenario_all_tunnel.json successfully parsed and validated"
        )
        assertNextLine("Simulation starts")
        assertNextLine("Simulation Tick: 0")
        assertNextLine("Simulation Tick: 1")
        assertNextLine("Emergency Assignment: 0 assigned to 1")
        assertNextLine("Emergency Assignment: 1 assigned to 1")
        assertNextLine("Simulation Tick: 2")
        assertNextLine("Simulation Tick: 3")
        assertNextLine("Simulation Tick: 4")
        assertNextLine("Simulation Tick: 5")
        assertNextLine("Simulation Tick: 6")
        assertNextLine("Simulation Tick: 7")
        assertNextLine("Emergency Failed: 0 failed.")
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
