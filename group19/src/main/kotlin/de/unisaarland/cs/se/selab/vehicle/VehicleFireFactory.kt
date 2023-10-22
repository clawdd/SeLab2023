package de.unisaarland.cs.se.selab.vehicle

import de.unisaarland.cs.se.selab.enumtype.VehicleType
/**
 *vehicle
 */
class VehicleFireFactory {
    /**
     * Create a Firetruck that holds water.
     */
    fun createFireTruckWater(vip: VehicleIntParameters, waterCapa: Int): FireVehicle {
        return FireVehicle(
            vip.vehicleId, // Vehicle ID
            vip.homeBaseId, // Base ID
            VehicleType.FIRE_TRUCK_WATER,
            vip.vehicleHeight, // Height
            vip.staffCapacity // Staff Cap
        ).apply {
            waterCapacity = waterCapa // Set the water capacity to the given param
            ladderLength = -1 // Does not have a ladder
        }
    }

    /**
     * Create a technical Firetruck. No water, no ladder
     */
    fun createFireTruckTechnical(vip: VehicleIntParameters): FireVehicle {
        return FireVehicle(
            vip.vehicleId, // Vehicle ID
            vip.homeBaseId, // Base ID
            VehicleType.FIRE_TRUCK_TECHNICAL,
            vip.vehicleHeight, // Height
            vip.staffCapacity // Staff Cap
        ).apply {
            waterCapacity = -1 // Doesn't hold water
            ladderLength = -1 // Doesn't have a ladder
        }
    }

    /**
     * Create a FireTruck with a ladder.
     */
    fun createFireTruckLadder(vip: VehicleIntParameters, ladderHe: Int): FireVehicle {
        return FireVehicle(
            vip.vehicleId, // Vehicle ID
            vip.homeBaseId, // Base ID
            VehicleType.FIRE_TRUCK_LADDER,
            vip.vehicleHeight, // Height
            vip.staffCapacity // Staff Cap
        ).apply {
            waterCapacity = -1 // No water
            ladderLength = ladderHe // Set ladder length to param
        }
    }

    /**
     * Create a Firefigter Transporter.
     */
    fun createFirefigherTransporter(vip: VehicleIntParameters): FireVehicle {
        return FireVehicle(
            vip.vehicleId, // Vehicle ID
            vip.homeBaseId, // Base ID
            VehicleType.FIREFIGHTER_TRANSPORTER,
            vip.vehicleHeight, // Height
            vip.staffCapacity // Staff Cap
        ).apply {
            waterCapacity = -1
            ladderLength = -1
        }
    }
}
