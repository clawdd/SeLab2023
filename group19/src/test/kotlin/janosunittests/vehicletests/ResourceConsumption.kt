/* package janosunittests.vehicletests

import de.unisaarland.cs.se.selab.base.FireStation
import de.unisaarland.cs.se.selab.base.Hospital
import de.unisaarland.cs.se.selab.base.PoliceStation
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.EmergencyState
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.parser.DotParser
import de.unisaarland.cs.se.selab.vehicle.FireVehicle
import de.unisaarland.cs.se.selab.vehicle.MedicalVehicle
import de.unisaarland.cs.se.selab.vehicle.PoliceVehicle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ResourceConsumption {

    @Test
    fun testSeverity1FireTwoTrucksProvideWater() {
        val dotParser: DotParser =
            DotParser("src/systemtest/resources/mapFiles/navigationTestMaps/dijkstraTestScenarios/more_complicated.dot")
        dotParser.parseMap()
        val fireStation1 = FireStation(
            0,
            Map.vertices[0],
            69,
            mutableListOf(),
            mutableListOf(),
            BaseType.FIRE_STATION
        )
        Navigation.initNavi(listOf(fireStation1))
        val fireTruck1 = FireVehicle(1, 0, VehicleType.FIRE_TRUCK_WATER, 2, 3)
        fireTruck1.waterCapacity = 600
        fireTruck1.waterLevel = 600
        fireTruck1.vehicleState = VehicleState.AT_EMERGENCY
        fireTruck1.currentLocation = Map.vertices[2]
        val fireTruck2 = FireVehicle(0, 0, VehicleType.FIRE_TRUCK_WATER, 2, 5)
        fireTruck2.waterCapacity = 600
        fireTruck2.waterLevel = 600
        fireTruck2.vehicleState = VehicleState.AT_EMERGENCY
        fireTruck2.currentLocation = Map.vertices[2]
        val fireSev1 = Emergency(5, "villageA", "road23", 0, 1, 10)
        fireSev1.requiredWater = 1200
        fireSev1.assignedVehicles.add(fireTruck1)
        fireSev1.assignedVehicles.add(fireTruck2)
        fireSev1.emergencyState = EmergencyState.CURRENTLY_BEING_HANDLED
        fireSev1.updateEmergencyStatus(1)
        assertEquals(fireTruck1.waterLevel, 0)
        assertEquals(fireTruck2.waterLevel, 0)
    }

    @Test
    fun testSeverity1FireOneTruckProvidesAll() {
        val dotParser: DotParser =
            DotParser("src/systemtest/resources/mapFiles/navigationTestMaps/dijkstraTestScenarios/more_complicated.dot")
        dotParser.parseMap()
        val fireStation1 = FireStation(
            0,
            Map.vertices[0],
            69,
            mutableListOf(),
            mutableListOf(),
            BaseType.FIRE_STATION
        )
        Navigation.initNavi(listOf(fireStation1))
        val fireTruck1 = FireVehicle(1, 0, VehicleType.FIRE_TRUCK_WATER, 2, 3)
        fireTruck1.waterCapacity = 600
        fireTruck1.waterLevel = 600
        fireTruck1.vehicleState = VehicleState.AT_EMERGENCY
        fireTruck1.currentLocation = Map.vertices[2]
        val fireTruck2 = FireVehicle(0, 0, VehicleType.FIRE_TRUCK_WATER, 2, 5)
        fireTruck2.waterCapacity = 2400
        fireTruck2.waterLevel = 2000
        fireTruck2.vehicleState = VehicleState.AT_EMERGENCY
        fireTruck2.currentLocation = Map.vertices[2]
        val fireSev1 = Emergency(5, "villageA", "road23", 0, 1, 10)
        fireSev1.requiredWater = 1200
        fireSev1.assignedVehicles.add(fireTruck1)
        fireSev1.assignedVehicles.add(fireTruck2)
        fireSev1.emergencyState = EmergencyState.CURRENTLY_BEING_HANDLED
        fireSev1.updateEmergencyStatus(1)
        assertEquals(fireTruck1.waterLevel, 600)
        assertEquals(fireTruck2.waterLevel, 800)
    }

    @Test
    fun testSeverity2Medical() {
        val dotParser: DotParser =
            DotParser("src/systemtest/resources/mapFiles/navigationTestMaps/dijkstraTestScenarios/more_complicated.dot")
        dotParser.parseMap()
        val hosptial1 =
            Hospital(0, Map.vertices[0], 69, mutableListOf(), mutableListOf(), 2, BaseType.HOSPITAL)
        Navigation.initNavi(listOf(hosptial1))
        val ambulance1 = MedicalVehicle(1, 0, VehicleType.AMBULANCE, 2, 3)
        ambulance1.vehicleState = VehicleState.AT_EMERGENCY
        ambulance1.currentLocation = Map.vertices[2]
        val ambulance2 = MedicalVehicle(0, 0, VehicleType.AMBULANCE, 2, 5)
        ambulance2.vehicleState = VehicleState.AT_EMERGENCY
        ambulance2.currentLocation = Map.vertices[2]
        val medicalSev2 = Emergency(5, "villageA", "road23", 0, 1, 10)
        medicalSev2.requiredPatients = 2
        medicalSev2.assignedVehicles.add(ambulance1)
        medicalSev2.assignedVehicles.add(ambulance2)
        medicalSev2.emergencyState = EmergencyState.CURRENTLY_BEING_HANDLED
        medicalSev2.updateEmergencyStatus(1)
        assertTrue(ambulance1.patient)
        assertTrue(ambulance1.patient)
        assertTrue(ambulance1.recovering)
        assertTrue(ambulance2.recovering)
    }

    @Test
    fun testSeverity2CrimeWithReturn() {
        val dotParser: DotParser =
            DotParser("src/systemtest/resources/mapFiles/navigationTestMaps/dijkstraTestScenarios/more_complicated.dot")
        dotParser.parseMap()
        val policeStation1 = PoliceStation(
            1,
            Map.vertices[5],
            0,
            mutableListOf(),
            mutableListOf(),
            0,
            BaseType.POLICE_STATION
        )
        val policeCar1 = PoliceVehicle(1, 1, VehicleType.POLICE_CAR, 2, 2)
        policeCar1.vehicleState = VehicleState.AT_EMERGENCY
        policeCar1.currentLocation = Map.vertices[3]
        policeCar1.criminalCapacity = 2
        policeCar1.criminalsOnBoard = 1
        val policeCar2 = PoliceVehicle(4, 1, VehicleType.POLICE_CAR, 2, 3)
        policeCar2.vehicleState = VehicleState.AT_EMERGENCY
        policeCar2.currentLocation = Map.vertices[3]
        policeCar2.criminalCapacity = 4
        policeCar2.criminalsOnBoard = 3
        val policeCar3 = PoliceVehicle(3, 1, VehicleType.POLICE_CAR, 2, 3)
        policeCar3.vehicleState = VehicleState.AT_EMERGENCY
        policeCar3.currentLocation = Map.vertices[3]
        policeCar3.criminalCapacity = 2
        policeCar3.criminalsOnBoard = 0
        val policeCar4 = PoliceVehicle(5, 1, VehicleType.POLICE_CAR, 2, 4)
        policeCar4.vehicleState = VehicleState.AT_EMERGENCY
        policeCar4.currentLocation = Map.vertices[3]
        policeCar4.criminalCapacity = 2
        policeCar4.criminalsOnBoard = 0
        val policeCar5 = PoliceVehicle(4, 1, VehicleType.K9_POLICE_CAR, 2, 1)
        policeCar5.vehicleState = VehicleState.AT_EMERGENCY
        policeCar5.currentLocation = Map.vertices[3]
        val hospital1 = Hospital(0, Map.vertices[0], 0, mutableListOf(), mutableListOf(), 2, BaseType.HOSPITAL)
        Navigation.initNavi(listOf(hospital1, policeStation1))
        val ambulance1 = MedicalVehicle(1, 0, VehicleType.AMBULANCE, 2, 3)
        ambulance1.vehicleState = VehicleState.AT_EMERGENCY
        ambulance1.currentLocation = Map.vertices[2]
        val crimeSev2 = Emergency(5, "villageA", "road23", 0, 1, 10)
        crimeSev2.requiredCriminals = 4
        crimeSev2.assignedVehicles.add(ambulance1)
        crimeSev2.assignedVehicles.add(policeCar1)
        crimeSev2.assignedVehicles.add(policeCar2)
        crimeSev2.assignedVehicles.add(policeCar3)
        crimeSev2.assignedVehicles.add(policeCar4)
        crimeSev2.assignedVehicles.add(policeCar5)
        crimeSev2.emergencyState = EmergencyState.CURRENTLY_BEING_HANDLED
        crimeSev2.updateEmergencyStatus(1)
        assertEquals(policeCar1.criminalsOnBoard, 2)
        assertTrue(policeCar1.recovering)
        assertEquals(policeCar2.criminalsOnBoard, 4)
        assertTrue(policeCar2.recovering)
        assertEquals(policeCar3.criminalsOnBoard, 2)
        assertTrue(policeCar3.recovering)
        assertEquals(policeCar4.criminalsOnBoard, 0)
    }

    @Test
    fun testSeverity1CrimeAllConsInOneCar() {
        val dotParser: DotParser =
            DotParser("src/systemtest/resources/mapFiles/navigationTestMaps/dijkstraTestScenarios/more_complicated.dot")
        dotParser.parseMap()
        val policeStation1 = PoliceStation(
            1,
            Map.vertices[5],
            0,
            mutableListOf(),
            mutableListOf(),
            0,
            BaseType.POLICE_STATION
        )
        val policeCar1 = PoliceVehicle(1, 1, VehicleType.POLICE_CAR, 2, 2)
        policeCar1.vehicleState = VehicleState.AT_EMERGENCY
        policeCar1.currentLocation = Map.vertices[3]
        policeCar1.criminalCapacity = 2
        policeCar1.criminalsOnBoard = 0
        Navigation.initNavi(listOf(policeStation1))
        val crimeSev1 = Emergency(5, "villageA", "road23", 0, 1, 10)
        crimeSev1.requiredCriminals = 1
        crimeSev1.assignedVehicles.add(policeCar1)
        crimeSev1.assignedVehicles.add(policeCar1)
        crimeSev1.emergencyState = EmergencyState.CURRENTLY_BEING_HANDLED
        crimeSev1.updateEmergencyStatus(1)
        assertEquals(policeCar1.criminalsOnBoard, 1)
        assertFalse(policeCar1.recovering)
    }
}
*/
