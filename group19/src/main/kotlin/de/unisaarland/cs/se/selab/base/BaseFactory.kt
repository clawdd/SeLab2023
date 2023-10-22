package de.unisaarland.cs.se.selab.base

import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.BaseType
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.FireVehicle
import de.unisaarland.cs.se.selab.vehicle.MedicalVehicle
import de.unisaarland.cs.se.selab.vehicle.PoliceVehicle

/**
 * Base Factory
 */
class BaseFactory {
    /**
     * creates a police station
     */
    fun createPoliceStation(baseID: Int, location: Vertex, staff: Int, dogs: Int): PoliceStation {
        return PoliceStation(
            baseID,
            location,
            staff,
            mutableListOf<Emergency>(),
            mutableListOf<PoliceVehicle>(),
            dogs,
            BaseType.POLICE_STATION
        )
    }

    /**
     * creates a fire station
     */
    fun createFireStation(baseID: Int, location: Vertex, staff: Int): FireStation {
        return FireStation(
            baseID,
            location,
            staff,
            mutableListOf<Emergency>(),
            mutableListOf<FireVehicle>(),
            BaseType.FIRE_STATION
        )
    }

    /**
     * creates a hospital
     */
    fun createHospital(baseID: Int, location: Vertex, staff: Int, doctors: Int): Hospital {
        return Hospital(
            baseID,
            location,
            staff,
            mutableListOf<Emergency>(),
            mutableListOf<MedicalVehicle>(),
            doctors,
            BaseType.HOSPITAL
        )
    }
}
