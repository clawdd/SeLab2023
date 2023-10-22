package de.unisaarland.cs.se.selab.vehicle

import de.unisaarland.cs.se.selab.base.Base
import de.unisaarland.cs.se.selab.base.Hospital
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Navigation

/**
 * TODO
 *
 * @constructor
 * TODO
 *
 * @param vehicleId
 * @param homeBaseId
 * @param vehicleType
 * @param staffCapacity
 * @param vehicleHeight
 */
class MedicalVehicle(
    vehicleId: Int,
    homeBaseId: Int,
    vehicleType: VehicleType?,
    vehicleHeight: Int,
    staffCapacity: Int,
) : Vehicle(vehicleId, homeBaseId, vehicleType, vehicleHeight, staffCapacity) {
    var doctor: Boolean = false
    var patient: Boolean = false

    /**
     * orders ambulances to hand in their patients
     */
    override fun recover() {
        when (vehicleType) {
            VehicleType.AMBULANCE -> this.handInPatient()
            else -> return
        }
    }

    /**
     * returns specialist for doctor cars first
     * otherwise directly returns the staff to base
     */
    override fun returnAssets() {
        when (vehicleType) {
            VehicleType.EMERGENCY_DOCTOR_CAR -> this.returnDoctor()
            else -> this.returnStaff()
        }
    }

    override fun assetsUsed(): Boolean {
        return this.patient
    }

    /**
     * returns an emergency doctor to a hospital
     */
    private fun returnDoctor() {
        val homeBase: Base = Navigation.getBaseById(this.homeBaseId)
        homeBase as Hospital

        // remove doctor from car, add him to base
        this.doctor = false
        homeBase.availableDoctors += 1

        // returns regular staff members
        this.returnStaff()
    }

    /**
     * returns staff members to base and adds the vehicle as well
     */
    private fun returnStaff() {
        val homeBase: Base = Navigation.getBaseById(this.homeBaseId)
        homeBase.availableStaff += staffCapacity
    }

    /**
     * loads a patient into the ambulance, which is equivalent to setting the patient flag true
     */
    fun loadPatient() {
        this.patient = true
        this.recovering = true
    }

    /**
     * hands in patient to the hospital which causes the vehicle to wait for a turn
     */
    private fun handInPatient() {
        if (patient == true) {
            patient = false
            this.unavailability = false
            return
        }
    }
}
