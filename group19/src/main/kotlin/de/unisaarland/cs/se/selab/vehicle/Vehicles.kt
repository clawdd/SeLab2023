package de.unisaarland.cs.se.selab.vehicle

import kotlin.math.min

/**
 * TODO
 * doc
 */
fun <V : Vehicle> List<V>.generateAllSubsets(): List<List<V>> {
    if (isEmpty()) {
        return listOf(emptyList())
    }

    val head = first()
    val tail = drop(1)
    val subsetsWithoutHead = tail.generateAllSubsets()
    val subsetsWithHead = subsetsWithoutHead.map { it + head }

    return subsetsWithHead + subsetsWithoutHead
}

/**
 * TODO
 *
 * @param sortedAllListsByID
 * @return
 */
fun <V : Vehicle> searchForLowestIDSubset(sortedAllListsByID: List<List<V>>): List<V> {
    /*
    val result = mutableListOf<V>()

    for (i in 0 until sortedAllListsByID.first().size) {
        val minVal: Int = sortedAllListsByID.minOf { it[i].vehicleId }
        val minSubsets = sortedAllListsByID.filter { it[i].vehicleId == minVal }

        if (minSubsets.size == 1) {
            result.addAll(minSubsets[0])
            break
        }
    }
    return result.toList()
     */

    val listOfLists: MutableList<List<V>> = mutableListOf()

    for (vList in sortedAllListsByID) {
        val sortedList = vList.sortedBy { it.vehicleId }
        listOfLists.add(sortedList)
    }

    listOfLists.sortWith(::compareVehicleLists)

    return listOfLists[0]
}

private fun <V : Vehicle> compareVehicleLists(vA: List<V>, vB: List<V>): Int {
    val minLength = min(vA.size, vB.size)
    for (i in 0 until minLength) {
        val cmp = compareValues(vA[i].vehicleId, vB[i].vehicleId)
        if (cmp != 0) {
            return cmp
        }
    }
    return compareValues(vA.size, vB.size)
}
