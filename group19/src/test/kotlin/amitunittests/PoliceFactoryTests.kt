
/*
Same issue as with the other vehicle factory tests.
package amitUnitTests
=======
package amitunittests
>>>>>>> f29b641d95d3cc031933a9b41d2703098bc686e4:src/test/kotlin/amitunittests/PoliceFactoryTests.kt

import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.vehicle.PoliceVehicle
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import de.unisaarland.cs.se.selab.vehicle.VehiclePoliceFactory
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PoliceFactoryTests {
    private val factory: VehiclePoliceFactory = VehiclePoliceFactory()

    @Test
    fun testPoliceCar() {
        val policeCar: PoliceVehicle = factory.createPoliceCar(VehicleIntParameters(1, 2, 3, 4), 2)

        assertNotNull(policeCar, "Object in null!")
        assertEquals(policeCar.vehicleType, VehicleType.POLICE_CAR, "Incorrect Vehicle Type!")

        assertEquals(policeCar.vehicleState, VehicleState.AT_BASE, "Incorrect Vehicle State!")
        assertEquals(policeCar.vehicleId, 1, "Incorrect VehicleId!")
        assertEquals(policeCar.homeBaseId, 2, "Incorrect Home Base ID!")
        assertEquals(policeCar.staffCapacity, 3, "Incorrect Staff Capacity!")
        assertEquals(policeCar.vehicleHeight, 4, "Incorrect Vehicle Height!")
        assertEquals(policeCar.criminalCapacity, 2, "Incorrect Criminal Capacity!")
        assertEquals(policeCar.dog, false, "Shouldn't contain dog!")
    }

    @Test
    fun testK9PoliceCar() {
        val policeCar: PoliceVehicle = factory.createK9PoliceCar(VehicleIntParameters(1, 2, 3, 4))

        assertNotNull(policeCar, "Object in null!")
        assertEquals(policeCar.vehicleType, VehicleType.K9_POLICE_CAR, "Incorrect Vehicle Type!")
        assertEquals(policeCar.vehicleState, VehicleState.AT_BASE, "Incorrect Vehicle State!")
        assertEquals(policeCar.vehicleId, 1, "Incorrect VehicleId!")
        assertEquals(policeCar.homeBaseId, 2, "Incorrect Home Base ID!")
        assertEquals(policeCar.staffCapacity, 3, "Incorrect Staff Capacity!")
        assertEquals(policeCar.vehicleHeight, 4, "Incorrect Vehicle Height!")
        assertEquals(policeCar.criminalCapacity, -1, "Incorrect Criminal Capacity!")
        assertEquals(policeCar.dog, true, "Should contain dog!")
    }

    @Test
    fun testPoliceMotorcycle() {
        val policeCar: PoliceVehicle = factory.createPoliceMotorcycle(VehicleIntParameters(1, 2, 3, 4))

        assertNotNull(policeCar, "Object in null!")
        assertEquals(policeCar.vehicleType, VehicleType.POLICE_MOTORCYCLE, "Incorrect Vehicle Type!")
        assertEquals(policeCar.vehicleState, VehicleState.AT_BASE, "Incorrect Vehicle State!")
        assertEquals(policeCar.vehicleId, 1, "Incorrect VehicleId!")
        assertEquals(policeCar.homeBaseId, 2, "Incorrect Home Base ID!")
        assertEquals(policeCar.staffCapacity, 3, "Incorrect Staff Capacity!")
        assertEquals(policeCar.vehicleHeight, 4, "Incorrect Vehicle Height!")
        assertEquals(policeCar.criminalCapacity, -1, "Incorrect Criminal Capacity!")
        assertEquals(policeCar.dog, false, "Shouldn't contain dog!")
    }
}
<<<<<<< HEAD:src/test/kotlin/amitUnitTests/PoliceFactoryTests.kt

 */
