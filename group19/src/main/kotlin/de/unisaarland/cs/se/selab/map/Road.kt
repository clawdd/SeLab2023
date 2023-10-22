package de.unisaarland.cs.se.selab.map

import de.unisaarland.cs.se.selab.event.Event

/**
 * Documentation...
 */
class Road(
    val sourceVertex: Vertex,
    val targetVertex: Vertex,
    val primaryType: PrimaryRoadType,
    var secondaryType: SecondaryRoadType,
    val name: String,
    val villageName: String,
    var weight: Double, // should be positive
    val height: Int, // >= 1
) {
    var event: Event? = null
    var scheduledGlobalEvent: Event? = null

    // BIG TODO - cater for emergencyHappeningOnRoad in EMCC!
    // when you set an EM to successful or failed
    // , set this to false and when you assign it, set it to true
    var emergencyHappeningOnRoad: Boolean = false

    override fun toString(): String {
        return name
    }
}
