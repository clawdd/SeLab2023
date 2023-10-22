
/*
Same issue as with the firetruck tests, check the topmost comment for an explanation

package amitUnitTests
=======
package amitunittests
src/test/kotlin/amitunittests/MedicalFactoryTests.kt

import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.vehicle.MedicalVehicle
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import de.unisaarland.cs.se.selab.vehicle.VehicleMedicalFactory
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MedicalFactoryTests {
    private val factory: VehicleMedicalFactory = VehicleMedicalFactory()

    @Test
    fun testAmbulance() {
        val ambulance: MedicalVehicle = factory.createAmbulance(VehicleIntParameters(1, 2, 3, 4))

        assertNotNull(ambulance, "Object in null!")
        assertEquals(ambulance.vehicleType, VehicleType.AMBULANCE, "Incorrect Vehicle Type!")
        assertEquals(ambulance.vehicleState, VehicleState.AT_BASE, "Incorrect Vehicle State!")
        assertEquals(ambulance.vehicleId, 1, "Incorrect VehicleId!")
        assertEquals(ambulance.homeBaseId, 2, "Incorrect Home Base ID!")
        assertEquals(ambulance.staffCapacity, 3, "Incorrect Staff Capacity!")
        assertEquals(ambulance.vehicleHeight, 4, "Incorrect Vehicle Height!")
        assertEquals(ambulance.patient, false, "Shouldn't contain patient!")
        assertEquals(ambulance.doctor, false, "Shouldn't contain doctor!")
    }

    @Test
    fun testDoctorCar() {
        val doctCar: MedicalVehicle = factory.createDoctorCar(VehicleIntParameters(1, 2, 3, 4))

        assertNotNull(doctCar, "Object in null!")
        assertEquals(doctCar.vehicleType, VehicleType.EMERGENCY_DOCTOR_CAR, "Incorrect Vehicle Type!")
        assertEquals(doctCar.vehicleState, VehicleState.AT_BASE, "Incorrect Vehicle State!")
        assertEquals(doctCar.vehicleId, 1, "Incorrect VehicleId!")
        assertEquals(doctCar.homeBaseId, 2, "Incorrect Home Base ID!")
        assertEquals(doctCar.staffCapacity, 3, "Incorrect Staff Capacity!")
        assertEquals(doctCar.vehicleHeight, 4, "Incorrect Vehicle Height!")
        assertEquals(doctCar.patient, false, "Shouldn't contain patient!")
        assertEquals(doctCar.doctor, true, "Doesn't contain doctor!")
    }
}*/
