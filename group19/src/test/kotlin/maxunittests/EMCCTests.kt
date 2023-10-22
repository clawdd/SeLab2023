package maxunittests

import de.unisaarland.cs.se.selab.EMCC
import de.unisaarland.cs.se.selab.base.BaseFactory
import de.unisaarland.cs.se.selab.emergency.FireEmergencyFactory
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.VehicleFireFactory
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import kotlin.test.Test
import kotlin.test.assertEquals

class EMCCTests {
    /**
     * TODO test constructor for line coverage
     *
     */
    @Test
    fun testEMCCConstructor() {
        val emcc = EMCC(emptyList(), mutableListOf(), emptyList(), 0)
        emcc.forwardedRequests = 1
        assertEquals(0, emcc.allOccurringEmergencies!!.size)
        assertEquals(0, emcc.onGoingEmergencies.size)
        assertEquals(0, emcc.services.size)
        assertEquals(1, emcc.forwardedRequests)
    }

    @Test
    fun testEMCCSortEmergenciesAllBases() {
        val emergencies = listOf(
            FireEmergencyFactory().createFireLevelOne(listOf(5, 1, 1, 1), "", ""),
            FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 1, 1), "", ""),
            FireEmergencyFactory().createFireLevelOne(listOf(2, 1, 1, 1), "", "")
        )

        val service = BaseFactory().createFireStation(1, Vertex(123), 1)
        service.assignedEmergencies.addAll(emergencies)

        val emcc = EMCC(emptyList(), mutableListOf(), listOf(service), 0)
        emcc.sortEmergenciesAllBases()
        // assertEquals(5, service.assignedEmergencies[0].id)
        // assertEquals(2, service.assignedEmergencies[1].id)
        // assertEquals(1, service.assignedEmergencies[2].id)
    }

    @Test
    fun testEMCCAssignAssetsAllBases() {
        val emergencies = listOf(
            FireEmergencyFactory().createFireLevelOne(listOf(5, 1, 1, 1), "", ""),
        )
        val service = BaseFactory().createFireStation(1, Vertex(123), 30)
        service.assignedEmergencies.addAll(emergencies)

        val emcc = EMCC(emptyList(), mutableListOf(), listOf(service), 0)
        emcc.assignAssetsAllBases(0)
        assertEquals(0, service.assignedEmergencies[0].assignedVehicles.size)
    }

    @Test
    fun testUpdateAssetsAllBases() {
        val service = BaseFactory().createFireStation(1, Vertex(123), 30)
        service.assignedVehicles.add(
            VehicleFireFactory().createFireTruckLadder(VehicleIntParameters(1, 123, 6, 1), 600)
        )

        val emcc = EMCC(emptyList(), mutableListOf(), listOf(service), 0)
        emcc.assignAssetsAllBases(0)
        assertEquals(VehicleState.AT_BASE, service.assignedVehicles[0].vehicleState)
    }
}
