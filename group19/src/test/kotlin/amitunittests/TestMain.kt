/* package amitunittests

import de.unisaarland.cs.se.selab.ECC
import de.unisaarland.cs.se.selab.EMCC
import de.unisaarland.cs.se.selab.Simulation
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.parser.DotParser
import de.unisaarland.cs.se.selab.parser.EmergencyEventParser
import de.unisaarland.cs.se.selab.parser.VehicleBaseParser
import org.junit.jupiter.api.Test

class TestMain {

    @Test
    fun test() {
        val dotParser = DotParser("src/systemtest/resources/mapFiles/amit_simple.dot")
        val assetsParser = VehicleBaseParser("src/systemtest/resources/assetsJsons/amit_simple.json")
        val scenarioParser = EmergencyEventParser("src/systemtest/resources/scenarioJsons/amit_simple2.json")

        dotParser.parseMap()
        val parsedBases = assetsParser.parseBases()
        val parsedVehicles = assetsParser.parseVehicles()
        val parsedEmergencies = scenarioParser.parseEmergencies()
        val parsedEvents = scenarioParser.parseEvents()

        val emcc = EMCC(parsedEmergencies, mutableListOf(), parsedBases, 0)
        val ecc = ECC(parsedEvents, parsedVehicles)

        val simulation = Simulation(emcc, ecc, 20)
        Navigation.initNavi(parsedBases)

        simulation.runSimulation()
    }

}
*/
