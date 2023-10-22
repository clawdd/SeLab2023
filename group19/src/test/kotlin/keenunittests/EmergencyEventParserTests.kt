package keenunittests

import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.enumtype.EventType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.PrimaryRoadType
import de.unisaarland.cs.se.selab.parser.BaseValidator
import de.unisaarland.cs.se.selab.parser.DotParser
import de.unisaarland.cs.se.selab.parser.EmergencyEventParser
import de.unisaarland.cs.se.selab.parser.VehicleBaseParser
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class EmergencyEventParserTests {
    val eeParser = EmergencyEventParser("src/systemtest/resources/scenarioJsons/unittest_example_scenario.json")
    private val jsonFile = File("src/systemtest/resources/assetsJsons/unittest_example_assets.json")
    private val dotFile = File("src/systemtest/resources/mapFiles/unittest_example_map.dot")
    private lateinit var originalAssetContent: String
    private lateinit var originalMapContent: String
    private lateinit var vbParser: VehicleBaseParser
    private lateinit var bValidator: BaseValidator

    @BeforeEach
    fun setup() {
        // todo
        val dotParser = DotParser("src/systemtest/resources/mapFiles/unittest_example_map.dot")
        dotParser.parseMap()
        Map.name
        //
        originalAssetContent = jsonFile.readText()
        originalMapContent = dotFile.readText()
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
    fun testEmpty() {
        assertEquals("", "")
        assertNotEquals(1, 2)
        // vbParser.parseBases()
    }

    @Test
    fun testWhole() {
        val dotParser = DotParser("src/systemtest/resources/mapFiles/unittest_example_map.dot")
        dotParser.parseMap()
        Map.name
        val basesList = vbParser.parseBases()
        val vehiclesList = vbParser.parseVehicles()
        assertEquals(true, vbParser.getValidFlag())
        Assertions.assertNotNull(basesList)
        Assertions.assertNotNull(vehiclesList)
        //
        val emergencyList = eeParser.parseEmergencies()
        val eventsList = eeParser.parseEvents(vehiclesList)
        assertEquals(true, eeParser.getValidFlag())
        Assertions.assertNotNull(emergencyList)
        Assertions.assertNotNull(eventsList)
    }

    @Test
    fun testParseEventType() {
        assertEquals(EventType.TRAFFIC_JAM, eeParser.parseEventType("TRAFFIC_JAM"))
        assertEquals(EventType.CONSTRUCTION_SITE, eeParser.parseEventType("CONSTRUCTION_SITE"))
        assertEquals(EventType.ROAD_CLOSURE, eeParser.parseEventType("ROAD_CLOSURE"))
        assertEquals(EventType.RUSH_HOUR, eeParser.parseEventType("RUSH_HOUR"))
        assertEquals(EventType.VEHICLE_UNAVAILABLE, eeParser.parseEventType("VEHICLE_UNAVAILABLE"))
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseEventType("WRONG")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testParseEmergencyType() {
        assertEquals(EmergencyType.ACCIDENT, eeParser.parseEmergencyType("ACCIDENT"))
        assertEquals(EmergencyType.CRIME, eeParser.parseEmergencyType("CRIME"))
        assertEquals(EmergencyType.FIRE, eeParser.parseEmergencyType("FIRE"))
        assertEquals(EmergencyType.MEDICAL, eeParser.parseEmergencyType("MEDICAL"))
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseEmergencyType("WRONG")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListA() {
        // id, tick, handletime, maxduration, severity
        val validEmergencyList = eeParser.createEmIntAttrList("1", "2", "3", "4", "1")
        assertEquals(listOf(1, 2, 3, 4, 1), validEmergencyList)
        assertEquals(true, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListB() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, missing id
        eeParser.createEmIntAttrList("", "2", "3", "4", "1")
        // assertEquals(listOf(-1, -1, -1, -1, -1), wrongbEmergencyList)
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListC() {
        // id, tick, handletime, maxduration, severity
        // wrong, missing tick
        assertEquals(true, eeParser.getValidFlag())
        eeParser.createEmIntAttrList("1", "", "3", "4", "1")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListD() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, missing handle
        eeParser.createEmIntAttrList("1", "2", "", "4", "1")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListE() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, missing duration
        eeParser.createEmIntAttrList("1", "2", "3", "", "1")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListF() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, missing severity
        eeParser.createEmIntAttrList("1", "2", "3", "4", "")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListG() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, negative id
        eeParser.createEmIntAttrList("-1", "2", "3", "4", "1")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListH() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, negative tick
        eeParser.createEmIntAttrList("-1", "-2", "3", "4", "1")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListI() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, negative handletime
        eeParser.createEmIntAttrList("-1", "2", "-3", "4", "1")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListJ() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, negative duration
        eeParser.createEmIntAttrList("-1", "2", "3", "-4", "1")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListK() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, severity
        eeParser.createEmIntAttrList("-1", "2", "3", "4", "5")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEmergencyIntAttributesListL() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, not number, seems find ,still need to check not number input?
        eeParser.createEmIntAttrList("test", "2", "3", "4", "3")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEventIntAttributesListA() {
        // id, tick, handletime, maxduration, severity
        val validList = eeParser.createEventIntAttributesList("1", "2", "3")
        assertEquals(listOf(1, 2, 3, 5), validList)
        assertEquals(true, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEventIntAttributesListB() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, missing id
        eeParser.createEventIntAttributesList("", "2", "3")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEventIntAttributesListC() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, missing tick
        eeParser.createEventIntAttributesList("1", "", "3")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEventIntAttributesListD() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, missing handle
        eeParser.createEventIntAttributesList("1", "2", "")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEventIntAttributesListE() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, negative id
        eeParser.createEventIntAttributesList("-1", "2", "3")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEventIntAttributesListF() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, negative tick
        eeParser.createEventIntAttributesList("1", "-2", "3")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEventIntAttributesListG() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, negative duration
        eeParser.createEventIntAttributesList("1", "2", "-3")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCreateEventIntAttributesListH() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // wrong, not number, seems find ,still need to check not number input?
        eeParser.createEventIntAttributesList("test", "2", "3")
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testHelperForRushHourA() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // valid
        val rTvalid = JSONArray()
        val rTenumValid = mutableListOf<PrimaryRoadType>()
        rTvalid.put("COUNTY_ROAD")
        rTvalid.put("MAIN_STREET")
        rTvalid.put("SIDE_STREET")
        eeParser.helperForRushHour(rTvalid, rTenumValid)
        assertEquals(true, eeParser.getValidFlag())
        assertEquals(3, rTenumValid.size)
    }

    @Test
    fun testHelperForRushHourB() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // valid
        val rTvalid = JSONArray()
        val rTenumValid = mutableListOf<PrimaryRoadType>()
        rTvalid.put("Wrong")
        eeParser.helperForRushHour(rTvalid, rTenumValid)
        assertEquals(false, eeParser.getValidFlag())
        assertEquals(0, rTenumValid.size)
    }

    @Test
    fun testParseSingleAccidentEmergency() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // valid
        eeParser.parseSingleAccidentEmergency(listOf(1, 2, 3, 4, 1), "saar", "eisenbahn", 1)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleAccidentEmergency(listOf(1, 2, 3, 4, 2), "saar", "eisenbahn", 2)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleAccidentEmergency(listOf(1, 2, 3, 4, 3), "saar", "eisenbahn", 3)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleAccidentEmergency(listOf(1, 2, 3, 4, 4), "saar", "eisenbahn", 4)
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testParseSingleCrimeEmergency() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // valid
        eeParser.parseSingleCrimeEmergency(listOf(1, 2, 3, 4, 1), "saar", "eisenbahn", 1)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleCrimeEmergency(listOf(1, 2, 3, 4, 2), "saar", "eisenbahn", 2)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleCrimeEmergency(listOf(1, 2, 3, 4, 3), "saar", "eisenbahn", 3)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleCrimeEmergency(listOf(1, 2, 3, 4, 4), "saar", "eisenbahn", 4)
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testParseSingleFireEmergency() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // valid
        eeParser.parseSingleFireEmergency(listOf(1, 2, 3, 4, 1), "saar", "eisenbahn", 1)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleFireEmergency(listOf(1, 2, 3, 4, 2), "saar", "eisenbahn", 2)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleFireEmergency(listOf(1, 2, 3, 4, 3), "saar", "eisenbahn", 3)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleFireEmergency(listOf(1, 2, 3, 4, 4), "saar", "eisenbahn", 4)
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testParseSingleMedicalEmergency() {
        // id, tick, handletime, maxduration, severity
        assertEquals(true, eeParser.getValidFlag())
        // valid
        eeParser.parseSingleMedicalEmergency(listOf(1, 2, 3, 4, 1), "saar", "eisenbahn", 1)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleMedicalEmergency(listOf(1, 2, 3, 4, 2), "saar", "eisenbahn", 2)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleMedicalEmergency(listOf(1, 2, 3, 4, 3), "saar", "eisenbahn", 3)
        assertEquals(true, eeParser.getValidFlag())
        eeParser.parseSingleMedicalEmergency(listOf(1, 2, 3, 4, 4), "saar", "eisenbahn", 4)
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCheckRoadExistByRoadNameAndVillage() {
        assertEquals(true, eeParser.getValidFlag())
        // valid
        // eeParser.checkRoadExistByRoadNameAndVillageA("Homburg", "Finkenweg")
        assertEquals(true, eeParser.getValidFlag())
        // invalid
        // eeParser.checkRoadExistByRoadNameAndVillageA("XXX", "Finkenweg")
        // assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testCheckVillageExist() {
        assertEquals(true, eeParser.getValidFlag())
        // valid
        Map.checkVillageExist("Homburg")
        // assertEquals(true, eeParser.getValidFlag())
        // invalid
        Map.checkVillageExist("XXX")
        // assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testValidateOptionmalEventAttributeRoadClosureA() {
        assertEquals(true, eeParser.getValidFlag())
        // valid: has  source and target vertex, NO onewaystreet,factor roadTypes and vehicleID
        val jO = JSONObject()
        jO.put("source", 1)
        jO.put("target", 2)
        eeParser.validateOptionmalEventAttributeRoadClosure(jO, EventType.ROAD_CLOSURE)
        assertEquals(true, eeParser.getValidFlag())
    }

    @Test
    fun testValidateOptionmalEventAttributeRoadClosureB() {
        assertEquals(true, eeParser.getValidFlag())
        // valid: has  source and target vertex, NO onewaystreet,factor roadTypes and vehicleID
        // more onewaystreet
        val jO = JSONObject()
        jO.put("source", 1)
        jO.put("target", 2)
        jO.put("oneWayStreet", true)
        eeParser.validateOptionmalEventAttributeRoadClosure(jO, EventType.ROAD_CLOSURE)
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testValidateOptionmalEventAttributeRoadClosureC() {
        assertEquals(true, eeParser.getValidFlag())
        // valid: has  source and target vertex, NO onewaystreet,factor roadTypes and vehicleID
        // more factor
        val jO = JSONObject()
        jO.put("source", 1)
        jO.put("target", 2)
        jO.put("factor", -2)
        eeParser.validateOptionmalEventAttributeRoadClosure(jO, EventType.ROAD_CLOSURE)
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testValidateOptionmalEventAttributeRoadClosureD() {
        assertEquals(true, eeParser.getValidFlag())
        // valid: has  source and target vertex, NO onewaystreet,factor roadTypes and vehicleID
        // more roadtypes
        val jO = JSONObject()
        jO.put("source", 1)
        jO.put("target", 2)
        jO.put("roadTypes", PrimaryRoadType.COUNTY_ROAD)
        eeParser.validateOptionmalEventAttributeRoadClosure(jO, EventType.ROAD_CLOSURE)
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testValidateOptionmalEventAttributeRoadClosureE() {
        assertEquals(true, eeParser.getValidFlag())
        // valid: has  source and target vertex, NO onewaystreet,factor roadTypes and vehicleID
        // more vehicleId
        val jO = JSONObject()
        jO.put("source", 1)
        jO.put("target", 2)
        jO.put("vehicleID", 10)
        eeParser.validateOptionmalEventAttributeRoadClosure(jO, EventType.ROAD_CLOSURE)
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testValidateOptionmalEventAttributeRoadClosureF() {
        assertEquals(true, eeParser.getValidFlag())
        // valid: has  source and target vertex, NO onewaystreet,factor roadTypes and vehicleID
        // vertex
        val jO = JSONObject()
        jO.put("source", 1)
        // jO.put("target", 2)
        eeParser.validateOptionmalEventAttributeRoadClosure(jO, EventType.ROAD_CLOSURE)
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testValidateOptionmalEventAttributeRoadClosureG() {
        assertEquals(true, eeParser.getValidFlag())
        // valid: has  source and target vertex, NO onewaystreet,factor roadTypes and vehicleID
        // vertex
        val jO = JSONObject()
        // jO.put("source", 1)
        jO.put("target", 2)
        eeParser.validateOptionmalEventAttributeRoadClosure(jO, EventType.ROAD_CLOSURE)
        assertEquals(false, eeParser.getValidFlag())
    }

    @Test
    fun testValidateOptionmalEventAttributeRoadClosureH() {
        assertEquals(true, eeParser.getValidFlag())
        // valid: has  source and target vertex, NO onewaystreet,factor roadTypes and vehicleID
        // vertex, before already checked by optInt, one of them is not 0/null/string
        // what if given one is 0, one is not?, in this method check another one??
        val jO = JSONObject()
        jO.put("source", "1")
        jO.put("target", "abc")
        eeParser.validateOptionmalEventAttributeRoadClosure(jO, EventType.ROAD_CLOSURE)
        assertEquals(false, eeParser.getValidFlag())
    }
}
