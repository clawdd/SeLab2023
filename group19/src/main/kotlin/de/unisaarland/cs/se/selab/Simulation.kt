package de.unisaarland.cs.se.selab

import de.unisaarland.cs.se.selab.enumtype.EmergencyState
import de.unisaarland.cs.se.selab.map.Map

/**
 * Doc...
 */
class Simulation(
    val emcc: EMCC,
    val ecc: ECC,
    val maxTick: Int
) {

    var currentTick: Int = 0
    var logger = Logger

    /**
     * Called from main
     */
    fun runSimulation() {
        // Code to run the entire simulation
        while (currentTick < maxTick) {
            if (
                emcc.allOccurringEmergencies?.all {
                    it.emergencyState == EmergencyState.SUCCESSFUL ||
                        it.emergencyState == EmergencyState.FAIL
                } != false
            ) {
                break
            }
            Logger.logSimulationTick(currentTick)
            runEmergencyPhase()
            runPlanningPhase()
            runRequestPhase()
            runUpdatePhase()
            currentTick++
        }
        runEvaluationPhase()
    }

    private fun runEmergencyPhase() {
        emcc.receiveCurrentEmergencies(currentTick)

        // other logic
    }

    private fun runPlanningPhase() {
        emcc.sortEmergenciesAllBases()
        emcc.assignAssetsAllBases(currentTick)

        // other logic
    }

    private fun runRequestPhase() {
        emcc.handleRequests(currentTick)

        // other logic
    }

    private fun runUpdatePhase() {
        emcc.updateAssetsAllBases()
        emcc.updateEMCCEmergenciesStatus(currentTick)

        ecc.endEvents(currentTick)
        ecc.receiveNewEvents(currentTick)

        Map.updateAdjacencyMatrix()

        ecc.rerouteVehicles()
        Logger.logReroutedAssetsNumberInOneTick()
        Logger.logCntRerouteOneTickIsFinished(true)
        // other logic
    }

    private fun runEvaluationPhase() {
        Logger.logSimutationEnd(emcc)
    }
}
