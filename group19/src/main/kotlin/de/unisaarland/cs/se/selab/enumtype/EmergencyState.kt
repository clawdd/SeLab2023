package de.unisaarland.cs.se.selab.enumtype

/**
 * Represents the current state of an emergency.
 */
enum class EmergencyState {
    PENDING,
    ALL_ASSETS_ASSIGNED,
    CURRENTLY_BEING_HANDLED,
    SUCCESSFUL,
    FAIL
}
