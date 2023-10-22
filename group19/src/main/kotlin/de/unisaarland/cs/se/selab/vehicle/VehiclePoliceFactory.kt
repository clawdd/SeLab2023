package de.unisaarland.cs.se.selab.vehicle

import de.unisaarland.cs.se.selab.enumtype.VehicleType
/**
 *vehicle
 */
class VehiclePoliceFactory {
    /**
     *Create a Police Car with a specified criminal capacity.
     */
    fun createPoliceCar(vip: VehicleIntParameters, criminalCapa: Int): PoliceVehicle {
        return PoliceVehicle(
            vip.vehicleId, // Vehicle ID
            vip.homeBaseId, // Base ID
            VehicleType.POLICE_CAR,
            vip.vehicleHeight, // Height
            vip.staffCapacity, // Staff Cap
        ).apply {
            dog = false
            criminalCapacity = criminalCapa // Holds x criminals
        }
    }

    /**
     * Create a K9 Police car that holds a dog.
     */
    fun createK9PoliceCar(vip: VehicleIntParameters): PoliceVehicle {
        return PoliceVehicle(
            vip.vehicleId, // Vehicle ID
            vip.homeBaseId, // Base ID
            VehicleType.K9_POLICE_CAR,
            vip.vehicleHeight, // Height
            vip.staffCapacity, // Staff Cap
        ).apply {
            dog = true
            criminalCapacity = -1
        }
    }

    /**
     *vehicle
     */
    fun createPoliceMotorcycle(vip: VehicleIntParameters): PoliceVehicle {
        return PoliceVehicle(
            vip.vehicleId,
            vip.homeBaseId,
            VehicleType.POLICE_MOTORCYCLE,
            vip.vehicleHeight,
            vip.staffCapacity,
        ).apply {
            dog = false
            criminalCapacity = -1
        }
    }
}
