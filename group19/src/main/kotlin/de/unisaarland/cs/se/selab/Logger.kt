package de.unisaarland.cs.se.selab

import java.io.PrintWriter

/**
 * This function print the success init message.
 *
 * @param filepath need to be taken as part of printed message.
 * @return unit
 */
object Logger {
    // private const val outputPath: String = ""
    var oneTickrerouteAssetsCnt: Int = 0
    private var outputWriter: PrintWriter = PrintWriter(System.out, true)

    // below are all the statistic, will be +1 after each print.
    var timesRerouted: Int = 0
    var emergenciesAssigned: Int = 0
    var emergenciesFailed: Int = 0
    var emergenciesSuccessful: Int = 0

    /**
     * TODO
     *
     * @param filePath
     */
    fun setOutputFile(filePath: String) {
        if (filePath != "null") {
            outputWriter = PrintWriter(filePath)
        }
    }

    // i think for below 2 methods, argument should be filename??? parser init the data,
    /**
     * This function print the success init message.
     *
     * @param fileName need to be taken as part of printed message.
     * @return unit
     */
    fun logSuccessfulParse(fileName: String) {
        outputWriter.println("Initialization Info: $fileName successfully parsed and validated")
        outputWriter.flush()
    }

    /**
     * This function print the failed init message.
     *
     * @param fileName need to be taken as part of printed message.
     * @return unit
     */
    fun logUnsuccessfulParse(fileName: String) {
        outputWriter.println("Initialization Info: $fileName invalid")
        outputWriter.flush()
    }

    /**
     * This function print the start/end message, or any necessaty message when debugging
     *
     * @param message
     * @return unit
     */
    fun logPrintInfo(message: String) {
        outputWriter.println(message)
        // myLogger.info { "Simulation starts" }
        outputWriter.flush()
    }

    /**
     * This function print the start message.
     *
     * @param tickNumber, to print current tick.
     * @return unit
     */
    fun logSimulationTick(tickNumber: Int) {
        outputWriter.println("Simulation Tick: $tickNumber")
        outputWriter.flush()
    }

    /**
     * This function print the assigned message.
     *
     * @param emId give the emergency id and baseId.
     * @return unit
     */
    fun logEmergencyAssignment(emId: Int, baseId: Int) {
        outputWriter.println("Emergency Assignment: $emId assigned to $baseId")
        outputWriter.flush()
        this.emergenciesAssigned += 1
        // not sure whether should be assigned ems or just the original data from config file.
    }

    /**
     * This function print the allocated message.
     *
     * @param assetId,
     * @param emId,
     * @param ticksToArrived,
     * @return unit
     */
    fun logAssetAllocation(assetId: Int, emId: Int, ticksToArrived: Int) {
        // asset == vehicle, class diagram's method need arg: tickstoarrived,
        outputWriter.println("Asset Allocation: $assetId allocated to $emId; $ticksToArrived ticks to arrive.")
        outputWriter.flush()
    }

// reallo phase
    /**
     * This function print the reallocated message.
     *
     * @param assetId,
     * @param emId,
     * @return unit
     */
    fun logAssetReallocation(assetId: Int, emId: Int) {
        // asset == vehicle
        outputWriter.println("Asset Reallocation: $assetId reallocated to $emId.")
        outputWriter.flush()
    }

    /**
     * This function print the request message.
     *
     * @param requestId,
     * @param baseId,
     * @param emId,
     * @return unit
     */
    fun logAssetRequest(requestId: Int, baseId: Int, emId: Int) {
        outputWriter.println("Asset Request: $requestId sent to $baseId for $emId.")
        outputWriter.flush()
    }

// request phase
    /**
     * This function print the request failed message.
     *
     * @param emId,
     * @return unit
     */
    fun logFailedRequest(emId: Int) {
        outputWriter.println("Request Failed: $emId failed.")
        outputWriter.flush()
    }

// update phase
    /**
     * This function print the vehicles arrived message.
     *
     * @param assetId,
     * @param vertexId
     * @return unit
     */
    fun logAssetArrival(assetId: Int, vertexId: Int) {
        outputWriter.println("Asset Arrival: $assetId arrived at $vertexId.")
        outputWriter.flush()
    }

    /**
     * This function print the emergency handling message.
     *
     * @param emId,
     * @return unit
     */
    fun logEmergencyHandlingStart(emId: Int) {
        outputWriter.println("Emergency Handling Start: $emId handling started.")
        outputWriter.flush()
    }

    /**
     * This function print the emergency handling successed message.
     *
     * @param emId,
     * @return unit
     */
    fun logEmergencyResolved(emId: Int) {
        this.emergenciesSuccessful += 1
        outputWriter.println("Emergency Resolved: $emId resolved.")
        outputWriter.flush()
    }

    /**
     * This function print the emergency handling failed message.
     *
     * @param emId,
     * @return unit
     */
    fun logEmergencyFailed(emId: Int) {
        this.emergenciesFailed += 1
        outputWriter.println("Emergency Failed: $emId failed.")
        outputWriter.flush()
    }

    /**
     * This function print the old event end message.
     *
     * @param eventId,
     * @return unit
     */
    fun logEventEnded(eventId: Int) {
        outputWriter.println("Event Ended: $eventId ended.")
        outputWriter.flush()
    }

    /**
     * This function print the new event triggered message.
     *
     * @param eventId,
     * @return unit
     */
    fun logEventTriggered(eventId: Int) {
        outputWriter.println("Event Triggered: $eventId triggered.")
        outputWriter.flush()
    }

    /**
     * This function print the number of reroute me.
     *
     * @param rerouteAssetNumber
     * @return unit
     */
    // here deal with the logic of reroute vehicles and it's final statistic
    fun logReroutedAssetsNumberInOneTick() {
        if (oneTickrerouteAssetsCnt != 0) {
            outputWriter.println("Assets Rerouted: $oneTickrerouteAssetsCnt")
            outputWriter.flush()
        }
    }

    // here deal with the logic of reroute vehicles and it's final statistic
    /**
     * This function deal with the one tick, max tick all logic.
     *
     * @param b if is finished, then clear, if not then add
     * @return unit
     */
    fun logCntRerouteOneTickIsFinished(b: Boolean) {
        if (b == true) {
            this.oneTickrerouteAssetsCnt = 0
        } else {
            this.oneTickrerouteAssetsCnt++
            this.timesRerouted++
        }
    }

    // now we print all the statistic for the whole simulation:

    /**
     * This function print all the statistics
     *@param emcc
     * @return unit
     */
    fun logSimutationEnd(emcc: EMCC) {
        outputWriter.println("Simulation End")
        outputWriter.flush()
        outputWriter.println("Simulation Statistics: ${this.timesRerouted} assets rerouted.")
        outputWriter.flush()
        outputWriter.println("Simulation Statistics: ${this.emergenciesAssigned} received emergencies.")
        outputWriter.flush()
        outputWriter.println("Simulation Statistics: ${emcc.onGoingEmergencies.size} ongoing emergencies.")
        outputWriter.flush()
        outputWriter.println("Simulation Statistics: ${this.emergenciesFailed} failed emergencies.")
        outputWriter.flush()
        outputWriter.println("Simulation Statistics: ${this.emergenciesSuccessful} resolved emergencies.")
        outputWriter.flush()
    }
}
