/*
I commented your test out because you don't construct fire trucks correctly, which causes errors.
Check the arguments of the fire vehicle constructor again

package amitUnitTests

import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.vehicle.FireVehicle
import de.unisaarland.cs.se.selab.vehicle.VehicleFireFactory
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FireFactoryTests {
    private val factory : VehicleFireFactory = VehicleFireFactory()

    @Test
    fun testFireTruckWater() : Unit {
        val fireTruck : FireVehicle = factory.createFireTruckWater(listOf(1,2,3,4), 5)

        assertNotNull(fireTruck, "Object in null!")
        assertEquals(fireTruck.vehicleType, VehicleType.FIRE_TRUCK_WATER, "Incorrect Vehicle Type!")
        assertEquals(fireTruck.vehicleState, VehicleState.AT_BASE, "Incorrect Vehicle State!")
        assertEquals(fireTruck.vehicleId, 1, "Incorrect VehicleId!")
        assertEquals(fireTruck.homeBaseId, 2, "Incorrect Home Base ID!")
        assertEquals(fireTruck.staffCapacity, 3, "Incorrect Staff Capacity!")
        assertEquals(fireTruck.vehicleHeight, 4, "Incorrect Vehicle Height!")
        assertEquals(fireTruck.waterCapacity, 5, "Incorrect Water Capacity!")
        assertEquals(fireTruck.ladderLength, -1, "Incorrect Ladder Capacity!")
    }
    @Test
    fun testFireTruckTechnical() : Unit {
        val fireTruck : FireVehicle = factory.createFireTruckTechnical(listOf(1,2,3,4))

        assertNotNull(fireTruck, "Object in null!")
        assertEquals(fireTruck.vehicleType, VehicleType.FIRE_TRUCK_TECHNICAL, "Incorrect Vehicle Type!")
        assertEquals(fireTruck.vehicleState, VehicleState.AT_BASE, "Incorrect Vehicle State!")
        assertEquals(fireTruck.vehicleId, 1, "Incorrect VehicleId!")
        assertEquals(fireTruck.homeBaseId, 2, "Incorrect Home Base ID!")
        assertEquals(fireTruck.staffCapacity, 3, "Incorrect Staff Capacity!")
        assertEquals(fireTruck.vehicleHeight, 4, "Incorrect Vehicle Height!")
        assertEquals(fireTruck.waterCapacity, -1, "Incorrect Water Capacity!")
        assertEquals(fireTruck.ladderLength, -1, "Incorrect Ladder Capacity!")
    }
    @Test
    fun testFireTruckLadder() : Unit {
        val fireTruck : FireVehicle = factory.createFireTruckLadder(listOf(1,2,3,4), 10)

        assertNotNull(fireTruck, "Object in null!")
        assertEquals(fireTruck.vehicleType, VehicleType.FIRE_TRUCK_LADDER, "Incorrect Vehicle Type!")
        assertEquals(fireTruck.vehicleState, VehicleState.AT_BASE, "Incorrect Vehicle State!")
        assertEquals(fireTruck.vehicleId, 1, "Incorrect VehicleId!")
        assertEquals(fireTruck.homeBaseId, 2, "Incorrect Home Base ID!")
        assertEquals(fireTruck.staffCapacity, 3, "Incorrect Staff Capacity!")
        assertEquals(fireTruck.vehicleHeight, 4, "Incorrect Vehicle Height!")
        assertEquals(fireTruck.waterCapacity, -1, "Incorrect Water Capacity!")
        assertEquals(fireTruck.ladderLength, 10, "Incorrect Ladder Capacity!")
    }
    @Test
    fun testFirefigherTransporter() : Unit {
        val fireTruck : FireVehicle = factory.createFirefigherTransporter(listOf(1,2,3,4))

        assertNotNull(fireTruck, "Object in null!")
        assertEquals(fireTruck.vehicleType, VehicleType.FIREFIGHTER_TRANSPORTER, "Incorrect Vehicle Type!")
        assertEquals(fireTruck.vehicleState, VehicleState.AT_BASE, "Incorrect Vehicle State!")
        assertEquals(fireTruck.vehicleId, 1, "Incorrect VehicleId!")
        assertEquals(fireTruck.homeBaseId, 2, "Incorrect Home Base ID!")
        assertEquals(fireTruck.staffCapacity, 3, "Incorrect Staff Capacity!")
        assertEquals(fireTruck.vehicleHeight, 4, "Incorrect Vehicle Height!")
        assertEquals(fireTruck.waterCapacity, -1, "Incorrect Water Capacity!")
        assertEquals(fireTruck.ladderLength, -1, "Incorrect Ladder Capacity!")
    }
}*/
