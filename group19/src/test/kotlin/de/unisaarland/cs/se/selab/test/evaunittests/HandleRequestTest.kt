/* package de.unisaarland.cs.se.selab.test.evaunittests

import de.unisaarland.cs.se.selab.base.BaseFactory
import de.unisaarland.cs.se.selab.base.FireStation
import de.unisaarland.cs.se.selab.base.Request
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.emergency.FireEmergencyFactory
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.PrimaryRoadType
import de.unisaarland.cs.se.selab.map.Road
import de.unisaarland.cs.se.selab.map.SecondaryRoadType
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.FireVehicle
import de.unisaarland.cs.se.selab.vehicle.VehicleFireFactory
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class HandleRequestTest {
    val baseVertex = Vertex(0)
    val emergencyVertex = Vertex(1)
    lateinit var fireStationCoordinator: FireStation
    lateinit var fireStationHelper: FireStation
    lateinit var ftw1: FireVehicle
    lateinit var ftw2: FireVehicle
    lateinit var ftw3: FireVehicle
    lateinit var ftw4: FireVehicle
    lateinit var ftt1: FireVehicle
    lateinit var ftt2: FireVehicle
    lateinit var r1: Request
    lateinit var r2: Request

    @BeforeTest
    fun setup() {
        fireStationCoordinator = BaseFactory().createFireStation(
            1,
            Vertex(1),
            20
        )
        fireStationHelper = BaseFactory().createFireStation(
            2,
            baseVertex,
            22
        )
        r1 = Request(e1, 123, fireStationCoordinator)
        r2 = Request(e2, 124, fireStationCoordinator)

        val vertices: List<Vertex> = listOf(
            emergencyVertex,
            baseVertex
        )

        val roads: List<Road> = listOf(
            Road(
                baseVertex,
                emergencyVertex,
                PrimaryRoadType.MAIN_STREET,
                SecondaryRoadType.NONE,
                "",
                "",
                5.0,
                5
            )
        )

        Map.setAttributes(vertices, roads, "")
        Map.updateAdjacencyMatrix()
        baseVertex.calculateConnectedVertices()
        emergencyVertex.calculateConnectedVertices()
        // vehicleId, homeBaseId, staffCapacity, VehicleHeight
        ftw1 = VehicleFireFactory().createFireTruckWater(VehicleIntParameters(1, 2, 7, 1), 600)
        ftw2 = VehicleFireFactory().createFireTruckWater(VehicleIntParameters(2, 2, 3, 1), 600)
        ftw3 = VehicleFireFactory().createFireTruckWater(VehicleIntParameters(3, 2, 100, 1), 600)
        ftw4 = VehicleFireFactory().createFireTruckWater(VehicleIntParameters(4, 2, 100, 1), 600)
        ftt1 = VehicleFireFactory().createFireTruckTechnical(VehicleIntParameters(5, 2, 1, 7))
        ftt2 = VehicleFireFactory().createFireTruckTechnical(VehicleIntParameters(6, 2, 1, 4))
    }

    val e1: Emergency = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 1, 1), "", "")
    val e2: Emergency = FireEmergencyFactory().createFireLevelOne(listOf(2, 1, 1, 1), "", "")

    @Test
    fun simpleHandleRequestTest() {
        assertEquals(0, r1.emergency.assignedVehicles.size)
        fireStationHelper.handleRequest(r1)
        assertEquals(0, r1.emergency.assignedVehicles.size)
    }

    @Test
    fun slightlyMoreComplicatedHandleRequestTest() {
        ftw1.currentLocation = baseVertex
        ftw2.currentLocation = baseVertex
        fireStationHelper.assignedVehicles.add(ftw1)
        fireStationHelper.assignedVehicles.add(ftw2)
        assertEquals(0, r1.emergency.assignedVehicles.size)
        fireStationHelper.handleRequest(r1)
        assertEquals(2, r1.emergency.assignedVehicles.size)
    }

    @Test
    fun cannotHandleRequestWithoutWater() {
        ftt1.currentLocation = baseVertex
        ftt2.currentLocation = baseVertex
        fireStationHelper.assignedVehicles.add(ftt1)
        fireStationHelper.assignedVehicles.add(ftt2)
        assertEquals(0, r1.emergency.assignedVehicles.size)
        fireStationHelper.handleRequest(r1)
        // The two vehicles were not assigned, they have the wrong type
        assertEquals(0, r1.emergency.assignedVehicles.size)
    }

    @Test
    fun tooLittleStaffAvailableTest() {
        ftw3.currentLocation = baseVertex
        ftw4.currentLocation = baseVertex
        fireStationHelper.assignedVehicles.add(ftw3)
        fireStationHelper.assignedVehicles.add(ftw4)
        assertEquals(0, r1.emergency.assignedVehicles.size)
        fireStationHelper.handleRequest(r1)
        assertEquals(0, r1.emergency.assignedVehicles.size)
    }

    @Test
    fun areVehiclesReducedAfterRequestTest() {
        ftw1.currentLocation = baseVertex
        ftw2.currentLocation = baseVertex
        ftw3.currentLocation = baseVertex
        ftw4.currentLocation = baseVertex
        fireStationHelper.assignedVehicles.add(ftw1)
        fireStationHelper.assignedVehicles.add(ftw2)
        fireStationHelper.assignedVehicles.add(ftw3)
        fireStationHelper.assignedVehicles.add(ftw4)
        assertEquals(listOf(ftw1, ftw2, ftw3, ftw4), fireStationHelper.assignedVehicles)
        fireStationHelper.handleRequest(r1)
        assertEquals(
            listOf(ftw3, ftw4),
            fireStationHelper.assignedVehicles.filter { it.vehicleState == VehicleState.AT_BASE }
        )
        assertEquals(2, r1.emergency.assignedVehicles.size)
        fireStationHelper.handleRequest(r2)
        assertEquals(
            listOf(ftw3, ftw4),
            fireStationHelper.assignedVehicles.filter { it.vehicleState == VehicleState.AT_BASE }
        )
        assertEquals(0, r2.emergency.assignedVehicles.size)
    }

    /**
     * Do nothing test
     */
}
*/
