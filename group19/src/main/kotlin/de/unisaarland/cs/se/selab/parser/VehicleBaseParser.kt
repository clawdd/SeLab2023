package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.base.Base
import de.unisaarland.cs.se.selab.base.BaseFactory
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.Vehicle
import de.unisaarland.cs.se.selab.vehicle.VehicleFireFactory
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import de.unisaarland.cs.se.selab.vehicle.VehicleMedicalFactory
import de.unisaarland.cs.se.selab.vehicle.VehiclePoliceFactory
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.NumberFormatException

/**
 *to parse the assets
 */
class VehicleBaseParser(fileName: String) {
    val fileName = fileName
    val parsedVehicles = mutableListOf<Vehicle>()
    val parsedBases = mutableListOf<Base>()
    private var validFlag = true
    // private val vehicleValidator: VehicleValidator = VehicleValidator(this.validFlag, this)

    val baseFactory = BaseFactory()
    val vehicleFireFactory = VehicleFireFactory()
    val vehiclePoliceFactory = VehiclePoliceFactory()
    val vehicleMedicalFactory = VehicleMedicalFactory()

    val jsonAssetsString = File(fileName).readText()
    val jsonAssetsObject = JSONObject(jsonAssetsString)

    // base
    val basesArray: JSONArray = jsonAssetsObject.getJSONArray("bases")

    // vehicles
    val vehiclesArray: JSONArray = jsonAssetsObject.getJSONArray("vehicles")

    /**
     *to parse the bases
     */
    fun parseBases(): List<Base> {
        for (i in 0 until basesArray.length()) {
            // all the necessary attribute,need to check whether exist
            val baseObj = basesArray.getJSONObject(i)
            val id = baseObj.optString(Constants.KEY_ID)
            val baseTypeStr = baseObj.optString("baseType")
            val baseType = parseBaseType(baseTypeStr)
            val location = baseObj.optString("location")
            val vertexForLocation = Map.getVertexByIDParser(location.toIntOrNull())
            if (vertexForLocation == null) {
                validFlag = false
                return parsedBases
            }
            val staff = baseObj.optString("staff")
            if (helperForParsingBase(baseType, baseObj, id, vertexForLocation, staff)) return parsedBases
        }
        val mybaseValidater = BaseValidator(this.validFlag, this)
        mybaseValidater.validateBases()
        if (!mybaseValidater.getBaseValidFlag()) {
            validFlag = mybaseValidater.getBaseValidFlag()
        }
        return checkUniqueIdBases(parsedBases)
    }

    private fun helperForParsingBase(
        baseType: BaseType,
        baseObj: JSONObject,
        id: String,
        vertexForLocation: Vertex?,
        staff: String
    ): Boolean {
        if (vertexForLocation == null) {
            validFlag = false
            return false
        }
        try {
            when (baseType) {
                BaseType.HOSPITAL -> {
                    val doctors = baseObj.optString("doctors")
                    if (doctors.toIntOrNull() == null) {
                        validFlag = false
                    }
                    val base = baseFactory.createHospital(
                        id.toInt(),
                        vertexForLocation,
                        staff.toInt(),
                        doctors.toInt()
                    )
                    parsedBases.add(base)
                }

                BaseType.FIRE_STATION -> {
                    val base =
                        baseFactory.createFireStation(
                            id.toInt(),
                            vertexForLocation,
                            staff.toInt()
                        )
                    parsedBases.add(base)
                }

                BaseType.POLICE_STATION -> {
                    val dogs = baseObj.optString("dogs")
                    if (dogs.toIntOrNull() == null) {
                        validFlag = false
                    }
                    val base =
                        baseFactory.createPoliceStation(
                            id.toInt(),
                            vertexForLocation,
                            staff.toInt(),
                            dogs.toInt()
                        )
                    parsedBases.add(base)
                }
            }
        } catch (ex: NumberFormatException) {
            validFlag = false
            return true
        }
        return false
    }

    /**
     *to parse the vehicles
     */
    fun parseVehicles(): List<Vehicle> {
        for (i in 0 until vehiclesArray.length()) {
            val vehicleObj = vehiclesArray.getJSONObject(i)
            val vehicleType = parseVehicleType(vehicleObj.optString("vehicleType"))
            try {
                createVehicleIntAttributesList(
                    vehicleObj.optString("id").toInt(),
                    vehicleObj.optString("baseID").toInt(),
                    vehicleObj.optInt("staffCapacity"),
                    vehicleObj.optInt("vehicleHeight")
                )
                val intAttributeList = VehicleIntParameters(
                    vehicleObj.optString("id").toInt(),
                    vehicleObj.optString("baseID").toInt(),
                    vehicleObj.optInt("staffCapacity"),
                    vehicleObj.optInt("vehicleHeight")
                )
                helperForVehicle(vehicleType, vehicleObj, intAttributeList)
            } catch (ex: NumberFormatException) {
                validFlag = false
                return parsedVehicles
            }
        }
        return checkUniqueIdVehicles(parsedVehicles)
    }

    private fun helperForVehicle(
        vehicleType: VehicleType,
        vehicleObj: JSONObject,
        intAttributeList: VehicleIntParameters
    ) {
        when (vehicleType) {
            VehicleType.FIRE_TRUCK_WATER -> {
                validateWrongLadderOrCriminalOrWater(vehicleObj, vehicleType)
                val waterCapacity = checkOptWater(vehicleObj.optString(Constants.WATER_CAPACITY))
                val vehicle =
                    vehicleFireFactory.createFireTruckWater(intAttributeList, waterCapacity)
                parsedVehicles.add(vehicle)
            }

            VehicleType.FIRE_TRUCK_LADDER -> {
                validateWrongLadderOrCriminalOrWater(vehicleObj, vehicleType)
                val ladderLength = checkOptLadder(vehicleObj.optString(Constants.LADDER_HEIGHT))
                val vehicle =
                    vehicleFireFactory.createFireTruckLadder(intAttributeList, ladderLength)
                parsedVehicles.add(vehicle)
            }

            VehicleType.FIRE_TRUCK_TECHNICAL -> {
                validateWrongLadderOrCriminalOrWater(vehicleObj, vehicleType)
                parsedVehicles.add(vehicleFireFactory.createFireTruckTechnical(intAttributeList))
            }

            VehicleType.FIREFIGHTER_TRANSPORTER -> {
                validateWrongLadderOrCriminalOrWater(vehicleObj, vehicleType)
                parsedVehicles.add(vehicleFireFactory.createFirefigherTransporter(intAttributeList))
            }

            VehicleType.POLICE_CAR -> {
                validateWrongLadderOrCriminalOrWater(vehicleObj, vehicleType) // police car
                val criminalCapacity =
                    checkOptCriminal(vehicleObj.optString(Constants.CRIMINAL_CAPACITY))
                val vehicle =
                    vehiclePoliceFactory.createPoliceCar(intAttributeList, criminalCapacity)
                parsedVehicles.add(vehicle)
            }

            VehicleType.POLICE_MOTORCYCLE -> {
                validateWrongLadderOrCriminalOrWater(vehicleObj, vehicleType)
                parsedVehicles.add(vehiclePoliceFactory.createPoliceMotorcycle(intAttributeList))
            }

            VehicleType.K9_POLICE_CAR -> {
                validateWrongLadderOrCriminalOrWater(vehicleObj, vehicleType)
                parsedVehicles.add(vehiclePoliceFactory.createK9PoliceCar(intAttributeList))
            }

            VehicleType.AMBULANCE -> {
                validateWrongLadderOrCriminalOrWater(vehicleObj, vehicleType)
                parsedVehicles.add(vehicleMedicalFactory.createAmbulance(intAttributeList))
            }

            VehicleType.EMERGENCY_DOCTOR_CAR -> {
                validateWrongLadderOrCriminalOrWater(vehicleObj, vehicleType)
                parsedVehicles.add(vehicleMedicalFactory.createDoctorCar(intAttributeList))
            }
        }
    }

    /**
     * helper function , parse vehicles too long
     */
    fun helperParseVehicles(): List<Vehicle> {
        parseVehicles()
        val myVehiclesValidater = VehicleValidator(this.validFlag, this)
        myVehiclesValidater.validateVehicles()
        if (!myVehiclesValidater.getVehicleValidFlag()) {
            validFlag = myVehiclesValidater.getVehicleValidFlag()
        }
        return parsedVehicles
    }

    /**
     *to parse the basetype, enum
     * @param baseTypeStr: take a string convert to enumtype
     * @return
     */
    fun parseBaseType(baseTypeStr: String): BaseType {
        return when (baseTypeStr) {
            "FIRE_STATION" -> BaseType.FIRE_STATION
            "POLICE_STATION" -> BaseType.POLICE_STATION
            "HOSPITAL" -> BaseType.HOSPITAL
            else -> {
                // Logger.logUnsuccessfulParse(fileName)
                validFlag = false
                return BaseType.HOSPITAL
                // throw IllegalArgumentException("Unknown baseType: $baseTypeStr")
            }
        }
    }

    /**
     *to parse string to enumtype
     */
    fun parseVehicleType(vehicleTypeStr: String): VehicleType {
        return when (vehicleTypeStr) {
            "AMBULANCE" -> VehicleType.AMBULANCE
            "EMERGENCY_DOCTOR_CAR" -> VehicleType.EMERGENCY_DOCTOR_CAR
            "POLICE_CAR" -> VehicleType.POLICE_CAR
            "K9_POLICE_CAR" -> VehicleType.K9_POLICE_CAR
            "POLICE_MOTORCYCLE" -> VehicleType.POLICE_MOTORCYCLE
            "FIRE_TRUCK_LADDER" -> VehicleType.FIRE_TRUCK_LADDER
            "FIRE_TRUCK_TECHNICAL" -> VehicleType.FIRE_TRUCK_TECHNICAL
            "FIRE_TRUCK_WATER" -> VehicleType.FIRE_TRUCK_WATER
            "FIREFIGHTER_TRANSPORTER" -> VehicleType.FIREFIGHTER_TRANSPORTER
            else -> {
                // Logger.logUnsuccessfulParse(fileName)
                validFlag = false
                return VehicleType.FIRE_TRUCK_WATER
                // throw IllegalArgumentException("Unknown vehicleType: $vehicleTypeStr")
            }
        }
    }

    /**
     *create an int list for vehicles, and validate the right range of id, base id, vehicle height and staffcapacity
     * id: Int, baseId: Int, staffCapacity: Int, vehicleHeight: Int
     */
    fun createVehicleIntAttributesList(id: Int, baseId: Int, sc: Int, vh: Int): List<Int> {
        if (id < 0 || baseId < 0) {
            // Logger.logUnsuccessfulParse(fileName)
            validFlag = false
            // throw IllegalArgumentException("vehicle: negative id, base id")
        }
        if (vh < Constants.VEHICLE_HEIGHT_LB || vh > Constants.VEHICLE_HEIGHT_UB) {
            // Logger.logUnsuccessfulParse(fileName)
            validFlag = false
            // throw IllegalArgumentException("wrong vehicle height ")
        }
        if (sc <= Constants.STAFF_CAPACITY_LB || sc > Constants.STAFF_CAPACITY_UB) {
            // Logger.logUnsuccessfulParse(fileName)
            validFlag = false
            // throw IllegalArgumentException("wrong staff capacity amount")
        }
        val vehicleAttributes = listOf(id, baseId, sc, vh)
        if (validFlag == false) {
            return listOf(-1, -1, -1, -1)
        }
        return vehicleAttributes
    }

    /**
     *create a int list for base, avoid too many args, no needed for base?
     fun createBaseIntAttributesList(id: Int, staff: Int, vertexId: Int): List<Int> {
     val baseAttributes = listOf(id, staff, vertexId)
     return baseAttributes
     }
     */

    /**
     *check the optional object ladder
     */
    fun checkOptLadder(ladder: String): Int {
        if (ladder != "" &&
            (ladder.toInt() < Constants.LADDER_HEIGHT_LB || ladder.toInt() > Constants.LADDER_HEIGHT_UB)
        ) {
            // Logger.logUnsuccessfulParse(fileName)
            validFlag = false
            // throw IllegalArgumentException("vehicle: wrong ladder length")
        }
        if (ladder == "") {
            // Logger.logUnsuccessfulParse(fileName)
            validFlag = false
            // throw IllegalArgumentException("vehicle: no ladder length")
        }
        if (validFlag == false) {
            return -1
        }
        return ladder.toInt()
    }

    /**
     *check the optional object criminal
     */
    fun checkOptCriminal(criminal: String): Int {
        if (criminal != "" &&
            (criminal.toInt() <= Constants.CRIMINAL_CAPACITY_LB || criminal.toInt() > Constants.CRIMINAL_CAPACITY_UB)
        ) {
            // Logger.logUnsuccessfulParse(fileName)
            validFlag = false
            // throw IllegalArgumentException("vehicle: wrong criminal number,<=0 or >4 ")
        }
        if (criminal == "") {
            // Logger.logUnsuccessfulParse(fileName)
            validFlag = false
            // throw IllegalArgumentException("vehicle: no criminal number.")
        }
        if (validFlag == false) {
            return -1
        }
        return criminal.toInt()
    }

    /**
     *check the optional object water
     */
    fun checkOptWater(water: String): Int {
        if (water == "") {
            // Logger.logUnsuccessfulParse(fileName)
            validFlag = false
            // throw IllegalArgumentException("vehicle : no water capacity $water")
        } else if (water.toInt() != Constants.WATER_VOLUME_ONE &&
            water.toInt() != Constants.WATER_VOLUME_TWO &&
            water.toInt() != Constants.WATER_VOLUME_THREE
        ) {
            // Logger.logUnsuccessfulParse(fileName)
            validFlag = false
            // throw IllegalArgumentException("vehicle : wrong water capacity $water")
        }
        if (validFlag == false) {
            return -1
        }
        return water.toInt()
    }

    /**
     *check if there is an extra wrong attribute ladder height/criminal capacity/water volume
     * in vehicles that is not fire truck ladder,police car, fire truck water
     */
    fun validateWrongLadderOrCriminalOrWater(jO: JSONObject, vT: VehicleType) {
        when (vT) {
            VehicleType.FIRE_TRUCK_LADDER -> {
                // ladder
                if (jO.optString(Constants.WATER_CAPACITY) != "" || jO.optString(Constants.CRIMINAL_CAPACITY) != "") {
                    // Logger.logUnsuccessfulParse(fileName)
                    validFlag = false
                    // throw IllegalArgumentException
                    // ("vehicle: fire_truck_ladder has water capacity or criminal capacity")
                }
            }

            VehicleType.FIRE_TRUCK_WATER -> {
                // water
                if (jO.optString(Constants.LADDER_HEIGHT) != "" || jO.optString(Constants.CRIMINAL_CAPACITY) != "") {
                    // Logger.logUnsuccessfulParse(fileName)
                    validFlag = false
                    // throw IllegalArgumentException
                    // ("vehicle: fire_truck_water has ladder or criminal capacity")
                }
            }

            VehicleType.POLICE_CAR -> {
                // police car
                if (jO.optString(Constants.LADDER_HEIGHT) != "" || jO.optString(Constants.WATER_CAPACITY) != "") {
                    // Logger.logUnsuccessfulParse(fileName)
                    validFlag = false
                    // throw IllegalArgumentException
                    // ("vehicle: police car has ladder or water capacity")
                }
            }

            else -> {
                if (jO.optString(Constants.LADDER_HEIGHT) != "" ||
                    jO.optString(Constants.WATER_CAPACITY) != "" ||
                    jO.optString(Constants.CRIMINAL_CAPACITY) != ""
                ) {
                    // Logger.logUnsuccessfulParse(fileName)
                    validFlag = false
                    // throw IllegalArgumentException
                    // ("vehicle: other general type has water,  ladder, or criminal")
                }
            }
        }
    }

    /**
     *to valadate base, self, and with map
     */
    fun checkUniqueIdBases(basesList: List<Base>): List<Base> {
        if (basesArray.isEmpty) {
            validFlag = false
        }
        val idSet = basesList.map { it.baseID }.toSet()
        val vertexIdSet = basesList.map { it.location.id }.toSet()
        // check unique base id
        if (idSet.size != basesList.size || vertexIdSet.size != basesList.size) {
            validFlag = false
            // throw IllegalArgumentException("base id not unique or same location(vertex) has multiple base")
        }
        return basesList
    }

    /**
     *to check vehicles, self and with map
     */
    fun checkUniqueIdVehicles(vehiclesList: List<Vehicle>): List<Vehicle> {
        val idSet = vehiclesList.map { it.vehicleId }.toSet()
        if (idSet.size != vehiclesList.size) {
            validFlag = false
            // throw IllegalArgumentException("vehicle id not unique")
        }
        return vehiclesList
    }

    /**
     *parser...check if ok
     */
    fun getValidFlag(): Boolean {
        return this.validFlag
    }
}
