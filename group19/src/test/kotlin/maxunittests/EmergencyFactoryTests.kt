package maxunittests

import de.unisaarland.cs.se.selab.emergency.AccidentEmergencyFactory
import de.unisaarland.cs.se.selab.emergency.CrimeEmergencyFactory
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.emergency.FireEmergencyFactory
import de.unisaarland.cs.se.selab.emergency.MedicalEmergencyFactory
import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

/**
 * TODO
 *
 */
class EmergencyFactoryTests {
    // --- helper methods ---

    private fun countVehicleType(e: Emergency): Map<VehicleType?, Int> {
        return e.requiredVehicles
            .groupingBy { it.vehicleType }
            .eachCount()
    }

    // --- Fire Emergency Tests ---
    @Test
    fun testFireEmergencyLevelOne() {
        val fe1: Emergency = FireEmergencyFactory().createFireLevelOne(listOf(1, 1, 1, 1), "", "")
        assertEquals(EmergencyType.FIRE, fe1.emergencyType)
        assertEquals(1, fe1.severity)
        assertEquals(1200, fe1.requiredWater)
        assertEquals(2, fe1.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(fe1)

        assertEquals(2, vehicleTypeCounts[VehicleType.FIRE_TRUCK_WATER])
    }

    @Test
    fun testFireEmergencyLevelTwo() {
        val fe2: Emergency = FireEmergencyFactory().createFireLevelTwo(listOf(1, 1, 1, 1), "", "")

        assertEquals(EmergencyType.FIRE, fe2.emergencyType)
        assertEquals(2, fe2.severity)
        assertEquals(3000, fe2.requiredWater)
        assertEquals(1, fe2.requiredPatients)
        assertEquals(7, fe2.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(fe2)

        assertEquals(4, vehicleTypeCounts[VehicleType.FIRE_TRUCK_WATER])
        assertEquals(1, vehicleTypeCounts[VehicleType.FIRE_TRUCK_LADDER])
        assertEquals(1, vehicleTypeCounts[VehicleType.FIREFIGHTER_TRANSPORTER])
        assertEquals(1, vehicleTypeCounts[VehicleType.AMBULANCE])
    }

    @Test
    fun testFireEmergencyLevelThree() {
        val fe3: Emergency = FireEmergencyFactory().createFireLevelThree(listOf(1, 1, 1, 1), "", "")

        assertEquals(EmergencyType.FIRE, fe3.emergencyType)
        assertEquals(3, fe3.severity)
        assertEquals(5400, fe3.requiredWater)
        assertEquals(2, fe3.requiredPatients)
        assertEquals(13, fe3.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(fe3)

        assertEquals(6, vehicleTypeCounts[VehicleType.FIRE_TRUCK_WATER])
        assertEquals(2, vehicleTypeCounts[VehicleType.FIRE_TRUCK_LADDER])
        assertEquals(2, vehicleTypeCounts[VehicleType.FIREFIGHTER_TRANSPORTER])
        assertEquals(2, vehicleTypeCounts[VehicleType.AMBULANCE])
        assertEquals(1, vehicleTypeCounts[VehicleType.EMERGENCY_DOCTOR_CAR])
    }

    // --- Crime Emergency Tests
    @Test
    fun testCrimeEmergencyLevelOne() {
        val ce1: Emergency = CrimeEmergencyFactory().createCrimeLevelOne(listOf(1, 1, 1, 1), "", "")

        assertEquals(EmergencyType.CRIME, ce1.emergencyType)
        assertEquals(1, ce1.severity)
        assertEquals(1, ce1.requiredCriminals)
        assertEquals(1, ce1.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(ce1)

        assertEquals(1, vehicleTypeCounts[VehicleType.POLICE_CAR])
    }

    @Test
    fun testCrimeEmergencyLevelTwo() {
        val ce2: Emergency = CrimeEmergencyFactory().createCrimeLevelTwo(listOf(1, 1, 1, 1), "", "")

        assertEquals(EmergencyType.CRIME, ce2.emergencyType)
        assertEquals(2, ce2.severity)
        assertEquals(4, ce2.requiredCriminals)
        assertEquals(6, ce2.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(ce2)

        assertEquals(4, vehicleTypeCounts[VehicleType.POLICE_CAR])
        assertEquals(1, vehicleTypeCounts[VehicleType.K9_POLICE_CAR])
        assertEquals(1, vehicleTypeCounts[VehicleType.AMBULANCE])
    }

    @Test
    fun testCrimeEmergencyLevelThree() {
        val ce3: Emergency = CrimeEmergencyFactory().createCrimeLevelThree(listOf(1, 1, 1, 1), "", "")

        assertEquals(EmergencyType.CRIME, ce3.emergencyType)
        assertEquals(3, ce3.severity)
        assertEquals(8, ce3.requiredCriminals)
        assertEquals(13, ce3.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(ce3)

        assertEquals(6, vehicleTypeCounts[VehicleType.POLICE_CAR])
        assertEquals(2, vehicleTypeCounts[VehicleType.POLICE_MOTORCYCLE])
        assertEquals(2, vehicleTypeCounts[VehicleType.K9_POLICE_CAR])
        assertEquals(2, vehicleTypeCounts[VehicleType.AMBULANCE])
        assertEquals(1, vehicleTypeCounts[VehicleType.FIREFIGHTER_TRANSPORTER])
    }

    // --- Medical Emergency Tests
    @Test
    fun testMedicalEmergencyLevelOne() {
        val me1: Emergency = MedicalEmergencyFactory().createMedicalLevelOne(listOf(1, 1, 1, 1), "", "")

        assertEquals(EmergencyType.MEDICAL, me1.emergencyType)
        assertEquals(1, me1.severity)
        assertEquals(1, me1.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(me1)

        assertEquals(1, vehicleTypeCounts[VehicleType.AMBULANCE])
    }

    @Test
    fun testMedicalEmergencyLevelTwo() {
        val me2: Emergency = MedicalEmergencyFactory().createMedicalLevelTwo(
            listOf(1, 1, 1, 1),
            "",
            ""
        )

        assertEquals(EmergencyType.MEDICAL, me2.emergencyType)
        assertEquals(2, me2.severity)
        assertEquals(2, me2.requiredPatients)
        assertEquals(3, me2.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(me2)

        assertEquals(2, vehicleTypeCounts[VehicleType.AMBULANCE])
        assertEquals(1, vehicleTypeCounts[VehicleType.EMERGENCY_DOCTOR_CAR])
    }

    @Test
    fun testMedicalEmergencyLevelThree() {
        val me3: Emergency = MedicalEmergencyFactory().createMedicalLevelThree(
            listOf(1, 1, 1, 1),
            "",
            ""
        )

        assertEquals(EmergencyType.MEDICAL, me3.emergencyType)
        assertEquals(3, me3.severity)
        assertEquals(5, me3.requiredPatients)
        assertEquals(9, me3.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(me3)

        assertEquals(5, vehicleTypeCounts[VehicleType.AMBULANCE])
        assertEquals(2, vehicleTypeCounts[VehicleType.EMERGENCY_DOCTOR_CAR])
        assertEquals(2, vehicleTypeCounts[VehicleType.FIRE_TRUCK_TECHNICAL])
    }

    // --- Accident Emergency Tests
    @Test
    fun testAccidentEmergencyLevelOne() {
        val ae1: Emergency = AccidentEmergencyFactory().createAccidentLevelOne(listOf(1, 1, 1, 1), "", "")

        assertEquals(EmergencyType.ACCIDENT, ae1.emergencyType)
        assertEquals(1, ae1.severity)
        assertEquals(1, ae1.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(ae1)

        assertEquals(1, vehicleTypeCounts[VehicleType.FIRE_TRUCK_TECHNICAL])
    }

    @Test
    fun testAccidentEmergencyLevelTwo() {
        val ae2: Emergency = AccidentEmergencyFactory().createAccidentLevelTwo(
            listOf(1, 1, 1, 1),
            "",
            ""
        )

        assertEquals(EmergencyType.ACCIDENT, ae2.emergencyType)
        assertEquals(2, ae2.severity)
        assertEquals(1, ae2.requiredPatients)
        assertEquals(5, ae2.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(ae2)

        assertEquals(2, vehicleTypeCounts[VehicleType.FIRE_TRUCK_TECHNICAL])
        assertEquals(1, vehicleTypeCounts[VehicleType.POLICE_MOTORCYCLE])
        assertEquals(1, vehicleTypeCounts[VehicleType.POLICE_CAR])
        assertEquals(1, vehicleTypeCounts[VehicleType.AMBULANCE])
    }

    @Test
    fun testAccidentEmergencyLevelThree() {
        val ae3: Emergency = AccidentEmergencyFactory().createAccidentLevelThree(
            listOf(1, 1, 1, 1),
            "",
            ""
        )
        assertEquals(EmergencyType.ACCIDENT, ae3.emergencyType)
        assertEquals(3, ae3.severity)
        assertEquals(2, ae3.requiredPatients)
        assertEquals(14, ae3.requiredVehicles.size)

        val vehicleTypeCounts = countVehicleType(ae3)

        assertEquals(4, vehicleTypeCounts[VehicleType.FIRE_TRUCK_TECHNICAL])
        assertEquals(2, vehicleTypeCounts[VehicleType.POLICE_MOTORCYCLE])
        assertEquals(4, vehicleTypeCounts[VehicleType.POLICE_CAR])
        assertEquals(3, vehicleTypeCounts[VehicleType.AMBULANCE])
        assertEquals(1, vehicleTypeCounts[VehicleType.EMERGENCY_DOCTOR_CAR])
    }

    // --- some more tests ---

    @Test
    fun testAttributes() {
        val em: Emergency = FireEmergencyFactory().createFireLevelOne(
            listOf(123, 2, 5, 10),
            "TestVillage",
            "TestRoad"
        )

        assertEquals(123, em.id)
        assertEquals(2, em.occurringTick)
        assertEquals("TestVillage", em.occurringVillage)
        assertEquals("TestRoad", em.roadName)
        assertEquals(5, em.handleTime)
        assertEquals(10, em.maxDuration)
    }
}
