package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.base.Base
import de.unisaarland.cs.se.selab.base.FireStation
import de.unisaarland.cs.se.selab.base.Hospital
import de.unisaarland.cs.se.selab.base.PoliceStation
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.MedicalVehicle
import org.json.JSONObject

/**
 * for vehicle
 */
class VehicleValidator(val validFlag: Boolean, val vehicleBaseParser: VehicleBaseParser) {
    // todo
    private var vehicleValidFlag = this.validFlag
    private val policeCheckMap: MutableMap<Int, PoliceCheck> = mutableMapOf()
    private val fireCheckMap: MutableMap<Int, FireCheck> = mutableMapOf()
    private val hospitalCheckMap: MutableMap<Int, HospitalCheck> = mutableMapOf()
    private var atLeastOneFireVehicle = false
    private var atLeastOnePoliceVehicle = false
    private var atLeastOneHospitalVehicle = false
    val nonExistBase: Base = Hospital(
        -1,
        Vertex(-1),
        -1,
        emptyList<Emergency>().toMutableList(),
        emptyList<MedicalVehicle>().toMutableList(),
        -1,
        BaseType.HOSPITAL
    )

    /**
     * for police station check enough capacity, dogs
     * val baseID: Int,
     *     val location: Vertex,
     *     var availableStaff: Int,
     *     var assignedEmergencies: MutableList<Emergency>,
     *     val baseType: BaseType
     */
    data class PoliceCheck(
        var baseID: Int = -1,
        var originalStaffCapacity: Int = 0,
        var originalDog: Int = 0,
        var calculateStaff: Int = 0,
        var calculateDogs: Int = 0
    )

    /**
     * for fire station check enough capacity
     */
    data class FireCheck(
        var baseID: Int = -1,
        var originalStaffCapacity: Int = 0,
        var calculateStaff: Int = 0,
    )

    /**
     * for hospital check enough capacity, doctors
     */
    data class HospitalCheck(
        var baseID: Int = -1,
        var originalStaffCapacity: Int = 0,
        var originalDoctors: Int = 0,
        var calculateStaff: Int = 0,
        var calculateDoctors: Int = 0
    )

    /**
     * major  vehicle validator goes
     */
    fun validateVehicles() {
        for (i in 0 until vehicleBaseParser.vehiclesArray.length()) {
            // check key are right number and amount
            val vehicleObj = vehicleBaseParser.vehiclesArray.getJSONObject(i)
            when (vehicleObj.optString("vehicleType")) {
                "AMBULANCE" -> {
                    validateAmbulance(vehicleObj)
                    atLeastOneHospitalVehicle = true
                }

                "EMERGENCY_DOCTOR_CAR" -> {
                    validateEmergencyDoctorCar(vehicleObj)
                    atLeastOneHospitalVehicle = true
                }

                "FIRE_TRUCK_WATER" -> {
                    validateFireTruckWater(vehicleObj)
                    atLeastOneFireVehicle = true
                }

                "FIRE_TRUCK_TECHNICAL" -> {
                    validateFireTruckTechnical(vehicleObj)
                    atLeastOneFireVehicle = true
                }

                "FIRE_TRUCK_LADDER" -> {
                    validateFireTruckLadder(vehicleObj)
                    atLeastOneFireVehicle = true
                }

                "FIREFIGHTER_TRANSPORTER" -> {
                    validateFirefighterTransporter(vehicleObj)
                    atLeastOneFireVehicle = true
                }

                "POLICE_CAR" -> {
                    validatePoliceCar(vehicleObj)
                    atLeastOnePoliceVehicle = true
                }

                "K9_POLICE_CAR" -> {
                    validateKninePoliceCar(vehicleObj)
                    atLeastOnePoliceVehicle = true
                }

                "POLICE_MOTORCYCLE" -> {
                    validatePoliceMotorcycle(vehicleObj)
                    atLeastOnePoliceVehicle = true
                }
            }
        } // for loop end
        if (atLeastOneFireVehicle == false || atLeastOneHospitalVehicle == false || atLeastOnePoliceVehicle == false) {
            vehicleValidFlag = false
        }
        checkCapacityEnough()
        checkAtleastVehicle()
    }

    /**
     * validate all the necessary fields
     */
    fun validateAllNecessaryAttributes(vehicleObj: JSONObject) {
        // check id exist, and is valid number.>=0
        var tempId = -1
        vehicleObj.optString(Constants.KEY_ID).toIntOrNull()?.let { tempId = it }
        if (tempId < 0) {
            vehicleValidFlag = false //
        }
        // check staffcapacity exist, and is valid number.0<x<=12
        var tempStaffCapa = -1
        vehicleObj.optString(Constants.STAFF_CAPACITY).toIntOrNull()?.let { tempStaffCapa = it }
        if (tempStaffCapa <= 0 || tempStaffCapa > Constants.STAFF_CAPACITY_UB) {
            vehicleValidFlag = false //
        }
        // check vehicle height exist, and is valid number.1<=x<=5
        var tempVehicleHeight = -1
        vehicleObj.optString(Constants.VEHICLE_HEIGHT).toIntOrNull()?.let { tempVehicleHeight = it }
        if (tempVehicleHeight < Constants.VEHICLE_HEIGHT_LB || tempVehicleHeight > Constants.VEHICLE_HEIGHT_UB) {
            vehicleValidFlag = false //
        }
        // check baseID is valid number.>=0, exist corresponding base in all base list
        var tempBaseId = -1
        vehicleObj.optString(Constants.KEY_BASE_ID).toIntOrNull()?.let { tempBaseId = it }
        var matchingBase: Base = nonExistBase
        // Find the base with matching baseId, if it exists
        vehicleBaseParser.parsedBases.find { it.baseID == tempBaseId }?.let { matchingBase = it }
        if (matchingBase.baseID == -1) {
            vehicleValidFlag = false
            return
        }
        // if (tempBaseId < 0) {
        //    vehicleValidFlag = false //
        // }
    }

    /**
     * validate ambulance
     */
    fun validateAmbulance(am: JSONObject) {
        // check match ; check has exactly 5 keys
        helperCheckKeyForAllOther(am)
        var tempStaffCapa = -1
        am.optString(Constants.STAFF_CAPACITY).toIntOrNull()?.let { tempStaffCapa = it }
        var tempBaseId = -1
        am.optString(Constants.KEY_BASE_ID).toIntOrNull()?.let { tempBaseId = it }
        var matchingBase: Base = nonExistBase
        // Find the base with matching baseId, if it exists
        vehicleBaseParser.parsedBases.find { it.baseID == tempBaseId }?.let { matchingBase = it }
        if (matchingBase.baseType != BaseType.HOSPITAL || matchingBase.baseID == -1) {
            vehicleValidFlag = false
            return
        }
        validateAllNecessaryAttributes(am)
        val mHospital: Hospital = matchingBase as Hospital
        if (!hospitalCheckMap.containsKey(tempBaseId)) {
            hospitalCheckMap[tempBaseId] = HospitalCheck(
                tempBaseId,
                mHospital.availableStaff,
                mHospital.availableDoctors,
                0,
                0
            )
            val temp = hospitalCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else if (hospitalCheckMap.containsKey(tempBaseId)) {
            val temp = hospitalCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else {
            vehicleValidFlag = false
        }
    }

    /**
     * validate doctorcar
     */
    fun validateEmergencyDoctorCar(edc: JSONObject) {
        // check match ; check has exactly 5 keys
        helperCheckKeyForAllOther(edc)
        var tempBaseId = -1
        edc.optString(Constants.KEY_BASE_ID).toIntOrNull()?.let { tempBaseId = it }
        var tempStaffCapa = -1
        edc.optString(Constants.STAFF_CAPACITY).toIntOrNull()?.let { tempStaffCapa = it }
        var matchingBase: Base = nonExistBase
        // Find the base with matching baseId, if it exists
        vehicleBaseParser.parsedBases.find { it.baseID == tempBaseId }?.let { matchingBase = it }
        if (matchingBase.baseType != BaseType.HOSPITAL || matchingBase.baseID == -1) {
            vehicleValidFlag = false
            return
        }
        validateAllNecessaryAttributes(edc)
        val mHospital: Hospital = matchingBase as Hospital
        if (!hospitalCheckMap.containsKey(tempBaseId)) {
            hospitalCheckMap.put(
                tempBaseId,
                HospitalCheck(
                    tempBaseId,
                    mHospital.availableStaff,
                    mHospital.availableDoctors,
                    0,
                    0
                )
            )
            val temp = hospitalCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
                temp.calculateDoctors = 1
            }
        } else if (hospitalCheckMap.containsKey(tempBaseId)) {
            val temp = hospitalCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
                temp.calculateDoctors = 1
            }
        } else {
            vehicleValidFlag = false
        }
    }

    /**
     * validate water
     */
    fun validateFireTruckWater(ftw: JSONObject) {
        // check match ; check has exactly 5 keys
        helperForCheckFireTruckWater(ftw)
        var tempStaffCapa = -1
        ftw.optString(Constants.STAFF_CAPACITY).toIntOrNull()?.let { tempStaffCapa = it }
        var tempBaseId = -1
        ftw.optString(Constants.KEY_BASE_ID).toIntOrNull()?.let { tempBaseId = it }
        var matchingBase: Base = nonExistBase
        // var matching doctors = -1
        // Find the base with matching baseId, if it exists
        vehicleBaseParser.parsedBases.find { it.baseID == tempBaseId }?.let { matchingBase = it }
        if (matchingBase.baseType != BaseType.FIRE_STATION || matchingBase.baseID == -1) {
            vehicleValidFlag = false
            return
        }
        validateAllNecessaryAttributes(ftw)
        val mFireStation: FireStation = matchingBase as FireStation
        if (!fireCheckMap.containsKey(tempBaseId)) {
            fireCheckMap.put(
                tempBaseId,
                FireCheck(
                    tempBaseId,
                    mFireStation.availableStaff,
                    0
                )
            )
            val temp = fireCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else if (fireCheckMap.containsKey(tempBaseId)) {
            val temp = fireCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else {
            vehicleValidFlag = false
        }
    }

    private fun helperForCheckFireTruckWater(ftw: JSONObject) {
        val allowedKeys = setOf(
            Constants.KEY_ID,
            Constants.KEY_BASE_ID,
            Constants.KEY_VEHICLE_TYPE,
            Constants.STAFF_CAPACITY,
            Constants.VEHICLE_HEIGHT,
            Constants.WATER_CAPACITY
        )
        for (key in ftw.keys()) {
            if (key !in allowedKeys) {
                vehicleValidFlag = false
                break
            }
        }
    }

    /**
     * validate water
     */
    fun validateFireTruckTechnical(ftt: JSONObject) {
        // check match ; check has exactly 5 keys
        helperCheckKeyForAllOther(ftt)
        var tempStaffCapa = -1
        ftt.optString(Constants.STAFF_CAPACITY).toIntOrNull()?.let { tempStaffCapa = it }
        var tempBaseId = -1
        ftt.optString(Constants.KEY_BASE_ID).toIntOrNull()?.let { tempBaseId = it }
        var matchingBase: Base = nonExistBase
        // var matching doctors = -1
        // Find the base with matching baseId, if it exists
        vehicleBaseParser.parsedBases.find { it.baseID == tempBaseId }?.let { matchingBase = it }
        if (matchingBase.baseType != BaseType.FIRE_STATION || matchingBase.baseID == -1) {
            vehicleValidFlag = false
            return
        }
        validateAllNecessaryAttributes(ftt)
        val mFireStation: FireStation = matchingBase as FireStation
        if (!fireCheckMap.containsKey(tempBaseId)) {
            fireCheckMap.put(
                tempBaseId,
                FireCheck(
                    tempBaseId,
                    mFireStation.availableStaff,
                    0
                )
            )
            val temp = fireCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else if (fireCheckMap.containsKey(tempBaseId)) {
            val temp = fireCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else {
            vehicleValidFlag = false
        }
    }

    /**
     * validate ladder
     */
    fun validateFireTruckLadder(ftl: JSONObject) {
        // check match ; check has exactly 5 keys
        helperForCheckFireTruckLadder(ftl)
        var tempStaffCapa = -1
        ftl.optString(Constants.STAFF_CAPACITY).toIntOrNull()?.let { tempStaffCapa = it }
        var tempBaseId = -1
        ftl.optString(Constants.KEY_BASE_ID).toIntOrNull()?.let { tempBaseId = it }
        var matchingBase: Base = nonExistBase
        // var matching doctors = -1
        // Find the base with matching baseId, if it exists
        vehicleBaseParser.parsedBases.find { it.baseID == tempBaseId }?.let { matchingBase = it }
        if (matchingBase.baseType != BaseType.FIRE_STATION || matchingBase.baseID == -1) {
            vehicleValidFlag = false
            return
        }
        validateAllNecessaryAttributes(ftl)
        val mFireStation: FireStation = matchingBase as FireStation
        if (!fireCheckMap.containsKey(tempBaseId)) {
            fireCheckMap.put(
                tempBaseId,
                FireCheck(
                    tempBaseId,
                    mFireStation.availableStaff,
                    0
                )
            )
            val temp = fireCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else if (fireCheckMap.containsKey(tempBaseId)) {
            val temp = fireCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else {
            vehicleValidFlag = false
        }
    }

    private fun helperForCheckFireTruckLadder(ftl: JSONObject) {
        val allowedKeys = setOf(
            Constants.KEY_ID,
            Constants.KEY_BASE_ID,
            Constants.KEY_VEHICLE_TYPE,
            Constants.STAFF_CAPACITY,
            Constants.VEHICLE_HEIGHT,
            Constants.LADDER_HEIGHT
        )
        for (key in ftl.keys()) {
            if (key !in allowedKeys) {
                vehicleValidFlag = false
                break
            }
        }
    }

    /**
     * validate
     */
    fun validateFirefighterTransporter(fft: JSONObject) {
        // check match ; check has exactly 5 keys
        helperCheckKeyForAllOther(fft)
        var tempStaffCapa = -1
        fft.optString(Constants.STAFF_CAPACITY).toIntOrNull()?.let { tempStaffCapa = it }
        var tempBaseId = -1
        fft.optString(Constants.KEY_BASE_ID).toIntOrNull()?.let { tempBaseId = it }
        var matchingBase: Base = nonExistBase
        // var matching doctors = -1
        // Find the base with matching baseId, if it exists
        vehicleBaseParser.parsedBases.find { it.baseID == tempBaseId }?.let { matchingBase = it }
        if (matchingBase.baseType != BaseType.FIRE_STATION || matchingBase.baseID == -1) {
            vehicleValidFlag = false
            return
        }
        validateAllNecessaryAttributes(fft)
        val mFireStation: FireStation = matchingBase as FireStation
        if (!fireCheckMap.containsKey(tempBaseId)) {
            fireCheckMap.put(
                tempBaseId,
                FireCheck(
                    tempBaseId,
                    mFireStation.availableStaff,
                    0
                )
            )
            val temp = fireCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else if (fireCheckMap.containsKey(tempBaseId)) {
            val temp = fireCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else {
            vehicleValidFlag = false
        }
    }

    /**
     * validate pcar
     */
    fun validatePoliceCar(pc: JSONObject) {
        // check match ; check has exactly 5 keys+ craminal capacity
        helperForCheckPoliceCar(pc)
        var tempStaffCapa = -1
        pc.optString(Constants.STAFF_CAPACITY).toIntOrNull()?.let { tempStaffCapa = it }
        var tempBaseId = -1
        pc.optString(Constants.KEY_BASE_ID).toIntOrNull()?.let { tempBaseId = it }
        var matchingBase: Base = nonExistBase // Find the base with matching baseId, if it exists
        vehicleBaseParser.parsedBases.find { it.baseID == tempBaseId }?.let { matchingBase = it }
        if (matchingBase.baseType != BaseType.POLICE_STATION || matchingBase.baseID == -1) {
            vehicleValidFlag = false
            return
        }
        validateAllNecessaryAttributes(pc)
        val mPoliceStation: PoliceStation = matchingBase as PoliceStation
        if (!policeCheckMap.containsKey(tempBaseId)) {
            policeCheckMap.put(
                tempBaseId,
                PoliceCheck(
                    tempBaseId,
                    mPoliceStation.availableStaff,
                    mPoliceStation.availableK9s,
                    0,
                    0
                )
            )
            val temp = policeCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else if (policeCheckMap.containsKey(tempBaseId)) {
            val temp = policeCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else {
            vehicleValidFlag = false
        }
    }

    private fun helperForCheckPoliceCar(pc: JSONObject) {
        val allowedKeys = setOf(
            Constants.KEY_ID,
            Constants.KEY_BASE_ID,
            Constants.KEY_VEHICLE_TYPE,
            Constants.STAFF_CAPACITY,
            Constants.VEHICLE_HEIGHT,
            Constants.CRIMINAL_CAPACITY
        )
        for (key in pc.keys()) {
            if (key !in allowedKeys) {
                vehicleValidFlag = false
                break
            }
        }
    }

    /**
     * validate dogcar
     */
    fun validateKninePoliceCar(kpc: JSONObject) {
        // check match ; check has exactly 5 keys
        helperCheckKeyForAllOther(kpc)
        var tempStaffCapa = -1
        kpc.optString(Constants.STAFF_CAPACITY).toIntOrNull()?.let { tempStaffCapa = it }
        var tempBaseId = -1
        kpc.optString(Constants.KEY_BASE_ID).toIntOrNull()?.let { tempBaseId = it }
        var matchingBase: Base = nonExistBase
        // var matching doctors = -1
        // Find the base with matching baseId, if it exists
        vehicleBaseParser.parsedBases.find { it.baseID == tempBaseId }?.let { matchingBase = it }
        if (matchingBase.baseType != BaseType.POLICE_STATION || matchingBase.baseID == -1) {
            vehicleValidFlag = false
            return
        }
        validateAllNecessaryAttributes(kpc)
        val mPoliceStation: PoliceStation = matchingBase as PoliceStation
        if (!policeCheckMap.containsKey(tempBaseId)) {
            policeCheckMap.put(
                tempBaseId,
                PoliceCheck(
                    tempBaseId,
                    mPoliceStation.availableStaff,
                    mPoliceStation.availableK9s,
                    0,
                    0
                )
            )
            val temp = policeCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
                temp.calculateDogs = 1
            }
        } else if (policeCheckMap.containsKey(tempBaseId)) {
            val temp = policeCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
                temp.calculateDogs = 1
            }
        } else {
            vehicleValidFlag = false
        }
    }

    private fun helperCheckKeyForAllOther(am: JSONObject) {
        val allowedKeys = setOf(
            Constants.KEY_ID,
            Constants.KEY_BASE_ID,
            Constants.KEY_VEHICLE_TYPE,
            Constants.STAFF_CAPACITY,
            Constants.VEHICLE_HEIGHT
        )
        for (key in am.keys()) {
            if (key !in allowedKeys) {
                vehicleValidFlag = false
                break
            }
        }
    }

    /**
     * validate water
     */
    fun validatePoliceMotorcycle(pm: JSONObject) {
        // check match ; check has exactly 5 keys
        helperCheckKeyForAllOther(pm)
        var tempStaffCapa = -1
        pm.optString(Constants.STAFF_CAPACITY).toIntOrNull()?.let { tempStaffCapa = it }
        var tempBaseId = -1
        pm.optString(Constants.KEY_BASE_ID).toIntOrNull()?.let { tempBaseId = it }
        var matchingBase: Base = nonExistBase
        // var matching doctors = -1
        // Find the base with matching baseId, if it exists
        vehicleBaseParser.parsedBases.find { it.baseID == tempBaseId }?.let { matchingBase = it }
        if (matchingBase.baseType != BaseType.POLICE_STATION || matchingBase.baseID == -1) {
            vehicleValidFlag = false
            return
        }
        validateAllNecessaryAttributes(pm)
        val mPoliceStation: PoliceStation = matchingBase as PoliceStation
        if (!policeCheckMap.containsKey(tempBaseId)) {
            policeCheckMap.put(
                tempBaseId,
                PoliceCheck(
                    tempBaseId,
                    mPoliceStation.availableStaff,
                    mPoliceStation.availableK9s,
                    0,
                    0
                )
            )
            val temp = policeCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else if (policeCheckMap.containsKey(tempBaseId)) {
            val temp = policeCheckMap.get(tempBaseId)
            if (temp != null && temp.calculateStaff <= tempStaffCapa) {
                temp.calculateStaff = tempStaffCapa
            }
        } else {
            vehicleValidFlag = false
        }
    }

    /**
     * check enough capacity, check at least one of the base
     */
    fun checkCapacityEnough() {
        val fireIterator = fireCheckMap.iterator()
        while (fireIterator.hasNext()) {
            val entry = fireIterator.next()
            // val key = entry.key
            val fireEnough = entry.value
            if (fireEnough.originalStaffCapacity < fireEnough.calculateStaff) {
                vehicleValidFlag = false
            }
        }

        val policeIterator = policeCheckMap.iterator()
        while (policeIterator.hasNext()) {
            val entry = policeIterator.next()
            // val key = entry.key
            val policeEnough = entry.value
            if (policeEnough.originalStaffCapacity < policeEnough.calculateStaff) {
                vehicleValidFlag = false
            }
            if (policeEnough.originalDog < policeEnough.calculateDogs) {
                vehicleValidFlag = false
            }
        }

        val hospitalIterator = hospitalCheckMap.iterator()
        while (hospitalIterator.hasNext()) {
            val entry = hospitalIterator.next()
            // val key = entry.key
            val hospitalEnough = entry.value
            if (hospitalEnough.originalStaffCapacity < hospitalEnough.calculateStaff) {
                vehicleValidFlag = false
            }

            if (hospitalEnough.originalDoctors < hospitalEnough.calculateDoctors) {
                vehicleValidFlag = false
            }
        }
    }

    /**
     * get vehiclevalidflag
     */
    fun getVehicleValidFlag(): Boolean {
        return vehicleValidFlag
    }

    /**
     * get at least
     */
    fun checkAtleastVehicle() {
        // check at least each type one base
        if (fireCheckMap.size < 1 || policeCheckMap.size < 1 || hospitalCheckMap.size < 1) {
            vehicleValidFlag = false
        }
    }
}
