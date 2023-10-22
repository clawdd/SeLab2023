package de.unisaarland.cs.se.selab.base

import de.unisaarland.cs.se.selab.emergency.Emergency

/**
 * doc...
 */
class Request(
    var emergency: Emergency,
    var requestID: Int,
    var initialRequester: Base,
    var listOfNearestBases: MutableList<Base>?
)
