package keenunittests

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.parser.BaseValidator
import de.unisaarland.cs.se.selab.parser.DotParser
import de.unisaarland.cs.se.selab.parser.VehicleBaseParser
import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import java.io.File
import kotlin.test.Test

class BaseValidatorTests {
    private val jsonFile = File("src/systemtest/resources/assetsJsons/unittest_example_assets.json")
    private val dotFile = File("src/systemtest/resources/mapFiles/unittest_example_map.dot")
    private lateinit var originalAssetContent: String
    private lateinit var originalMapContent: String
    private lateinit var dotParser: DotParser
    private lateinit var vbParser: VehicleBaseParser
    private lateinit var bValidator: BaseValidator
    // make sure don't change the files

    @BeforeEach
    fun setup() {
        originalAssetContent = jsonFile.readText()
        originalMapContent = dotFile.readText()
        dotParser = DotParser("src/systemtest/resources/mapFiles/unittest_example_map.dot")
        // dotParser.parseMap()
        vbParser = VehicleBaseParser("src/systemtest/resources/assetsJsons/unittest_example_assets.json")
        bValidator = BaseValidator(vbParser.getValidFlag(), vbParser)
    }

    @AfterEach
    fun tearDown() {
        jsonFile.writeText(originalAssetContent)
        dotFile.writeText(originalMapContent)
    }

    @Test
    fun testValidateAllNecessaryAttributesA() {
        assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // valid
        jsonObject.put(Constants.KEY_ID, "0")
        jsonObject.put(Constants.KEY_STAFF, "62")
        jsonObject.put(Constants.KEY_LOCATION, "2")
        bValidator.validateAllNecessaryAttributes(jsonObject)
        assertTrue(bValidator.getBaseValidFlag())
    }

    @Test
    fun testValidateAllNecessaryAttributesB() {
        assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // id negative
        jsonObject.put(Constants.KEY_ID, "-1")
        jsonObject.put(Constants.KEY_STAFF, "62")
        jsonObject.put(Constants.KEY_LOCATION, "2")
        bValidator.validateAllNecessaryAttributes(jsonObject)
        assertFalse(bValidator.getBaseValidFlag())
    }

    @Test
    fun testValidateAllNecessaryAttributesC() {
        assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // id string
        jsonObject.put(Constants.KEY_ID, "abc")
        jsonObject.put(Constants.KEY_STAFF, "62")
        jsonObject.put(Constants.KEY_LOCATION, "2")
        bValidator.validateAllNecessaryAttributes(jsonObject)
        assertFalse(bValidator.getBaseValidFlag())
    }

    @Test
    fun testValidateAllNecessaryAttributesD() {
        assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // staff negative
        jsonObject.put(Constants.KEY_ID, "1")
        jsonObject.put(Constants.KEY_STAFF, "-62")
        jsonObject.put(Constants.KEY_LOCATION, "2")
        bValidator.validateAllNecessaryAttributes(jsonObject)
        assertFalse(bValidator.getBaseValidFlag())
    }

    @Test
    fun testValidateAllNecessaryAttributesE() {
        assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // location string
        jsonObject.put(Constants.KEY_ID, "1")
        jsonObject.put(Constants.KEY_STAFF, "4")
        jsonObject.put(Constants.KEY_LOCATION, "abc")
        bValidator.validateAllNecessaryAttributes(jsonObject)
        assertFalse(bValidator.getBaseValidFlag())
    }

    @Test
    fun testValidateAllNecessaryAttributesF() {
        assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // location empty string
        jsonObject.put(Constants.KEY_ID, "1")
        jsonObject.put(Constants.KEY_STAFF, "4")
        jsonObject.put(Constants.KEY_LOCATION, "")
        bValidator.validateAllNecessaryAttributes(jsonObject)
        assertFalse(bValidator.getBaseValidFlag())
    }

    @Test
    fun testValidateFireStation() {
        assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // missing key
        jsonObject.put(Constants.KEY_ID, "1")
        jsonObject.put(Constants.KEY_STAFF, "4")
        jsonObject.put(Constants.KEY_LOCATION, "")
        bValidator.validateFireStation(jsonObject)
        assertFalse(bValidator.getBaseValidFlag())
    }

    @Test
    fun testValidatePoliceStation() {
        assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // more key
        jsonObject.put(Constants.KEY_ID, "1")
        jsonObject.put(Constants.KEY_STAFF, "12")
        jsonObject.put(Constants.KEY_LOCATION, "4")
        jsonObject.put(Constants.KEY_BASE_TYPE, "POLICE_STATION")
        jsonObject.put(Constants.KEY_DOGS, "3")
        bValidator.validatePoliceStation(jsonObject)
        assertTrue(bValidator.getBaseValidFlag())
        jsonObject.put(Constants.KEY_DOCTORS, "4")
        bValidator.validatePoliceStation(jsonObject)
        assertFalse(bValidator.getBaseValidFlag())
    }

    @Test
    fun testValidateHospital() {
        assertTrue(dotParser.parseMap())
        val jsonObject = JSONObject()
        // wrong key, here don't check base type, just the key
        jsonObject.put(Constants.KEY_ID, "1")
        jsonObject.put(Constants.KEY_STAFF, "12")
        jsonObject.put(Constants.KEY_LOCATION, "4")
        jsonObject.put(Constants.KEY_BASE_TYPE, "xxx")
        jsonObject.put(Constants.KEY_DOGS, "3")
        bValidator.validateHospital(jsonObject)
        assertFalse(bValidator.getBaseValidFlag())
    }
}
