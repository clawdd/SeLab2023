package maxunittests

import de.unisaarland.cs.se.selab.emergency.AccidentEmergencyFactory
import de.unisaarland.cs.se.selab.emergency.CrimeEmergencyFactory
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.emergency.FireEmergencyFactory
import de.unisaarland.cs.se.selab.emergency.MedicalEmergencyFactory
import de.unisaarland.cs.se.selab.enumtype.EmergencyState
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.PrimaryRoadType
import de.unisaarland.cs.se.selab.map.Road
import de.unisaarland.cs.se.selab.map.SecondaryRoadType
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.Vehicle
import de.unisaarland.cs.se.selab.vehicle.VehicleFireFactory
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import de.unisaarland.cs.se.selab.vehicle.VehicleMedicalFactory
import de.unisaarland.cs.se.selab.vehicle.VehiclePoliceFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EmergencyTests {

    val stateToID: MutableMap<Int, EmergencyState> = mutableMapOf()

    @BeforeTest
    fun setup() {
        val vertices: List<Vertex> = listOf(
            Vertex(0,),
            Vertex(1,)
        )

        val roads: List<Road> = listOf(
            Road(
                vertices[0],
                vertices[1],
                PrimaryRoadType.MAIN_STREET,
                SecondaryRoadType.NONE,
                "",
                "",
                5.0,
                5
            )
        )

        Map.setAttributes(vertices, roads, "")
    }

    @AfterTest
    fun afterTest() {
        stateToID.clear()
    }

    // --- infer required vehicles ---
    /**
     * all provided
     */
    @Test
    fun testInferRequiredVehiclesCrime1() {
        val assignedVehicleList: List<Vehicle> = List(1) {
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 1, 1, 1), 1)
        }

        val em: Emergency = CrimeEmergencyFactory().createCrimeLevelOne(listOf(1, 1, 1, 1), "", "")
        em.assignedVehicles.addAll(assignedVehicleList)
        assertEquals(0, em.inferRequiredVehicles().size)
    }

    /**
     * Missing:
     *  - Ambulance
     */
    @Test
    fun testInferRequiredVehiclesMedical2() {
        val em: Emergency = MedicalEmergencyFactory().createMedicalLevelTwo(
            listOf(1, 1, 1, 1),
            "",
            ""
        )

        val toAssign: List<Vehicle> = listOf(
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 1, 1, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(1, 1, 1, 1))
        )

        em.addToAssignedVehicles(toAssign)

        val result: List<Vehicle> = em.inferRequiredVehicles()
        assertEquals(1, result.size)
    }

    /**
     * Missing:
     *  - Technical Firetruck
     *  - 2 Police cars
     */
    @Test
    fun testInferRequiredVehicleAccident3() {
        val em: Emergency = AccidentEmergencyFactory().createAccidentLevelThree(
            listOf(
                1,
                1,
                1,
                1
            ),
            "",
            ""
        )

        val toAssign: List<Vehicle> = listOf(
            VehicleFireFactory().createFireTruckTechnical(VehicleIntParameters(1, 1, 1, 1)),
            VehicleFireFactory().createFireTruckTechnical(VehicleIntParameters(1, 1, 1, 1)),
            VehicleFireFactory().createFireTruckTechnical(VehicleIntParameters(1, 1, 1, 1)),

            VehiclePoliceFactory().createPoliceMotorcycle(VehicleIntParameters(1, 1, 1, 1)),
            VehiclePoliceFactory().createPoliceMotorcycle(VehicleIntParameters(1, 1, 1, 1)),

            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 1, 1, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 1, 1, 1), 1),

            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 1, 1, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 1, 1, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 1, 1, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(1, 1, 1, 1))
        )

        em.addToAssignedVehicles(toAssign)

        val result = em.inferRequiredVehicles()

        // result.map { println(it.vehicleType) }
        assertEquals(3, result.size)
    }

    @Test
    fun testInferRequiredVehicleAccident3RandomOrder() {
        val em: Emergency = AccidentEmergencyFactory().createAccidentLevelThree(
            listOf(
                1,
                1,
                1,
                1
            ),
            "",
            ""
        )

        val toAssign: List<Vehicle> = listOf(
            VehicleFireFactory().createFireTruckTechnical(VehicleIntParameters(1, 1, 1, 1)),
            VehicleFireFactory().createFireTruckTechnical(VehicleIntParameters(1, 1, 1, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 1, 1, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 1, 1, 1)),

            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 1, 1, 1), 1),
            VehiclePoliceFactory().createPoliceMotorcycle(VehicleIntParameters(1, 1, 1, 1)),
            VehiclePoliceFactory().createPoliceMotorcycle(VehicleIntParameters(1, 1, 1, 1)),

            VehicleFireFactory().createFireTruckTechnical(VehicleIntParameters(1, 1, 1, 1)),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 1, 1, 1), 1),

            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 1, 1, 1)),
            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(1, 1, 1, 1))
        )

        em.addToAssignedVehicles(toAssign)

        val result = em.inferRequiredVehicles()

        // result.map { println(it.vehicleType) }
        assertEquals(3, result.size)
    }

    // --- test compare() function

    /**
     * everything provided
     */
    @Test
    fun testCompareFire1() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 1, 1), "", "")

        val toAssign = listOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 500),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 700)
        )

        em.addToAssignedVehicles(toAssign)
        val result = em.compare()
        assertTrue(result)
    }

    /**
     * Missing:
     *  - 1 criminal capacity
     */
    @Test
    fun testCompareCrime2() {
        val em: Emergency = CrimeEmergencyFactory().createCrimeLevelTwo(listOf(1, 1, 1, 1), "", "")

        val toAssign = listOf(
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 1, 1, 1), 2),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 1, 1, 1), 1),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 1, 1, 1), 0),
            VehiclePoliceFactory().createPoliceCar(VehicleIntParameters(1, 1, 1, 1), 0),

            VehiclePoliceFactory().createK9PoliceCar(VehicleIntParameters(1, 1, 1, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 1, 1, 1))
        )

        em.addToAssignedVehicles(toAssign)
        val result = em.compare()
        assertFalse(result)
    }

    /**
     * Missing:
     * - enough water provided but 1 firetruck with water missing
     */
    @Test
    fun testCompareFire3() {
        val em: Emergency = FireEmergencyFactory().createFireLevelThree(listOf(1, 1, 1, 1), "", "")

        val toAssign = listOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 1000),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 1000),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 1000),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 1000),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 1400),

            VehicleFireFactory().createFireTruckLadder(VehicleIntParameters(1, 1, 1, 1), 40),
            VehicleFireFactory().createFireTruckLadder(VehicleIntParameters(1, 1, 1, 1), 40),

            VehicleFireFactory().createFirefigherTransporter(VehicleIntParameters(1, 1, 1, 1)),
            VehicleFireFactory().createFirefigherTransporter(VehicleIntParameters(1, 1, 1, 1)),

            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 1, 1, 1)),
            VehicleMedicalFactory().createAmbulance(VehicleIntParameters(1, 1, 1, 1)),

            VehicleMedicalFactory().createDoctorCar(VehicleIntParameters(1, 1, 1, 1))
        )

        em.addToAssignedVehicles(toAssign)
        val result = em.compare()
        assertFalse(result)
    }

    // --- test updateEmergencyStatus() function
    // 1. testing PENDING State

    @Test
    fun testPENDINGtoPENDING() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 5, 6), "", "")

        em.updateEmergencyStatus(4, stateToID)
        assertEquals(EmergencyState.PENDING, em.emergencyState)
    }

    @Test
    fun testPENDINGtoFAIL() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 5, 6), "", "")

        em.updateEmergencyStatus(6, stateToID)
        assertEquals(EmergencyState.PENDING, em.emergencyState)
        em.updateEmergencyStatus(7, stateToID)
        assertEquals(EmergencyState.PENDING, em.emergencyState)
        em.updateEmergencyStatus(8, stateToID)
        assertEquals(EmergencyState.FAIL, em.emergencyState)
    }

    @Test
    fun testPENDINGtoALL_ASSETS_ASSIGNED() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 5, 6), "", "")
        val toAssign = listOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 500),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 700)
        )
        em.updateEmergencyStatus(6, stateToID)
        assertEquals(EmergencyState.PENDING, em.emergencyState)
        em.addToAssignedVehicles(toAssign)
        em.updateEmergencyStatus(7, stateToID)
        assertEquals(EmergencyState.ALL_ASSETS_ASSIGNED, em.emergencyState)
    }

    //  2. ALL_ASSETS_ASSIGNED

    @Test
    fun testALL_ASSETS_ASSIGNEDtoALL_ASSETS_ASSIGNED() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 5, 6), "", "")
        val toAssign = listOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 500),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 700)
        )
        em.updateEmergencyStatus(5, stateToID)
        assertEquals(EmergencyState.PENDING, em.emergencyState)
        em.addToAssignedVehicles(toAssign)
        em.updateEmergencyStatus(6, stateToID)
        assertEquals(EmergencyState.ALL_ASSETS_ASSIGNED, em.emergencyState)
        em.updateEmergencyStatus(7, stateToID)
        assertEquals(EmergencyState.ALL_ASSETS_ASSIGNED, em.emergencyState)
    }

    @Test
    fun testALL_ASSETS_ASSIGNEDtoFAIL() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 5, 6), "", "")
        em.emergencyState = EmergencyState.ALL_ASSETS_ASSIGNED
        em.updateEmergencyStatus(8, stateToID)
        assertEquals(EmergencyState.FAIL, em.emergencyState)
    }

    @Test
    fun testALL_ASSETS_ASSIGNEDtoPENDING() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 5, 6), "", "")
        val toAssign = listOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 500),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 700)
        )
        em.updateEmergencyStatus(5, stateToID)
        assertEquals(EmergencyState.PENDING, em.emergencyState)
        em.addToAssignedVehicles(toAssign)
        em.updateEmergencyStatus(6, stateToID)
        assertEquals(EmergencyState.ALL_ASSETS_ASSIGNED, em.emergencyState)
        em.assignedVehicles.removeAt(0)
        em.updateEmergencyStatus(7, stateToID)
        assertEquals(EmergencyState.PENDING, em.emergencyState)
    }

    @Test
    fun testALL_ASSETS_ASSIGNEDtoCURRENTLY_BEING_HANDLED() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 5, 6), "", "")
        val toAssign = listOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 500),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 700)
        )
        em.updateEmergencyStatus(5, stateToID)
        assertEquals(EmergencyState.PENDING, em.emergencyState)
        em.addToAssignedVehicles(toAssign)
        em.updateEmergencyStatus(6, stateToID)
        assertEquals(EmergencyState.ALL_ASSETS_ASSIGNED, em.emergencyState)
        em.assignedVehicles.map { it.vehicleState = VehicleState.AT_EMERGENCY }
        em.updateEmergencyStatus(7, stateToID)
        assertEquals(EmergencyState.CURRENTLY_BEING_HANDLED, em.emergencyState)
    }

    //  3. CURRENTLY_BEING_HANDLED

    @Test
    fun testCURRENTLY_BEING_HANDLEDtoCURRENTLY_BEING_HANDLED() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 5, 6), "", "")
        val toAssign = listOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 500),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 700)
        )
        em.addToAssignedVehicles(toAssign)
        em.updateEmergencyStatus(5, stateToID)
        assertEquals(EmergencyState.ALL_ASSETS_ASSIGNED, em.emergencyState)
        em.assignedVehicles.map { it.vehicleState = VehicleState.AT_EMERGENCY }
        em.updateEmergencyStatus(6, stateToID)
        assertEquals(EmergencyState.CURRENTLY_BEING_HANDLED, em.emergencyState)
        em.updateEmergencyStatus(7, stateToID)
        assertEquals(EmergencyState.CURRENTLY_BEING_HANDLED, em.emergencyState)
    }

    @Test
    fun testCURRENTLY_BEING_HANDLEDtoFAIL() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 5, 6), "", "")

        em.emergencyState = EmergencyState.CURRENTLY_BEING_HANDLED
        em.updateEmergencyStatus(8, stateToID)
        assertEquals(EmergencyState.FAIL, em.emergencyState)
    }

    @Test
    fun testHandleTimeUpdate() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 5, 6), "", "")
        val toAssign = listOf(
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 500),
            VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 1, 1, 1), 700)
        )
        em.addToAssignedVehicles(toAssign)
        em.updateEmergencyStatus(2, stateToID)
        assertEquals(EmergencyState.ALL_ASSETS_ASSIGNED, em.emergencyState)
        em.assignedVehicles.map { it.vehicleState = VehicleState.AT_EMERGENCY }
        em.updateEmergencyStatus(3, stateToID)
        assertEquals(EmergencyState.CURRENTLY_BEING_HANDLED, em.emergencyState)

        // first actual tick it gets handled
        em.updateEmergencyStatus(4, stateToID)
        assertEquals(EmergencyState.CURRENTLY_BEING_HANDLED, em.emergencyState)
        assertEquals(4, em.handleTime)
    }

    @Test
    fun testCURRENTLY_BEING_HANDLEDtoSUCCESS() {
        val em = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 5, 6), "", "")

        em.emergencyState = EmergencyState.CURRENTLY_BEING_HANDLED
        // println(em.handleTime)
        // first tick being handled - HandleTime = 5
        em.updateEmergencyStatus(2, stateToID)
        assertEquals(EmergencyState.CURRENTLY_BEING_HANDLED, em.emergencyState)
        // HandleTime = 4
        em.updateEmergencyStatus(3, stateToID)
        assertEquals(EmergencyState.CURRENTLY_BEING_HANDLED, em.emergencyState)
        // HandleTime = 3
        em.updateEmergencyStatus(4, stateToID)
        assertEquals(EmergencyState.CURRENTLY_BEING_HANDLED, em.emergencyState)
        // HandleTime = 2
        em.updateEmergencyStatus(5, stateToID)
        assertEquals(EmergencyState.CURRENTLY_BEING_HANDLED, em.emergencyState)
        // HandleTime = 1
        em.updateEmergencyStatus(6, stateToID)
        assertEquals(EmergencyState.SUCCESSFUL, em.emergencyState)
        // HandleTime = 0 -> therefore successful
    }
}
