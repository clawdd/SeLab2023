package de.unisaarland.cs.se.selab.systemtest.keensystemtest
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest
const val NO_DOCTOR_TICK = 5

/**
 * no emergency, no event triggered, just end...
 *
 */
class TriangleHospitalNoDoctor : SystemTest() {

    override val name = "TriangleHospitalNoDoctor"
    override val map = "mapFiles/triangleMaps/triangleMap.dot"
    override val assets = "assetsJsons/triangleassetJsons/triangle_hospital_no_doctor.json"
    override val scenario = "scenarioJsons/triangle_scenario/triangle_scenario_nothing_happened.json"
    override val maxTicks = NO_DOCTOR_TICK

    override suspend fun run() {
        assertNextLine("Initialization Info: triangleMap.dot successfully parsed and validated")
        assertNextLine("Initialization Info: triangle_hospital_no_doctor.json invalid")
        assertEnd()
    }
}
