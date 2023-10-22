package de.unisaarland.cs.se.selab.vehicle

import de.unisaarland.cs.se.selab.enumtype.VehicleType
/**
 *vehicle
 */
class VehicleMedicalFactory {
    /**
     * Create an ambulance.
     */
    fun createAmbulance(vip: VehicleIntParameters): MedicalVehicle {
        return MedicalVehicle(
            vip.vehicleId, // Vehicle ID
            vip.homeBaseId, // Home Base ID
            VehicleType.AMBULANCE, // Type of vehicle
            vip.vehicleHeight, // Vehicle Height
            vip.staffCapacity // Staff cap
        ).apply {
            patient = false // No patient inside vehicle atm
            doctor = false // An ambulance doesn't hold a doctor so
        }
    }

    /**
     * Create a Doctor Car which holds one doctor.
     */
    fun createDoctorCar(vip: VehicleIntParameters): MedicalVehicle {
        return MedicalVehicle(
            vip.vehicleId, // Vehicle ID
            vip.homeBaseId, // Home Base ID
            VehicleType.EMERGENCY_DOCTOR_CAR, // Type of vehicle
            vip.vehicleHeight, // Vehicle Height
            vip.staffCapacity // Staff cap
        ).apply {
            patient = false // No patient inside vehicle atm
            doctor = true // Doctor is present
        }
    }
}
