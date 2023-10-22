package de.unisaarland.cs.se.selab.event

import de.unisaarland.cs.se.selab.map.PrimaryRoadType
import de.unisaarland.cs.se.selab.map.Vertex

/**
 *event...
 */
class EventFactory {
    // Just so you know : we don't actually need ending tick anymore!
    // Refactor the methods sometime in Keen's parser I guess?
    /**
     *event...
     */
    fun createRushHour(
        args: List<Int>,
        roads: List<PrimaryRoadType>,
        // Keen modified:
        weightFactor: Int
    ): RushHour {
        return RushHour(
            id = args[0],
            startingTick = args[1],
            eventDuration = args[2],
            endingTick = args[3],
            affectedRoadTypes = roads,
            // weightFactor = args[4],
            weightFactor = weightFactor,
        )
    }

    /**
     *event...
     */
    fun createTrafficJam(
        args: List<Int>,
        vertices: List<Vertex>,
        // Keen modified:
        weightFactor: Int
    ): TrafficJam {
        return TrafficJam(
            id = args[0],
            startingTick = args[1],
            eventDuration = args[2],
            endingTick = args[3],
            // weightFactor = args[4],
            weightFactor = weightFactor,
            sourceVertex = vertices[0],
            targetVertex = vertices[1]
        )
    }

    /**
     *event...
     */
    fun createConstructionSite(
        args: List<Int>,
        vertices: List<Vertex>,
        oneWay: Boolean,
        // Keen modified:
        weightFactor: Int
    ): ConstructionSite {
        return ConstructionSite(
            id = args[0],
            startingTick = args[1],
            eventDuration = args[2],
            endingTick = args[3],
            // weightFactor = args[4],
            weightFactor = weightFactor,
            sourceVertex = vertices[0],
            targetVertex = vertices[1],
            oneWay
        )
    }

    /**
     *event...
     */
    fun createClosedRoad(
        args: List<Int>,
        vertices: List<Vertex>
    ): ClosedRoad {
        return ClosedRoad(
            id = args[0],
            startingTick = args[1],
            eventDuration = args[2],
            endingTick = args[3],
            sourceVertex = vertices[0],
            targetVertex = vertices[1]
        )
    }

    /**
     *event...
     */
    fun createVehicleUnavailable(
        args: List<Int>,
        // Keen modified:
        // ecc: ECC
        unVehicleId: Int
    ): VehicleUnavailable {
        return VehicleUnavailable(
            id = args[0],
            startingTick = args[1],
            eventDuration = args[2],
            endingTick = args[3],
            unavailableVehicleId = unVehicleId
        )
    }
}
