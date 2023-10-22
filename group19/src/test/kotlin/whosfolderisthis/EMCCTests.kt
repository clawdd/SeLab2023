package whosfolderisthis

import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.emergency.FireEmergencyFactory
import kotlin.test.Test
import kotlin.test.assertNotEquals

class EMCCTests {
    // --- Helper functions ---
    private fun createListOfFireLevelOneEMs(amount: Int, tick: Int): MutableList<Emergency> {
        val emf: FireEmergencyFactory = FireEmergencyFactory()
        val emergencies: MutableList<Emergency> = mutableListOf()

        /*for (i in 1.amount) {
            emergencies.add(
                emf.createFireLevelOne(listOf(-1, tick, -1, -1), "", "")
            )
        }*/
        repeat(amount) {
            emergencies.add(
                emf.createFireLevelOne(listOf(-1, tick, -1, -1), "", "")
            )
        }
        return emergencies
    }

    // --- Tests ---

    @Test
    fun testMapCurrentTickToEmergencies1() {
        val emergenciesAtSameTick: List<Emergency> = createListOfFireLevelOneEMs(10, 5)
        val emergenciesAtSameTick2: List<Emergency> = createListOfFireLevelOneEMs(20, 15)
        assertNotEquals(emergenciesAtSameTick, emergenciesAtSameTick2)
    }
}
