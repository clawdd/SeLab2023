package de.unisaarland.cs.se.selab.systemtest.janossystemtests.parsertests.logicerrors

import de.unisaarland.cs.se.selab.systemtest.api.SystemTest

class TunnelLargerThree : SystemTest() {
    override val name = "TunnelLargerThree"

    override val map = "mapFiles/invalidMaps/tunnel_larger_three.dot"
    override val assets = "assetsJsons/easy_example.json"
    override val scenario = "scenarioJsons/easy_example_eventEM.json"

    override val maxTicks = 0

    override suspend fun run() {
        assertNextLine("Initialization Info: tunnel_larger_three.dot invalid")
        assertEnd()
    }
}
