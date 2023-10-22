package maxunittests

import de.unisaarland.cs.se.selab.vehicle.FireVehicle
import de.unisaarland.cs.se.selab.vehicle.MedicalVehicle
import de.unisaarland.cs.se.selab.vehicle.PoliceVehicle
import de.unisaarland.cs.se.selab.vehicle.VehicleFireFactory
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import de.unisaarland.cs.se.selab.vehicle.VehicleMedicalFactory
import de.unisaarland.cs.se.selab.vehicle.VehiclePoliceFactory
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

/**
 * TODO
 *
 */
class MaxVehicleTests {

    /**
     * TODO
     *
     */
    @Test
    fun testCreateFireVehicle() {
        val vehicle: FireVehicle = VehicleFireFactory().createFireTruckWater(
            VehicleIntParameters(
                0,
                123,
                6,
                2
            ),
            600
        )

        assertEquals(0, vehicle.vehicleId)
        assertEquals(123, vehicle.homeBaseId)
        assertEquals(6, vehicle.staffCapacity)
        assertEquals(2, vehicle.vehicleHeight)
        assertEquals(600, vehicle.waterCapacity)
    }

    /**
     * TODO
     *
     */
    @Test
    fun testCreateMedicalVehicel() {
        val vehicle: MedicalVehicle = VehicleMedicalFactory().createDoctorCar(
            VehicleIntParameters(
                0,
                123,
                6,
                2
            )
        )

        assertEquals(0, vehicle.vehicleId)
        assertEquals(123, vehicle.homeBaseId)
        assertEquals(6, vehicle.staffCapacity)
        assertEquals(2, vehicle.vehicleHeight)
    }

    @Test
    fun testCreatePoliceVehicle() {
        val vehicle: PoliceVehicle = VehiclePoliceFactory().createPoliceMotorcycle(
            VehicleIntParameters(
                0,
                123,
                6,
                2
            )
        )

        assertEquals(0, vehicle.vehicleId)
        assertEquals(123, vehicle.homeBaseId)
        assertEquals(6, vehicle.staffCapacity)
        assertEquals(2, vehicle.vehicleHeight)
    }
}
