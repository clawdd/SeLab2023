package keenunittests

import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.parser.DotParser
import de.unisaarland.cs.se.selab.parser.VehicleBaseParser
import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class VehicleBaseParserTests {
    private val jsonFile = File("src/systemtest/resources/assetsJsons/unittest_example_assets.json")
    private val dotFile = File("src/systemtest/resources/mapFiles/unittest_example_map.dot")
    private lateinit var originalAssetContent: String
    private lateinit var originalMapContent: String
    private lateinit var vbParser: VehicleBaseParser
    // make sure don't change the files

    @BeforeEach
    fun setup() {
        originalAssetContent = jsonFile.readText()
        originalMapContent = dotFile.readText()
        vbParser = VehicleBaseParser("src/systemtest/resources/assetsJsons/unittest_example_assets.json")
    }

    @AfterEach
    fun tearDown() {
        jsonFile.writeText(originalAssetContent)
        dotFile.writeText(originalMapContent)
    }

    @Test
    fun testParseBaseType() {
        val firestation = vbParser.parseBaseType("FIRE_STATION")
        assertEquals(BaseType.FIRE_STATION, firestation)
        val policestation = vbParser.parseBaseType("POLICE_STATION")
        assertEquals(BaseType.POLICE_STATION, policestation)
        val hospital = vbParser.parseBaseType("HOSPITAL")
        assertEquals(BaseType.HOSPITAL, hospital)
        // invalid
        vbParser.parseBaseType("WRONG")
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testParseVehicleType() {
        assertEquals(VehicleType.AMBULANCE, vbParser.parseVehicleType("AMBULANCE"))
        assertEquals(VehicleType.EMERGENCY_DOCTOR_CAR, vbParser.parseVehicleType("EMERGENCY_DOCTOR_CAR"))
        assertEquals(VehicleType.POLICE_CAR, vbParser.parseVehicleType("POLICE_CAR"))
        assertEquals(VehicleType.K9_POLICE_CAR, vbParser.parseVehicleType("K9_POLICE_CAR"))
        assertEquals(VehicleType.POLICE_MOTORCYCLE, vbParser.parseVehicleType("POLICE_MOTORCYCLE"))
        assertEquals(VehicleType.FIRE_TRUCK_LADDER, vbParser.parseVehicleType("FIRE_TRUCK_LADDER"))
        assertEquals(VehicleType.FIRE_TRUCK_TECHNICAL, vbParser.parseVehicleType("FIRE_TRUCK_TECHNICAL"))
        assertEquals(VehicleType.FIRE_TRUCK_WATER, vbParser.parseVehicleType("FIRE_TRUCK_WATER"))
        assertEquals(VehicleType.FIREFIGHTER_TRANSPORTER, vbParser.parseVehicleType("FIREFIGHTER_TRANSPORTER"))
        // invalid
        // val wrong =
        vbParser.parseVehicleType("WRONG")
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCreateVehicleIntAttributesListA() {
        // valid
        val attributesValid = vbParser.createVehicleIntAttributesList(1, 2, 3, 4)
        assertEquals(listOf(1, 2, 3, 4), attributesValid)
        assertEquals(true, vbParser.getValidFlag())
        // negative id
        val negId = vbParser.createVehicleIntAttributesList(-1, 2, 3, 4)
        assertEquals(listOf(-1, -1, -1, -1), negId)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCreateVehicleIntAttributesListB() {
        assertEquals(true, vbParser.getValidFlag())
        // negative BaseId
        val negBaseId = vbParser.createVehicleIntAttributesList(1, -2, 3, 4)
        assertEquals(listOf(-1, -1, -1, -1), negBaseId)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCreateVehicleIntAttributesListC() {
        assertEquals(true, vbParser.getValidFlag())
        // staff,not 0<x<=12
        val wrongStaff = vbParser.createVehicleIntAttributesList(1, 2, -3, 4)
        assertEquals(listOf(-1, -1, -1, -1), wrongStaff)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCreateVehicleIntAttributesListD() {
        assertEquals(true, vbParser.getValidFlag())
        // staff,not 0<x<=12
        val wrongStaff = vbParser.createVehicleIntAttributesList(1, 2, 13, 4)
        assertEquals(listOf(-1, -1, -1, -1), wrongStaff)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCreateVehicleIntAttributesListE() {
        assertEquals(true, vbParser.getValidFlag())
        // vehicle height,not 1<=x<=5
        val wrongStaff = vbParser.createVehicleIntAttributesList(1, 2, 3, 0)
        assertEquals(listOf(-1, -1, -1, -1), wrongStaff)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCreateVehicleIntAttributesListF() {
        assertEquals(true, vbParser.getValidFlag())
        // vehicle height,not 1<=x<=5
        val wrongStaff = vbParser.createVehicleIntAttributesList(1, 2, 3, 6)
        assertEquals(listOf(-1, -1, -1, -1), wrongStaff)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCheckOptLadderA() {
        // valid ladder height
        assertEquals(true, vbParser.getValidFlag())
        assertEquals(40, vbParser.checkOptLadder("40"))
        assertEquals(true, vbParser.getValidFlag())
    }

    @Test
    fun testCheckOptLadderB() {
        // valid ladder height
        assertEquals(true, vbParser.getValidFlag())
        assertEquals(-1, vbParser.checkOptLadder("29"))
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCheckOptLadderC() {
        assertEquals(true, vbParser.getValidFlag())
        assertEquals(-1, vbParser.checkOptLadder("71"))
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCheckOptLadderD() {
        assertEquals(true, vbParser.getValidFlag())
        assertEquals(-1, vbParser.checkOptLadder(""))
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCheckOptCriminalA() {
        assertEquals(true, vbParser.getValidFlag())
        assertEquals(1, vbParser.checkOptCriminal("1"))
        assertEquals(4, vbParser.checkOptCriminal("4"))
        assertEquals(true, vbParser.getValidFlag())
    }

    @Test
    fun testCheckOptCriminalB() {
        assertEquals(true, vbParser.getValidFlag())
        assertEquals(-1, vbParser.checkOptCriminal("-2"))
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCheckOptCriminalC() {
        assertEquals(true, vbParser.getValidFlag())
        assertEquals(-1, vbParser.checkOptCriminal("88"))
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCheckOptCriminalD() {
        assertEquals(true, vbParser.getValidFlag())
        assertEquals(-1, vbParser.checkOptCriminal(""))
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCheckOptWaterA() {
        // valid water volume
        assertEquals(true, vbParser.getValidFlag())
        assertEquals(600, vbParser.checkOptWater("600"))
        assertEquals(1200, vbParser.checkOptWater("1200"))
        assertEquals(2400, vbParser.checkOptWater("2400"))
        assertEquals(true, vbParser.getValidFlag())
    }

    @Test
    fun testCheckOptWaterB() {
        // invalid water volume
        assertEquals(true, vbParser.getValidFlag())
        assertEquals(-1, vbParser.checkOptWater("3001"))
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testCheckOptWaterC() {
        // invalid water volume
        assertEquals(true, vbParser.getValidFlag())
        assertEquals(-1, vbParser.checkOptWater(""))
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterA() {
        // FireTruckLadder with WaterCapacity
        val jsonObjectFtlW = JSONObject()
        jsonObjectFtlW.put("waterCapacity", "1000")
        assertEquals(true, vbParser.getValidFlag())
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectFtlW, VehicleType.FIRE_TRUCK_LADDER)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterB() {
        // FireTruckLadder with CriminalCapacity
        val jsonObjectFtlC = JSONObject()
        jsonObjectFtlC.put("criminalCapacity", "2")
        assertEquals(true, vbParser.getValidFlag())
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectFtlC, VehicleType.FIRE_TRUCK_LADDER)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterC() {
        assertEquals(true, vbParser.getValidFlag())
        // FireTruckLadder valid
        val jsonObjectFtl = JSONObject()
        jsonObjectFtl.put("ladderLength", "50")
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectFtl, VehicleType.FIRE_TRUCK_LADDER)
        assertEquals(true, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterD() {
        assertEquals(true, vbParser.getValidFlag())
        // FireTruckWater With Ladder
        val jsonObjectFtwL = JSONObject()
        jsonObjectFtwL.put("ladderLength", "50")
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectFtwL, VehicleType.FIRE_TRUCK_WATER)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterE() {
        assertEquals(true, vbParser.getValidFlag())
        // FireTruckWater with CriminalCapacity
        val jsonObjectFtwC = JSONObject()
        jsonObjectFtwC.put("criminalCapacity", "1")
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectFtwC, VehicleType.FIRE_TRUCK_WATER)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterF() {
        assertEquals(true, vbParser.getValidFlag())
        // FireTruckwater valid
        val jsonObjectFtw = JSONObject()
        jsonObjectFtw.put("waterCapacity", "600")
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectFtw, VehicleType.FIRE_TRUCK_WATER)
        assertEquals(true, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterG() {
        assertEquals(true, vbParser.getValidFlag())
        // PoliceCar with Ladder
        val jsonObjectPcL = JSONObject()
        jsonObjectPcL.put("ladderLength", "30")
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectPcL, VehicleType.POLICE_CAR)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterH() {
        assertEquals(true, vbParser.getValidFlag())
        // PoliceCar with WaterCapacity
        val jsonObjectPcW = JSONObject()
        jsonObjectPcW.put("waterCapacity", "800")
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectPcW, VehicleType.POLICE_CAR)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterI() {
        assertEquals(true, vbParser.getValidFlag())
        // PoliceCar valid
        val jsonObjectPc = JSONObject()
        jsonObjectPc.put("criminalCapacity", "1")
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectPc, VehicleType.POLICE_CAR)
        assertEquals(true, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterJ() {
        // other vehicle wrong water attribute
        assertEquals(true, vbParser.getValidFlag())
        val jsonObjectWrongW = JSONObject()
        jsonObjectWrongW.put("waterCapacity", "1200")
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectWrongW, VehicleType.K9_POLICE_CAR)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterK() {
        assertEquals(true, vbParser.getValidFlag())
        // other type with wrong attributes given, ladder
        val jsonObjectWrongL = JSONObject()
        jsonObjectWrongL.put("ladderLength", "40")
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectWrongL, VehicleType.EMERGENCY_DOCTOR_CAR)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterL() {
        // other type with wrong attributes given
        assertEquals(true, vbParser.getValidFlag())
        val jsonObjectWrongC = JSONObject()
        jsonObjectWrongC.put("criminalCapacity", "3")
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObjectWrongC, VehicleType.FIREFIGHTER_TRANSPORTER)
        assertEquals(false, vbParser.getValidFlag())
    }

    @Test
    fun testValidateWrongLadderOrCriminalOrWaterM() {
        assertEquals(true, vbParser.getValidFlag())
        // other valid... just for detekt
        val jsonObject = JSONObject()
        vbParser.validateWrongLadderOrCriminalOrWater(jsonObject, VehicleType.POLICE_MOTORCYCLE)
        assertEquals(true, vbParser.getValidFlag())
    }

    @Test
    fun testParseBasesAndVehicles() {
        val dotParser = DotParser("src/systemtest/resources/mapFiles/unittest_example_map.dot")
        dotParser.parseMap()
        Map.name
        val basesList = vbParser.parseBases()
        val vehiclesList = vbParser.parseVehicles()
        assertEquals(true, vbParser.getValidFlag())
        assertNotNull(basesList)
        assertNotNull(vehiclesList)
    }
}
