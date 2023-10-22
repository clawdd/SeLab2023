package de.unisaarland.cs.se.selab

import de.unisaarland.cs.se.selab.event.Event
import de.unisaarland.cs.se.selab.event.VehicleUnavailable
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.vehicle.Vehicle

/**
 * Doc...
 */
class ECC(var allOccurringEvents: List<Event>, var vehicles: List<Vehicle> = emptyList()) {
    var ongoingEvents: List<Event> = emptyList()
    var eventChanged: Boolean = false

    /**
     * Each VehicleUnavailable needs the ECC so that it can loop through the list of vehicles
     * I didn't want to complicate the triggering logic or mess with the parser, so I've put
     * the logic to initialize that here.
     */
    init {
        // Initialize the ECC attribute for all VehicleUnavailable events
        for (event in allOccurringEvents) {
            if (event is VehicleUnavailable) {
                event.ecc = this
            }
        }
    }

    /**
     * Reverse the effect of any event that might have ended this tick, and
     * //todo : log it
     */
    fun endEvents(currentTick: Int) {
        ongoingEvents = ongoingEvents.sortedBy { it.id }
        for (event in ongoingEvents) {
            if (event.checkIfEndedPreviously(currentTick)) {
                // if an event in the ongoingEvents list has ended, reverse it!
                val reversed = event.reverseEvent()
                eventChanged = eventChanged || reversed
            }
        }
    }

    /**
     * Set ongoing events to only the list of events happening as of this tick
     * And trigger
     */
    fun receiveNewEvents(currentTick: Int) {
        // Get the list of all events that haven't ended
        ongoingEvents = allOccurringEvents.filter { !it.checkIfEndedPreviously(currentTick) }.toMutableList()
        // And make it have only ONGOING events i. e who are not in the future, and haven't ended
        ongoingEvents = ongoingEvents.filter { it.startingTick <= currentTick }.sortedBy { it.id }
        triggerEvents(currentTick)
        Map.updateAdjacencyMatrix()
    }

    private fun triggerEvents(currentTick: Int) {
        val eventsToTrigger = ongoingEvents.filter { it.startingTick == currentTick }.sortedBy { it.id }
        val iterator = eventsToTrigger.iterator()
        // Iterate through the list of ongoing events
        while (iterator.hasNext()) {
            val event = iterator.next()
            if (event is VehicleUnavailable) {
                // tryDisableVehicle(event)
                event.currentTickCopy = currentTick
            }
            val triggeredSuccess = event.triggerEvent()
            if (!triggeredSuccess) {
                // If we couldn't trigger the event and procrastinated it...
                // remove the event from the ongoingEvents list
                ongoingEvents = ongoingEvents.filter { it != event }
            } else {
                eventChanged = true // An event changed the map by triggering
            }
        }
    }

    private fun rerouteVehiclesEnRoute() {
        for (vehicle in vehicles) {
            vehicle.reroute()
        }
    }

    /**
     * doc...
     */
    fun rerouteVehicles() {
        if (eventChanged) {
            rerouteVehiclesEnRoute()
            eventChanged = false
        }
    }

    /*
    /**
     * Decrements unavailability timer for the vehicle
     * If the timer hits 0, we set the vehicle to available again
     */
    fun checkUnavailability() {
        val unavailableVehicles = this.vehicles.filter { it.unavailability }
        for (unavailable in unavailableVehicles) {
            if (unavailable.unavailableFor > 0) {
                unavailable.unavailableFor--
            } else {
                unavailable.unavailability = false
            }
        }
    }
    */
}

// COMMENTED, OLD FUNCS INCASE I BREAK SMTH

/*/**
     * sets newEvents to the list of events that start in the currentTick
     * @param currentTick: the tick the simulation is currently on
     */
    fun receiveNewEvents(currentTick: Int) {
        // Remove events that have already ended, backup the old ones so you can remove them later
        oldEvents = ongoingEvents
        ongoingEvents = allOccurringEvents.filter { !it.checkIfEndedPreviously(currentTick) }.toMutableList()
        triggerEvents()
        Map.updateAdjacencyMatrix()
    }*/

/*/**
 * Reverse the effect of any event that might have ended this tick
 * Backup the events that were happening last tick to oldEvents.
 */
fun endEvents(currentTick: Int) {
    for (event in oldEvents) {
        if (event.checkIfEndedPreviously(currentTick)) {
            // if an event in the ongoingEvents list has ended
            event.reverseEvent()
        }
    }
}*/

/**
 * saves the events from the last tick inside oldEvents
 * then updates ongoingEvents
 * @return: Event list with events that are currently ongoing (event.checkIfEndedPreviously == false)
 */
/*private fun mapCurrentTickToEvents(): List<Event> {
    ongoingEvents = allOccurringEvents.filter { !it.checkIfEndedPreviously(currentTick) }
    return ongoingEvents
}*/

/**
 * calls the triggerEvent method on every event in currentTicksEvents
 * param currentTicksEvents: the events that start this tick
 */
/*private fun triggerEvents(currentTicksEvents: List<Event>) {
    for (event in currentTicksEvents) {
        val triggeredsuccess = event.triggerEvent()
        if (!triggeredsuccess) {
            // how do I remove something from the list??
            // this should do the trick but like
            // remove command??
            ongoingEvents.filter { it != event }
        }
    }
}*/

/*/**
     * @return: true if oldEvents != ongoingEvents, false if oldEvents == ongoingEvents
     */
    private fun checkIfReroutingNecessary(): Boolean {
        return oldEvents != ongoingEvents
        // my new triggerevents func means that if something procrastinates
        // it won't be in ongoingEvents because it's changed nothing.
}*/
