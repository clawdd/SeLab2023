package de.unisaarland.cs.se.selab.event

import de.unisaarland.cs.se.selab.ECC
import de.unisaarland.cs.se.selab.Logger
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.PrimaryRoadType
import de.unisaarland.cs.se.selab.map.Road
import de.unisaarland.cs.se.selab.map.SecondaryRoadType
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.Vehicle
const val PLACEHOLDER_OG_ROADWEIGHT = 1.1230

/**
 *event...
 */
sealed class Event {
    public abstract val id: Int
    public abstract var startingTick: Int
    public abstract val eventDuration: Int
    public abstract val endingTick: Int

    /**
     *event...
     */
    abstract fun triggerEvent(): Boolean
    // Triggers an event.
    // True if the event succesfully changed the road.
    // False if the event gets delayed/procrastinated.

    /**
     * Reverses an event, and returns a bool stating if anything took place.
     */
    abstract fun reverseEvent(): Boolean
    // IF and only if the event actually belongs to the road, trigger it

    /**
     * check if the event has ended on this tick or before this
     */
    open fun checkIfEndedPreviously(currentTick: Int): Boolean {
        return currentTick >= startingTick + eventDuration
    }
}

// I call Map a lot here. Optimization can be done in the future by reducing how many times I call it
/**
 * Represents a rushhour that takes in a list of affected road types and a weight factor.
 */
data class RushHour(
    override val id: Int,
    override var startingTick: Int,
    override val eventDuration: Int,
    override val endingTick: Int,
    val affectedRoadTypes: List<PrimaryRoadType>,
    val weightFactor: Int
) : Event() {
    var firstStartTick: Int = startingTick // The tick from the first time the event got triggered.
    var triggered: Boolean = false // Keeps track of if the event has triggered on atleast one road.
    val triggeredRoads: MutableSet<Road> = mutableSetOf()
    var logged = false
    override fun triggerEvent(): Boolean {
        // distribute events to all roads of this type in map
        val map = Map
        val wastriggeredalready = triggered
        for (road in map.roads) {
            if (road.primaryType in affectedRoadTypes) {
                triggerRoadLogic(road) // Trigger on every road of type
            }
        }
        if (!wastriggeredalready) { // If it wasn't triggered before this...
            if (triggered) { // But did just get triggered meaning the first time!
                firstStartTick = startingTick // Our event starts now.
                if (!logged) {
                    Logger.logEventTriggered(id)
                    logged = false
                }
            } else {
                // Still hasn't started...
                firstStartTick++
            }
        }
        if (startingTick + 1 < firstStartTick + eventDuration) {
            startingTick++ // I want to keep triggering every tick until the ending tick.
        }
        return triggered // If a road triggered this tick, return true
        // I.e. a rerouting is necessary.
    }

    // To avoid detekt, I've put the internal logic of trigger inside of this func.
    /**
     * Goes through every road and triggers the event
     * Side effect of changing triggered if atleast one event triggers.
     */
    private fun triggerRoadLogic(road: Road) {
        if (road.scheduledGlobalEvent == null || road.scheduledGlobalEvent == this) {
            // If the road currently doesn't have either type of event going on...
            // or if it hasn't been triggered already
            if (road.event == null && !triggeredRoads.contains(road)) {
                // trigger the actual event
                road.weight *= weightFactor
                road.scheduledGlobalEvent = this
                triggeredRoads.add(road)
                triggered = true
            }
            // Either way, set the scheduled global event to this. "queue" it, basically
            if (road.event !is ClosedRoad) {
                road.scheduledGlobalEvent = this
            } // If there's a closed road queued, other rush hours might be procrastinating and
            // waiting for it to end as well
        }
        // Else : There's already a global event, let's not intrude
    }

    override fun reverseEvent(): Boolean {
        var triggerforECC = false // Checks if anything changed. If yes, true
        val map = Map
        for (road in map.roads) {
            if (road.primaryType in affectedRoadTypes) {
                // Only reverse changes for roads that were triggered by this RushHour event
                if (road.scheduledGlobalEvent == this) {
                    triggerforECC = reverseEventLogic(road)
                }
            }
        }
        triggeredRoads.clear() // Clear the set of triggered roads, not really needed but yknow
        // Reverse being called means there was definitely one road that got triggered!
        Logger.logEventEnded(id)
        return triggerforECC
    }

    private fun reverseEventLogic(r: Road): Boolean {
        var trigger = false
        if (r in triggeredRoads) { // We triggered it
            r.weight /= weightFactor
            trigger = true
        }
        // Else : the event got scheduled but never got to actually trigger
        // So either way :
        r.scheduledGlobalEvent = null
        return trigger
    }

    /**
     * Overload for rushhour because it starts ticking when it triggers, even on only one road.
     */
    override fun checkIfEndedPreviously(currentTick: Int): Boolean {
        return currentTick >= firstStartTick + eventDuration
    }
}

/**
 * Represents a traffic jam with a weight factor.
 */
data class TrafficJam(
    override val id: Int,
    override var startingTick: Int,
    override val eventDuration: Int,
    override val endingTick: Int,
    val weightFactor: Int,
    val sourceVertex: Vertex,
    val targetVertex: Vertex
) : Event() {
    var logged = false
    override fun triggerEvent(): Boolean {
        // distribute events to single road
        val road = getRoad()
        // I added the 2nd part of this check on the 30th, for closedRoad
        // if there is a closed road, its okay to procrastinate the event right???
        return if (road.scheduledGlobalEvent != null || road.event != null) {
            // Procrastinate
            startingTick++
            false
        } else {
            // Trigger the event
            road.weight *= weightFactor
            road.event = this
            if (!logged) {
                Logger.logEventTriggered(id)
                logged = true
            }

            true
        }
    }

    override fun reverseEvent(): Boolean {
        var triggerforECC = false
        val road = getRoad()
        if (road.event != null) {
            if (road.event?.id == this.id) { // If the event is the same one
                // Non-null asserted as we just checked and made sure it's not null lol
                road.weight /= weightFactor
                road.event = null
                triggerforECC = true
                Logger.logEventEnded(id)
            }
        }
        return triggerforECC
        // So the else case here is when the event kept getting procrastinated and never triggered.
        // god I hope this logic is correct. I fear the day I have to debug my own flawed thinking
    }

    /**
     *event...
     */
    fun getRoad(): Road {
        val map = Map
        return map.findRoadByVertex(sourceVertex, targetVertex)
    }
}

/**
 * Represents a construction site with a weight factor and a bool for if it's a one way
 */
data class ConstructionSite(
    override val id: Int,
    override var startingTick: Int,
    override val eventDuration: Int,
    override val endingTick: Int,
    val weightFactor: Int,
    val sourceVertex: Vertex,
    val targetVertex: Vertex,
    val turnIntoOneWay: Boolean
) : Event() {

    var ogtypeofroad: SecondaryRoadType = SecondaryRoadType.NONE
    override fun triggerEvent(): Boolean {
        val road = getRoad()
        // If there's already a global event or the road is closed
        return if (road.scheduledGlobalEvent != null || road.event != null) {
            // Procrastinate
            startingTick++
            false
        } else {
            // Event trigger logic
            ogtypeofroad = road.secondaryType
            if (turnIntoOneWay) {
                road.secondaryType = SecondaryRoadType.ONE_WAY_ROAD
            }
            road.weight *= weightFactor
            road.event = this
            Logger.logEventTriggered(id)
            true
        }
    }

    override fun reverseEvent(): Boolean {
        var triggerforECC = false
        val road = getRoad()
        if (road.event != null) {
            if (road.event?.id == this.id) { // If the event is the same one
                road.weight /= weightFactor
                road.secondaryType = ogtypeofroad
                road.event = null
                triggerforECC = true
                Logger.logEventEnded(id)
            }
        }
        return triggerforECC
    }

    /**
     *event...
     */
    fun getRoad(): Road {
        val map = Map
        return map.findRoadByVertex(sourceVertex, targetVertex)
    }
}

/**
 * Represents a closed road.
 * todo : if emergency on tick 1, but event also on tick 1 log it and open it regardless
 */
data class ClosedRoad(
    override val id: Int,
    override var startingTick: Int,
    override var eventDuration: Int,
    override val endingTick: Int,
    val sourceVertex: Vertex,
    val targetVertex: Vertex
) : Event() {
    var ogRoadWeight: Double = PLACEHOLDER_OG_ROADWEIGHT
    var triggeredalready: Boolean = false

    var delayedthistick: Boolean = false // Has delay road closing been called this tick?

    override fun triggerEvent(): Boolean {
        val road = getRoad()
        // If there is an emergency happening on the road
        if (road.emergencyHappeningOnRoad) {
            // procrastinate this
            startingTick++
            return if (delayedthistick) { // The road opened on this tick
                // So the map still has to update to reflect it
                delayedthistick = false
                true
            } else {
                false
            }
        } else { // No emergency on road
            // Trigger the event as usual
            if (road.scheduledGlobalEvent != null || (road.event != null && road.event != this)) {
                // If there's either a global event or another event already on the road...
                // Procrastinate - standard behaviour
                startingTick++
                return false
            } else {
                // Close the road
                ogRoadWeight = road.weight
                road.weight = Double.MAX_VALUE
                road.event = this
                if (!triggeredalready) { // Triggering for first time
                    Logger.logEventTriggered(id)
                    triggeredalready = true
                }
                return true
            }
        }
    }

    override fun reverseEvent(): Boolean {
        var triggeredforECC = false
        val road = getRoad()
        if (road.event != null) {
            if (road.event?.id == this.id) { // If the event is the same one
                road.weight = ogRoadWeight
                road.event = null
                triggeredforECC = true
                Logger.logEventEnded(id)
            }
        }
        return triggeredforECC
    }

    /**
     *event...
     */
    fun getRoad(): Road {
        val map = Map
        return map.findRoadByVertex(sourceVertex, targetVertex)
    }

    // (should literally only be one in EMCC) to reflect the new parameter of just a single int.
    // THIS IS FOR YOU TO USE, MAX
    /**
     *event...
     */
    fun delayRoadClosing(currentTick1: Int) {
        // This means that the event will be triggered this current tick
        // and keep procrastinating until the emergency is gone
        eventDuration = currentTick1 - startingTick
        startingTick = currentTick1
        // Conditional in trigger forces a map update
        delayedthistick = true
        // Open the road immediately
        getRoad().weight = ogRoadWeight
        // getRoad().event = null // YOU DON'T!! because no other events can happen while its closed
    }
}

/**
 * Represents a Vehicle Unavailable event with the ID of a vehicle to affect.
 */
data class VehicleUnavailable(
    override val id: Int,
    override var startingTick: Int,
    override val eventDuration: Int,
    override var endingTick: Int,
    val unavailableVehicleId: Int,
) : Event() {
    var ecc: ECC? = null
    var currentTickCopy: Int = -1

    init {
        endingTick = Int.MAX_VALUE
    }

    override fun triggerEvent(): Boolean {
        // find the vehicle you want to make unavailable
        val vehicles = ecc?.vehicles
        val vehicleUnavail = vehicles?.find { vehicle: Vehicle -> vehicle.vehicleId == unavailableVehicleId }
        when {
            vehicleUnavail != null -> {
                vehicleUnavail.unavailability = true
                if (!vehicleUnavail.recovering && vehicleUnavail.vehicleState == VehicleState.AT_BASE) {
                    this.endingTick = currentTickCopy + eventDuration
                    Logger.logEventTriggered(this.id)
                } else {
                    this.startingTick++ // procrastinate
                    return false
                }
            }
        }
        return true
    }

    override fun reverseEvent(): Boolean {
        val vehicles = ecc?.vehicles
        val vehicleUnavail = vehicles?.find { vehicle: Vehicle -> vehicle.vehicleId == unavailableVehicleId }
        vehicleUnavail?.unavailability = false
        return true
    }

    override fun checkIfEndedPreviously(currentTick: Int): Boolean {
        if (currentTick >= endingTick) {
            return true
        } else {
            return false
        }
    }
}
