package keenunittests

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.parser.BaseValidator
import de.unisaarland.cs.se.selab.parser.DotParser
import de.unisaarland.cs.se.selab.parser.VehicleBaseParser
import de.unisaarland.cs.se.selab.parser.VehicleValidator
import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import java.io.File
import kotlin.test.Test

class VehicleValidatorTests {
    private val jsonFile = File("src/systemtest/resources/assetsJsons/unittest_example_assets.json")
    private val dotFile = File("src/systemtest/resources/mapFiles/unittest_example_map.dot")
    private lateinit var originalAssetContent: String
    private lateinit var originalMapContent: String
    private lateinit var dotParser: DotParser
    private lateinit var vbParser: VehicleBaseParser
    private lateinit var bValidator: BaseValidator
    private lateinit var vValidator: VehicleValidator
    // make sure don't change the files

    @BeforeEach
    fun setup() {
        originalAssetContent = jsonFile.readText()
        originalMapContent = dotFile.readText()
        dotParser = DotParser("src/systemtest/resources/mapFiles/unittest_example_map.dot")
        vbParser = VehicleBaseParser("src/systemtest/resources/assetsJsons/unittest_example_assets.json")
        bValidator = BaseValidator(vbParser.getValidFlag(), vbParser)
        vValidator = VehicleValidator(vbParser.getValidFlag(), vbParser)
    }

    @AfterEach
    fun tearDown() {
        jsonFile.writeText(originalAssetContent)
        dotFile.writeText(originalMapContent)
    }

    @Test
    fun testwhole() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vbParser.helperParseVehicles()
        Assertions.assertTrue(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateAllNecessaryAttributesA() {
        Assertions.assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // valid
        jsonObject.put(Constants.KEY_ID, "0")
        jsonObject.put(Constants.KEY_BASE_ID, "1")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "POLICE_CAR")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "2")
        jsonObject.put(Constants.STAFF_CAPACITY, "5")
        jsonObject.put(Constants.CRIMINAL_CAPACITY, "3")
        // vValidator.validateAllNecessaryAttributes(jsonObject)
        Assertions.assertTrue(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateAllNecessaryAttributesB() {
        Assertions.assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // string id
        jsonObject.put(Constants.KEY_ID, "www")
        jsonObject.put(Constants.KEY_BASE_ID, "1")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "POLICE_CAR")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "2")
        jsonObject.put(Constants.STAFF_CAPACITY, "5")
        vValidator.validateAllNecessaryAttributes(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateAllNecessaryAttributesC() {
        Assertions.assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // neg height
        jsonObject.put(Constants.KEY_ID, "0")
        jsonObject.put(Constants.KEY_BASE_ID, "1")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "POLICE_CAR")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "-2")
        jsonObject.put(Constants.STAFF_CAPACITY, "5")
        vValidator.validateAllNecessaryAttributes(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateAllNecessaryAttributesD() {
        Assertions.assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // missing staff capa
        jsonObject.put(Constants.KEY_ID, "0")
        jsonObject.put(Constants.KEY_BASE_ID, "1")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "POLICE_CAR")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "2")
        jsonObject.put(Constants.STAFF_CAPACITY, "")
        vValidator.validateAllNecessaryAttributes(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateAllNecessaryAttributesE() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vbParser.parseVehicles()
        val jsonObject = JSONObject()
        // string base id
        jsonObject.put(Constants.KEY_ID, "0")
        jsonObject.put(Constants.KEY_BASE_ID, "xxx")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "POLICE_CAR")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "2")
        jsonObject.put(Constants.STAFF_CAPACITY, "")
        vValidator.validateAllNecessaryAttributes(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateAmbulance() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vbParser.parseVehicles()
        val jsonObject = JSONObject()
        // valide
        jsonObject.put(Constants.KEY_ID, "42")
        jsonObject.put(Constants.KEY_BASE_ID, "2")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "AMBULANCE")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "4")
        jsonObject.put(Constants.STAFF_CAPACITY, "2")
        vValidator.validateAmbulance(jsonObject)
        Assertions.assertTrue(vValidator.getVehicleValidFlag())
        // add more key
        jsonObject.put(Constants.KEY_DOGS, "2")
        vValidator.validateAmbulance(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateEmergencyDoctorCar() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vbParser.parseVehicles()
        val jsonObject = JSONObject()
        // valide
        jsonObject.put(Constants.KEY_ID, "43")
        jsonObject.put(Constants.KEY_BASE_ID, "2")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "EMERGENCY_DOCTOR_CAR")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "3")
        jsonObject.put(Constants.STAFF_CAPACITY, "2")
        vValidator.validateEmergencyDoctorCar(jsonObject)
        Assertions.assertTrue(vValidator.getVehicleValidFlag())
        // wrong staff
        jsonObject.put(Constants.STAFF_CAPACITY, "-2")
        vValidator.validateEmergencyDoctorCar(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateFireTruckWater() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vbParser.parseVehicles()
        val jsonObject = JSONObject()
        // invalid missing water, don't check type here
        jsonObject.put(Constants.KEY_ID, "43")
        jsonObject.put(Constants.KEY_BASE_ID, "2")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "...")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "3")
        jsonObject.put(Constants.STAFF_CAPACITY, "2")
        vValidator.validateFireTruckWater(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateFireTruckTechnical() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vbParser.parseVehicles()
        val jsonObject = JSONObject()
        // invalid not match base
        jsonObject.put(Constants.KEY_ID, "43")
        jsonObject.put(Constants.KEY_BASE_ID, "1")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "...")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "3")
        jsonObject.put(Constants.STAFF_CAPACITY, "2")
        vValidator.validateFireTruckTechnical(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateFireTruckLadder() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vbParser.parseVehicles()
        val jsonObject = JSONObject()
        // invalid base
        jsonObject.put(Constants.KEY_ID, "43")
        jsonObject.put(Constants.KEY_BASE_ID, "70")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "...")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "3")
        jsonObject.put(Constants.STAFF_CAPACITY, "2")
        jsonObject.put(Constants.LADDER_HEIGHT, "35")
        vValidator.validateFireTruckLadder(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidatFirefighterTransporter() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vbParser.parseVehicles()
        val jsonObject = JSONObject()
        // invalid staff
        jsonObject.put(Constants.KEY_ID, "43")
        jsonObject.put(Constants.KEY_BASE_ID, "0")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "...")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "3")
        jsonObject.put(Constants.STAFF_CAPACITY, "")
        vValidator.validateFirefighterTransporter(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidatePoliceCar() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vbParser.parseVehicles()
        val jsonObject = JSONObject()
        // invalid staff
        jsonObject.put(Constants.KEY_ID, "43")
        jsonObject.put(Constants.KEY_BASE_ID, "1")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "...")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "7")
        jsonObject.put(Constants.STAFF_CAPACITY, "5")
        vValidator.validatePoliceCar(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateKPoliceCar() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vbParser.parseVehicles()
        val jsonObject = JSONObject()
        // invalid height
        jsonObject.put(Constants.KEY_ID, "43")
        jsonObject.put(Constants.KEY_BASE_ID, "1")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "...")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "0")
        jsonObject.put(Constants.STAFF_CAPACITY, "5")
        vValidator.validateKninePoliceCar(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testValidateMPoliceCar() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vbParser.parseVehicles()
        val jsonObject = JSONObject()
        // invalid height
        jsonObject.put(Constants.KEY_ID, "43")
        jsonObject.put(Constants.KEY_BASE_ID, "1")
        jsonObject.put(Constants.KEY_VEHICLE_TYPE, "...")
        jsonObject.put(Constants.VEHICLE_HEIGHT, "0")
        jsonObject.put(Constants.STAFF_CAPACITY, "5")
        jsonObject.put(Constants.WATER_CAPACITY, "3")
        vValidator.validatePoliceMotorcycle(jsonObject)
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }

    @Test
    fun testAtLeastVehicle() {
        Assertions.assertTrue(dotParser.parseMap())
        vbParser.parseBases()
        vValidator.checkAtleastVehicle()
        Assertions.assertFalse(vValidator.getVehicleValidFlag())
    }
}
