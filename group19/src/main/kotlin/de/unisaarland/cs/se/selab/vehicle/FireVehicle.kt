package de.unisaarland.cs.se.selab.vehicle

import de.unisaarland.cs.se.selab.base.Base
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Navigation

const val WATER_REFILL_PER_TICK = 300

/**
 * TODO
 *
 * @constructor
 * TODO
 *
 * @param vehicleId
 * @param homeBaseId
 * @param vehicleType
 * @param vehicleHeight
 * @param staffCapacity
 */
class FireVehicle(
    vehicleId: Int,
    homeBaseId: Int,
    vehicleType: VehicleType?,
    vehicleHeight: Int,
    staffCapacity: Int,
    // Should be within (600, 1200, 2400)
) : Vehicle(vehicleId, homeBaseId, vehicleType, vehicleHeight, staffCapacity) {

    var ladderLength: Int = -1 // Represents not having a ladder and
    var waterCapacity: Int = -1 // no water capacity, respectively
    var waterLevel: Int = -1 // current amount of water stored in water truck

    /**
     * starts refilling water if fire truck, otherwise does nothing
     */
    override fun recover() {
        when (vehicleType) {
            VehicleType.FIRE_TRUCK_WATER -> this.refillWater()
            else -> return
        }
    }

    /**
     * since fire vehicles don't have any specialists on board
     * we can simply add the staff back to the base
     */
    override fun returnAssets() {
        this.returnStaff()
    }

    override fun assetsUsed(): Boolean {
        return this.waterLevel < waterCapacity
    }

    /**
     *
     */
    private fun returnStaff() {
        val homeBase: Base = Navigation.getBaseById(this.homeBaseId)
        homeBase.availableStaff += staffCapacity
    }

    /**
     * uses up the vehicle's water at the end of the emergency
     * if waterLevel is emptied, sets the vehicle to recovering
     */
    fun useWater(liters: Int) {
        waterLevel -= liters

        if (waterLevel == 0) {
            this.recovering = true
        }
    }

    /**
     * as long as vehicle's water level is below its capacity,
     * fills up the truck with water
     * should the threshold be reached, the vehicle's recovery is disabled
     */
    private fun refillWater() {
        if (waterLevel < waterCapacity) {
            waterLevel += WATER_REFILL_PER_TICK
        }

        if (waterLevel >= waterCapacity) {
            waterLevel = waterCapacity
            this.recovering = false
        }
    }
}
