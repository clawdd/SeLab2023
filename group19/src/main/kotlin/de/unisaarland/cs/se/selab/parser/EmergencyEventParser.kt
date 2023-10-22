package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.Logger
import de.unisaarland.cs.se.selab.emergency.AccidentEmergencyFactory
import de.unisaarland.cs.se.selab.emergency.CrimeEmergencyFactory
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.emergency.FireEmergencyFactory
import de.unisaarland.cs.se.selab.emergency.MedicalEmergencyFactory
import de.unisaarland.cs.se.selab.enumtype.EmergencyType
import de.unisaarland.cs.se.selab.enumtype.EventType
import de.unisaarland.cs.se.selab.event.Event
import de.unisaarland.cs.se.selab.event.EventFactory
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.PrimaryRoadType
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.Vehicle
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.NumberFormatException

// id+tick
/**
 *parser
 */
class EmergencyEventParser(fileName: String) {

    val fileName = fileName
    val parsedEmergencies = mutableListOf<Emergency>()
    val parsedEvents = mutableListOf<Event>()

    // var schema = getSchema(VehicleBaseParser::class.java, "config.schema")
    private var validFlag = true

    val jsonScenarioString = File(fileName).readText()
    val jsonScenarioObject = JSONObject(jsonScenarioString)

    // events
    val eventsArray: JSONArray = jsonScenarioObject.getJSONArray("events")
    val eventFactory = EventFactory()

    /**
     *...event type
     */
    fun parseEventType(eventTypeStr: String): EventType {
        return when (eventTypeStr) {
            "TRAFFIC_JAM" -> EventType.TRAFFIC_JAM
            "VEHICLE_UNAVAILABLE" -> EventType.VEHICLE_UNAVAILABLE
            "RUSH_HOUR" -> EventType.RUSH_HOUR
            "ROAD_CLOSURE" -> EventType.ROAD_CLOSURE
            "CONSTRUCTION_SITE" -> EventType.CONSTRUCTION_SITE
            else -> {
                // Logger.logUnsuccessfulParse(fileName)
                validFlag = false
                return EventType.RUSH_HOUR
                // throw IllegalArgumentException("Unknown eventType: $eventTypeStr")
            }
        }
    }

    /**
     *...emergency type
     */
    fun parseEmergencyType(emergencyTypeStr: String): EmergencyType {
        return when (emergencyTypeStr) {
            "ACCIDENT" -> EmergencyType.ACCIDENT
            "CRIME" -> EmergencyType.CRIME
            "FIRE" -> EmergencyType.FIRE
            "MEDICAL" -> EmergencyType.MEDICAL
            else -> {
                // Logger.logUnsuccessfulParse(fileName)
                validFlag = false
                return EmergencyType.ACCIDENT
                // throw IllegalArgumentException("Unknown emergencyType: $emergencyTypeStr")
            }
        }
    }

    /**
     *event...check id>=0, tick>0,handleTime>0,maxDuration>0, severity =1, =2, =3
     * and all the necessary attribute exists
     */
    fun createEmIntAttrList(
        id: String?,
        tick: String?,
        handleTime: String?,
        maxDuration: String?,
        severity: String?
    ): List<Int> {
        // not null then return true
        // if (id == null || tick == null || handleTime == null) {
        // validFlag = false
        // Logger.logUnsuccessfulParse(fileName)
        // throw IllegalArgumentException("emergency: missing id, tick, or handletime")
        // }
        // if (maxDuration == null || severity == null) {
        // validFlag = false
        // Logger.logUnsuccessfulParse(fileName)
        // throw IllegalArgumentException("emergency: missing maxDuration or serverity")
        // }
        val tempId = id?.toIntOrNull() ?: -1
        val tempTick = tick?.toIntOrNull() ?: -1
        if (tempId < 0 || tempTick <= 0) {
            validFlag = false
            // Logger.logUnsuccessfulParse(fileName)
            // throw IllegalArgumentException("emergency: negative id, tick")
        }
        val tempHandleTime = handleTime?.toIntOrNull() ?: -1
        val tempMaxDuration = maxDuration?.toIntOrNull() ?: -1
        if (tempHandleTime <= 0 || tempMaxDuration <= 0 || tempMaxDuration <= tempHandleTime) {
            validFlag = false
            // Logger.logUnsuccessfulParse(fileName)
            // throw IllegalArgumentException("emergency: negative handle time and max duration")
        }
        val tempSeverity = severity?.toIntOrNull() ?: -1
        if (tempSeverity != Constants.SEVERITY_ONE &&
            tempSeverity != Constants.SEVERITY_TWO &&
            tempSeverity != Constants.SEVERITY_THREE
        ) {
            validFlag = false
            // throw IllegalArgumentException("emergency: wrong severity")
        }
        return listOf(tempId, tempTick, tempHandleTime, tempMaxDuration, tempSeverity)
    }

    /**
     *event...
     */
    fun createEventIntAttributesList(
        id: String?,
        startTick: String?,
        duration: String?,
        // endTick: Int
    ): List<Int> {
        val tempId = id?.toIntOrNull() ?: -1
        val tempTick = startTick?.toIntOrNull() ?: -1
        val tempDuration = duration?.toIntOrNull() ?: -1
        if (id == null || startTick == null || duration == null) {
            validFlag = false
            // Logger.logUnsuccessfulParse(fileName)
            // throw IllegalArgumentException("event: missing id, startTick, or duration")
        }
        if (id != null && startTick != null && duration != null) {
            if (tempId < 0 || tempTick < 0 || tempDuration < 1) {
                validFlag = false
                // Logger.logUnsuccessfulParse(fileName)
                // throw IllegalArgumentException("event: negative id,start tick, duration")
            }
        }
        return listOf(tempId, tempTick, tempDuration, tempTick + tempDuration)
    }

    /**
     *event...try {
     *                 val roadTypeEnum = PrimaryRoadType.valueOf(roadTypeStr)
     *                 roadTypesEnums.add(roadTypeEnum)
     *             } catch (e: IllegalArgumentException) {
     *                 Logger.logUnsuccessfulParse(fileName)
     *                 throw e
     *             }
     */
    fun helperForRushHour(roadTypesArray: JSONArray, roadTypesEnums: MutableList<PrimaryRoadType>) {
        for (i in 0 until roadTypesArray.length()) {
            val roadTypeStr = roadTypesArray.optString(i)
            // val roadTypeEnum = PrimaryRoadType.valueOf(roadTypeStr)
            when (roadTypeStr) {
                "COUNTY_ROAD" -> {
                    roadTypesEnums.add(PrimaryRoadType.COUNTY_ROAD)
                }

                "MAIN_STREET" -> {
                    roadTypesEnums.add(PrimaryRoadType.MAIN_STREET)
                }

                "SIDE_STREET" -> {
                    roadTypesEnums.add(PrimaryRoadType.SIDE_STREET)
                }

                else -> {
                    // Logger.logUnsuccessfulParse(fileName)
                    validFlag = false
                }
            }
        }
    }

    /**
     *...event type, todo i think if the necessary attribute is invalid,
     * then it should give a log before throw an exception
     */
    fun parseEvents(vehiclesList: List<Vehicle>): List<Event> {
        if (eventsArray.isEmpty) {
            validFlag = false
        }
        for (i in 0 until eventsArray.length()) { // all the necessary attribute
            val eventObj = eventsArray.getJSONObject(i)
            val id = eventObj.optString(Constants.KEY_ID)
            val eventType = parseEventType(eventObj.optString(Constants.KEY_TYPE))
            val tick = eventObj.optString(Constants.KEY_TICK)
            val duration = eventObj.optString(Constants.KEY_DURATION)
            val intAttributeList: List<Int> = createEventIntAttributesList(id, tick, duration) // optional attribute
            val factor = eventObj.optInt(Constants.KEY_FACTOR)
            var roadST: List<Vertex> = emptyList()
            if (eventObj.optInt(Constants.KEY_SOURCE) != 0 || eventObj.optInt(Constants.KEY_TARGET) != 0) {
                val sourceVertex = Map.getVertexByIDParser(eventObj.optInt(Constants.KEY_SOURCE))
                val targetVertex = Map.getVertexByIDParser(eventObj.optInt(Constants.KEY_TARGET))
                if (Map.findRoadByVertexParser(sourceVertex, targetVertex) == null ||
                    sourceVertex == null ||
                    targetVertex == null
                ) {
                    validFlag = false
                    return parsedEvents
                } else {
                    roadST = listOf(sourceVertex, targetVertex)
                }
            }
            val oneWayStreet = eventObj.optBoolean(Constants.KEY_ONE_WAY_STREET)
            val roadTypesArray = eventObj.optJSONArray(Constants.KEY_ROAD_TYPES)
            val roadTypesEnums = mutableListOf<PrimaryRoadType>()
            if (eventType == EventType.RUSH_HOUR) {
                helperForRushHour(roadTypesArray, roadTypesEnums)
            }
            val unavailableVehicleId = eventObj.optInt(Constants.KEY_VEHICLE_ID)
            helperForParsingEvents(
                eventType,
                eventObj,
                intAttributeList,
                roadST,
                oneWayStreet,
                factor,
                roadTypesEnums,
                vehiclesList,
                unavailableVehicleId
            ) // when end
        }
        val idSet = parsedEvents.map { it.id }.toSet()
        if (idSet.size != parsedEvents.size) {
            validFlag = false //  IllegalArgumentException("events id not unique")
        }
        return parsedEvents
    }

    private fun helperForParsingEvents(
        eventType: EventType,
        eventObj: JSONObject,
        intAttributeList: List<Int>,
        roadST: List<Vertex>,
        oneWayStreet: Boolean,
        factor: Int,
        roadTypesEnums: MutableList<PrimaryRoadType>,
        vehiclesList: List<Vehicle>,
        unavailableVehicleId: Int
    ) {
        when (eventType) {
            EventType.CONSTRUCTION_SITE -> {
                validateOptionmalEventAttributeConstructionSite(eventObj, eventType)
                val event = eventFactory.createConstructionSite(intAttributeList, roadST, oneWayStreet, factor)
                parsedEvents.add(event)
            }

            EventType.ROAD_CLOSURE -> {
                validateOptionmalEventAttributeRoadClosure(eventObj, eventType)
                val event = eventFactory.createClosedRoad(intAttributeList, roadST)
                parsedEvents.add(event)
            }

            EventType.RUSH_HOUR -> {
                validateOptionmalEventAttributeRushHour(eventObj, eventType)
                val event = eventFactory.createRushHour(intAttributeList, roadTypesEnums, factor)
                parsedEvents.add(event)
            }

            EventType.TRAFFIC_JAM -> {
                validateOptionmalEventAttributeTrafficJam(eventObj, eventType)
                val event = eventFactory.createTrafficJam(intAttributeList, roadST, factor)
                parsedEvents.add(event)
            }

            EventType.VEHICLE_UNAVAILABLE -> {
                validateOptionmalEventAttributeVechileUnavailable(eventObj, eventType, vehiclesList)
                val event = eventFactory.createVehicleUnavailable(intAttributeList, unavailableVehicleId)
                parsedEvents.add(event)
            }
        }
    }

    val emergenciesArray: JSONArray = jsonScenarioObject.getJSONArray("emergencyCalls")
    val fireEmFactory = FireEmergencyFactory()
    val medicalEmFactory = MedicalEmergencyFactory()
    val crimeEmFactory = CrimeEmergencyFactory()
    val accidentEmFactory = AccidentEmergencyFactory()

    /**
     *...parser emergency
     */
    fun parseEmergencies(): List<Emergency> {
        if (emergenciesArray.isEmpty) { validFlag = false }
        for (i in 0 until emergenciesArray.length()) { // all the necessary attribute
            val emergencyObj = emergenciesArray.getJSONObject(i)
            val id = emergencyObj.optString("id")
            val tick = emergencyObj.optString("tick")
            if (!Map.checkVillageExist(emergencyObj.optString(Constants.KEY_VILLAGE)) ||
                Map.checkRoadExistByRoadNameAndVillage(
                        emergencyObj.optString(Constants.KEY_VILLAGE),
                        emergencyObj.optString(Constants.KEY_ROAD_NAME)
                    ) == null
            ) {
                validFlag = false
                return parsedEmergencies
            }
            val emergencyType = parseEmergencyType(emergencyObj.optString("emergencyType"))
            val severity = emergencyObj.optString("severity")
            val handleTime = emergencyObj.optString("handleTime")
            val maxDuration = emergencyObj.optString("maxDuration")
            val intAttributeList: List<Int> = createEmIntAttrList(id, tick, handleTime, maxDuration, severity)
            try {
                when (emergencyType) {
                    EmergencyType.ACCIDENT -> parseSingleAccidentEmergency(
                        intAttributeList,
                        emergencyObj.optString(Constants.KEY_VILLAGE),
                        emergencyObj.optString(Constants.KEY_ROAD_NAME),
                        severity.toInt()
                    )
                    EmergencyType.FIRE -> parseSingleFireEmergency(
                        intAttributeList,
                        emergencyObj.optString(Constants.KEY_VILLAGE),
                        emergencyObj.optString(Constants.KEY_ROAD_NAME),
                        severity.toInt()
                    )
                    EmergencyType.MEDICAL -> parseSingleMedicalEmergency(
                        intAttributeList,
                        emergencyObj.optString(Constants.KEY_VILLAGE),
                        emergencyObj.optString(Constants.KEY_ROAD_NAME),
                        severity.toInt()
                    )
                    EmergencyType.CRIME -> parseSingleCrimeEmergency(
                        intAttributeList,
                        emergencyObj.optString(Constants.KEY_VILLAGE),
                        emergencyObj.optString(Constants.KEY_ROAD_NAME),
                        severity.toInt()
                    )
                    EmergencyType.FACTORY -> Logger.logUnsuccessfulParse(fileName)
                }
            } catch (ex: NumberFormatException) {
                validFlag = false
                return parsedEmergencies
            }
        }
        val idSet = parsedEmergencies.map { it.id }.toSet()
        if (idSet.size != parsedEmergencies.size) {
            validFlag = false
        }
        return parsedEmergencies
    }

    /**
     *emergency...
     */
    fun parseSingleAccidentEmergency(
        intAttributeList: List<Int>,
        villageStr: String,
        roadNameStr: String,
        severity: Int
    ) {
        when (severity) {
            1 -> {
                val accidentEmergency =
                    accidentEmFactory.createAccidentLevelOne(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(accidentEmergency)
            }

            2 -> {
                val accidentEmergency =
                    accidentEmFactory.createAccidentLevelTwo(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(accidentEmergency)
            }

            3 -> {
                val accidentEmergency =
                    accidentEmFactory.createAccidentLevelThree(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(accidentEmergency)
            }

            else -> {
                // Logger.logUnsuccessfulParse(fileName)
                // throw IllegalArgumentException("wrong severity for emergency accident")
                validFlag = false
            }
        }
    }

    /**
     *emergency...
     */
    fun parseSingleCrimeEmergency(
        intAttributeList: List<Int>,
        villageStr: String,
        roadNameStr: String,
        severity: Int
    ) {
        when (severity) {
            1 -> {
                val crimeEmergency = crimeEmFactory.createCrimeLevelOne(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(crimeEmergency)
            }

            2 -> {
                val crimeEmergency = crimeEmFactory.createCrimeLevelTwo(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(crimeEmergency)
            }

            3 -> {
                val crimeEmergency = crimeEmFactory.createCrimeLevelThree(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(crimeEmergency)
            }

            else -> {
                // throw IllegalArgumentException("wrong severity for emergency crime")
                validFlag = false
            }
        }
    }

    /**
     *emergency...
     */
    fun parseSingleFireEmergency(
        intAttributeList: List<Int>,
        villageStr: String,
        roadNameStr: String,
        severity: Int
    ) {
        when (severity) {
            1 -> {
                val fireEmergency = fireEmFactory.createFireLevelOne(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(fireEmergency)
            }

            2 -> {
                val fireEmergency = fireEmFactory.createFireLevelTwo(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(fireEmergency)
            }

            3 -> {
                val fireEmergency = fireEmFactory.createFireLevelThree(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(fireEmergency)
            }

            else -> {
                // Logger.logUnsuccessfulParse(fileName)
                // throw IllegalArgumentException("wrong severity for emergency fire")
                validFlag = false
            }
        }
    }

    /**
     *emergency...
     */
    fun parseSingleMedicalEmergency(
        intAttributeList: List<Int>,
        villageStr: String,
        roadNameStr: String,
        severity: Int
    ) {
        when (severity) {
            1 -> {
                val medicalEmergency =
                    medicalEmFactory.createMedicalLevelOne(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(medicalEmergency)
            }

            2 -> {
                val medicalEmergency =
                    medicalEmFactory.createMedicalLevelTwo(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(medicalEmergency)
            }

            3 -> {
                val medicalEmergency =
                    medicalEmFactory.createMedicalLevelThree(intAttributeList, villageStr, roadNameStr)
                this.parsedEmergencies.add(medicalEmergency)
            }

            else -> {
                // Logger.logUnsuccessfulParse(fileName)
                // throw IllegalArgumentException("wrong severity for emergency medical")
                validFlag = false
            }
        }
    }

    /**
     * check whether there are more unwanted attribute provided
     */
    fun validateOptionmalEventAttributeRoadClosure(jO: JSONObject, eT: EventType) {
        if (eT == EventType.ROAD_CLOSURE) {
            // has  source and target vertex, NO onewaystreet,factor roadTypes and vehicleID
            if (jO.optString(Constants.KEY_SOURCE).toIntOrNull() == null ||
                jO.optString(Constants.KEY_TARGET).toIntOrNull() == null
            ) {
                // Logger.logUnsuccessfulParse(fileName)
                // throw IllegalArgumentException("event ROAD_CLOSURE: missing vertex")
                validFlag = false
            } // continue with throw exception if attribute is more than needed
            if (jO.optString(Constants.KEY_FACTOR) != "" || jO.optString(Constants.KEY_ONE_WAY_STREET) != "") {
                // Logger.logUnsuccessfulParse(fileName)
                // throw IllegalArgumentException("event ROAD_CLOSURE: wrong  factor or one way street provided")
                validFlag = false
            }
            if (jO.optString(Constants.KEY_VEHICLE_ID) != "" || jO.optString(Constants.KEY_ROAD_TYPES) != "") {
                // Logger.logUnsuccessfulParse(fileName),done
                // throw IllegalArgumentException("event ROAD_CLOSURE: wrong vehicleid or roadtypes provided")
                validFlag = false
            }
        }
    }

    /**
     * check vehicle rh
     */
    fun validateOptionmalEventAttributeRushHour(jO: JSONObject, eT: EventType) {
        if (eT == EventType.RUSH_HOUR) { // has factor(opt), roadtypes, No source and target vertex, oneway, vehicleID
            if (jO.optString(Constants.KEY_ROAD_TYPES) == "") {
                // roadtype not exist, invalid
                // throw IllegalArgumentException("event RUSH_HOUR : missing roadtypes")
                validFlag = false
            } // continue with throw exception if attribute is more than needed
            if (jO.optString(Constants.KEY_SOURCE) != "" || jO.optString(Constants.KEY_TARGET) != "") {
                // source or target exist, invalid
                // throw IllegalArgumentException("event RUSH_HOUR: wrong vertex provided")
                validFlag = false
            }
            if (jO.optString(Constants.KEY_VEHICLE_ID) != "" || jO.optString(Constants.KEY_ONE_WAY_STREET) != "") {
                // vehicle id or onewaystreet exist, invalid
                // throw IllegalArgumentException("event RUSH_HOUR : wrong vehicleid or onewaystreet provided")
                validFlag = false
            }
            if (jO.optString(Constants.KEY_FACTOR).toIntOrNull() != null && jO.optInt(Constants.KEY_FACTOR) < 1) {
                // factor exist and is a number and <1, invalid
                // throw IllegalArgumentException("RUSH_HOUR : factor < 1 ")
                validFlag = false
            }
        }
    }

    /**
     *
     * check cs
     */
    fun validateOptionmalEventAttributeConstructionSite(jO: JSONObject, eT: EventType) {
        if (eT == EventType.CONSTRUCTION_SITE) {
            // has factor, oneway,source & target vertex, NO  roadTypes and vehicleID
            if (jO.optString(Constants.KEY_FACTOR) == "" || jO.optString(Constants.KEY_ONE_WAY_STREET) == "") {
                // miss factor or onewaystreet, invalid
                // throw IllegalArgumentException("event CONSTRUCTION_SITE: missing factor or one way street")
                validFlag = false
            }
            // if (jO.optString(KEY_SOURCE) == "" || jO.optString(KEY_TARGET) == "") {
            // Logger.logUnsuccessfulParse(fileName)
            // throw IllegalArgumentException("event CONSTRUCTION_SITE: missing vertex")
            //    validFlag = false
            // } // continue with throw exception if attribute is more than needed
            if (jO.optString(Constants.KEY_VEHICLE_ID) != "" || jO.optString(Constants.KEY_ROAD_TYPES) != "") {
                // Logger.logUnsuccessfulParse(fileName)
                // throw IllegalArgumentException("event CONSTRUCTION_SITE: wrong vehicleid or roadtypes provided")
                validFlag = false
            }
            if (jO.optInt(Constants.KEY_FACTOR) < 1) {
                // invalid factor number; check oneway exist, and can only be true or false
                // throw IllegalArgumentException("CONSTRUCTION_SITE : factor < 1 ")
                validFlag = false
            }
            if (!jO.has(Constants.KEY_ONE_WAY_STREET)) {
                // invalid factor number; check oneway exist, and can only be true or false
                // throw IllegalArgumentException("CONSTRUCTION_SITE : factor < 1 ")
                validFlag = false
            }
            if (jO.optString(Constants.KEY_SOURCE).toIntOrNull() == null ||
                jO.optString(Constants.KEY_TARGET).toIntOrNull() == null
            ) {
                // check sourse and target exist, and are Int
                // throw IllegalArgumentException("event ROAD_CLOSURE: missing vertex").done?
                validFlag = false
            }
        }
    }

    /**
     * check tj
     */
    fun validateOptionmalEventAttributeTrafficJam(jO: JSONObject, eT: EventType) {
        if (eT == EventType.TRAFFIC_JAM) {
            // has factor, source and target vertex, NO onewaystreet, roadTypes and vehicleID
            if (jO.optString(Constants.KEY_SOURCE) == "" ||
                jO.optString(Constants.KEY_TARGET) == "" ||
                jO.optString(Constants.KEY_FACTOR) == ""
            ) {
                // missing any of source, target, factor, invalid
                // throw IllegalArgumentException("TRAFFIC_JAM : missing vertices or factor")
                validFlag = false
            } // continue with throw exception if attribute is more than needed
            if (jO.optString(Constants.KEY_VEHICLE_ID) != "" ||
                jO.optString(Constants.KEY_ROAD_TYPES) != "" ||
                jO.optString(Constants.KEY_ONE_WAY_STREET) != ""
            ) {
                // more roadtype, vehicle id , oneway, invalid
                // throw IllegalArgumentException("TRAFFIC_JAM : wrong vehicleid or roadtypes,onewaystreet provided")
                validFlag = false
            }
            if (jO.optInt(Constants.KEY_FACTOR) < 1) {
                // Logger.logUnsuccessfulParse(fileName)
                // throw IllegalArgumentException("TRAFFIC_JAM : factor < 1 ")
                validFlag = false
            }
            if (jO.optString(Constants.KEY_SOURCE).toIntOrNull() == null ||
                jO.optString(Constants.KEY_TARGET).toIntOrNull() == null
            ) {
                // check vertices are exist and are number
                // throw IllegalArgumentException("Traffic Jam, not valid vertices")
                validFlag = false
            }
        }
    }

    /**
     * check vehicle unav
     */
    fun validateOptionmalEventAttributeVechileUnavailable(jO: JSONObject, eT: EventType, vehiclesList: List<Vehicle>) {
        if (eT == EventType.VEHICLE_UNAVAILABLE) { // has vehicleID, NO factor, roadtypes, source & target vertex,oneway
            if (jO.optString(Constants.KEY_VEHICLE_ID) == "") {
                // Logger.logUnsuccessfulParse(fileName)
                // throw IllegalArgumentException("event VEHICLE_UNAVAIABLE : missing vehicleID")
                validFlag = false
            }
            if (jO.optString(Constants.KEY_SOURCE) != "" ||
                jO.optString(Constants.KEY_TARGET) != "" ||
                jO.optString(Constants.KEY_FACTOR) != ""
            ) {
                // throw IllegalArgumentException("event VEHICLE_UNAVAIABLE : wrong vertices or factor provided")
                validFlag = false
            }
            // continue with throw exception if attribute is more than needed
            if (jO.optString(Constants.KEY_ROAD_TYPES) != "" || jO.optString(Constants.KEY_ONE_WAY_STREET) != "") {
                // Logger.logUnsuccessfulParse(fileName)
                // throw IllegalArgumentException("event VEHICLE_UNAVAIABLE : wrong roadtypes,onewaystreet provided")
                validFlag = false
            }
            if (jO.optString(Constants.KEY_VEHICLE_ID).toIntOrNull() == null) {
                // check vehicle id is a valid number
                validFlag = false
                // throw IllegalArgumentException("event VEHICLE_UNAVAIABLE : missing vehicleID")
            }
            checkUnavailableVehicleExist(jO, vehiclesList)
        }
    }

    private fun checkUnavailableVehicleExist(
        jO: JSONObject,
        vehiclesList: List<Vehicle>
    ) {
        val tempVehicleId = jO.getInt(Constants.KEY_VEHICLE_ID)
        val existVehicle = vehiclesList.any { it.vehicleId == tempVehicleId }
        if (!existVehicle) {
            validFlag = false
        }
    }

    /**
     * get flag to check valid
     */
    fun getValidFlag(): Boolean {
        return validFlag
    }
}
