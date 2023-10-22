package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.map.Map
import org.json.JSONObject

/**
 * Provides functionality to validated objects parsed from asset json file, base
 */
class BaseValidator(val validFlag: Boolean, val vehicleBaseParser: VehicleBaseParser) {
    // todo
    private var baseValidFlag = this.validFlag
    private var atLeastOneFireStation = false
    private var atLeastOnePoliceStation = false
    private var atLeastOneHospital = false

    /**
     * get flag
     */
    fun getBaseValidFlag(): Boolean {
        return baseValidFlag
    }

    /**
     * major validator goes
     */
    fun validateBases() {
        for (i in 0 until vehicleBaseParser.basesArray.length()) {
            // check key are right number and amount
            val baseObj = vehicleBaseParser.basesArray.getJSONObject(i)
            when (baseObj.optString("baseType")) {
                "FIRE_STATION" -> {
                    validateFireStation(baseObj)
                    atLeastOneFireStation = true
                }

                "POLICE_STATION" -> {
                    validatePoliceStation(baseObj)
                    atLeastOnePoliceStation = true
                }

                "HOSPITAL" -> {
                    validateHospital(baseObj)
                    atLeastOneHospital = true
                }
            }
        } // for loop end
        checkAtleastEachBase()
    }

    /**
     * validate all the necessary fields
     */
    fun validateAllNecessaryAttributes(baseObj: JSONObject) {
        // check id exist, and is valid number.>=0
        var tempId = -1
        baseObj.optString(Constants.KEY_ID).toIntOrNull()?.let { tempId = it }
        if (tempId < 0) {
            baseValidFlag = false //
        }
        // check staff exist, and is valid number.>0
        var tempStaff = -1
        baseObj.optString(Constants.KEY_STAFF).toIntOrNull()?.let { tempStaff = it }
        if (tempStaff <= 0) {
            baseValidFlag = false //
        }
        // check location exist(both, in object and with the map), and is valid number.>=0
        var tempLocationId: String
        baseObj.optString(Constants.KEY_LOCATION).let { tempLocationId = it }
        if (tempLocationId == "" || Map.getVertexByIDParser(tempLocationId.toIntOrNull()) == null) {
            baseValidFlag = false //
        }
        // check one base one vertex, todo still need to check if the id is string
        val vertexHasBase = Map.getVertexByIDParser(tempLocationId.toIntOrNull())
        if (vertexHasBase != null && !vertexHasBase.hasBase) {
            vertexHasBase.hasBase = true
        } else {
            baseValidFlag = false
        }
    }

    /**
     * validate firestation
     */
    fun validateFireStation(fs: JSONObject) {
        // check has exactly 4 keys
        val allowedKeys = setOf(
            Constants.KEY_ID,
            Constants.KEY_BASE_TYPE,
            Constants.KEY_STAFF,
            Constants.KEY_LOCATION
        )
        for (key in fs.keys()) {
            if (key !in allowedKeys) {
                baseValidFlag = false
                break
            }
        }
        validateAllNecessaryAttributes(fs)
    }

    /**
     * validate police station
     */
    fun validatePoliceStation(ps: JSONObject) {
        // check has exactly 5 keys, dogs
        val allowedKeys = setOf(
            Constants.KEY_ID,
            Constants.KEY_BASE_TYPE,
            Constants.KEY_STAFF,
            Constants.KEY_LOCATION,
            Constants.KEY_DOGS
        )
        for (key in ps.keys()) {
            if (key !in allowedKeys) {
                baseValidFlag = false
                break
            }
        }
        if (ps.optInt(Constants.KEY_DOGS) < 0) {
            baseValidFlag = false
        }
        validateAllNecessaryAttributes(ps)
    }

    /**
     * validate hospital
     */
    fun validateHospital(hospital: JSONObject) {
        // check has exactly 5 keys, doctors
        val allowedKeys = setOf(
            Constants.KEY_ID,
            Constants.KEY_BASE_TYPE,
            Constants.KEY_STAFF,
            Constants.KEY_LOCATION,
            Constants.KEY_DOCTORS
        )
        for (key in hospital.keys()) {
            if (key !in allowedKeys) {
                baseValidFlag = false
                break
            }
        }
        if (hospital.optInt(Constants.KEY_DOCTORS) < 0) {
            baseValidFlag = false
        }
        validateAllNecessaryAttributes(hospital)
    }

    /**
     * check at least one type of base exist
     */
    fun checkAtleastEachBase() {
        // check at least each type one base
        if (!atLeastOneFireStation || !atLeastOnePoliceStation || !atLeastOneHospital) {
            baseValidFlag = false
        }
    }
}
