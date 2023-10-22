package maxunittests

import de.unisaarland.cs.se.selab.base.BaseFactory
import de.unisaarland.cs.se.selab.emergency.FireEmergencyFactory
import de.unisaarland.cs.se.selab.map.Vertex
import kotlin.test.Test

class TestSorting {

    @Test
    fun testSort() {
        val base = BaseFactory().createFireStation(0, Vertex(1), 30)
        base.assignedEmergencies.addAll(
            listOf(
                FireEmergencyFactory().createFireLevelTwo(listOf(2, 0, 0, 0), "", ""),
                FireEmergencyFactory().createFireLevelTwo(listOf(1, 0, 0, 0), "", ""),
                FireEmergencyFactory().createFireLevelThree(listOf(6, 0, 0, 0), "", ""),
                FireEmergencyFactory().createFireLevelThree(listOf(5, 0, 0, 0), "", ""),
            )
        )
        base.sortEmergencies()
        // base.assignedEmergencies.map { println(it.id) }
    }
}
