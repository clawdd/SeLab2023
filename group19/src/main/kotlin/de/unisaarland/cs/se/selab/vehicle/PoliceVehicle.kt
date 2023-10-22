package de.unisaarland.cs.se.selab.vehicle

import de.unisaarland.cs.se.selab.base.Base
import de.unisaarland.cs.se.selab.base.PoliceStation
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
class PoliceVehicle(

    vehicleId: Int,
    homeBaseId: Int,
    vehicleType: VehicleType,
    vehicleHeight: Int,
    staffCapacity: Int,
) : Vehicle(vehicleId, homeBaseId, vehicleType, vehicleHeight, staffCapacity) {
    var dog: Boolean = false
    var criminalCapacity: Int = -1
    var criminalsOnBoard: Int = 0
    var unloadTimer: Int = -1 // indicates when handing in criminals is finished

    /**
     * starts unloading criminals out of a police car
     * does not need to do anything for other types of police vehicles
     */
    override fun recover() {
        when (vehicleType) {
            VehicleType.POLICE_CAR -> this.unloadCriminals()
            else -> return
        }
    }

    override fun returnAssets() {
        when (vehicleType) {
            VehicleType.K9_POLICE_CAR -> this.returnDog()
            else -> returnStaff()
        }
    }

    override fun assetsUsed(): Boolean {
        return this.criminalsOnBoard > 0
    }

    /**
     * returns a K9 to a police station
     */
    private fun returnDog() {
        val homeBase: Base = Navigation.getBaseById(this.homeBaseId)
        homeBase as PoliceStation

        // remove dog from vehicle, add dog to base
        this.dog = false
        homeBase.availableK9s += 1

        // add regular staff to base
        this.returnStaff()
    }

    private fun returnStaff() {
        val homeBase: Base = Navigation.getBaseById(this.homeBaseId)
        homeBase.availableStaff += staffCapacity
    }

    /**
     * loads the specified amount of criminals into the vehicles
     */
    fun loadCriminals(criminals: Int) {
        criminalsOnBoard += criminals

        if (criminalsOnBoard == criminalCapacity) {
            this.recovering = true
        }
    }

    /**
     * effectively makes the police car unresponsible to anything for two ticks
     */
    private fun unloadCriminals() {
        // if the vehicle's unload timer is -1 and it's capacity is full starts
        // setting the vehicle unavailable for two ticks
        if (unloadTimer == -1) {
            unloadTimer = 1
            return
        }

        // if unloadTimer is 2 or 1, the vehicle's unloading has either been started or is ongoing
        if (unloadTimer == 1) {
            criminalsOnBoard = 0
            unloadTimer = -1
            this.recovering = false
        }
    }
}
