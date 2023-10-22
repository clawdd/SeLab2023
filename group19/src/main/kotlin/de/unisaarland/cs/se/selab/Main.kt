package de.unisaarland.cs.se.selab

import de.unisaarland.cs.se.selab.base.Base
import de.unisaarland.cs.se.selab.base.FireStation
import de.unisaarland.cs.se.selab.base.Hospital
import de.unisaarland.cs.se.selab.base.PoliceStation
import de.unisaarland.cs.se.selab.emergency.Emergency
import de.unisaarland.cs.se.selab.enumtype.VehicleState
import de.unisaarland.cs.se.selab.enumtype.VehicleType
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.Navigation
import de.unisaarland.cs.se.selab.parser.CommandLineParser
import de.unisaarland.cs.se.selab.parser.DotParser
import de.unisaarland.cs.se.selab.parser.EmergencyEventParser
import de.unisaarland.cs.se.selab.parser.VehicleBaseParser
import de.unisaarland.cs.se.selab.vehicle.FireVehicle
import de.unisaarland.cs.se.selab.vehicle.MedicalVehicle
import de.unisaarland.cs.se.selab.vehicle.PoliceVehicle
import de.unisaarland.cs.se.selab.vehicle.Vehicle

/**
 * This is the entry point of the simulation.
 */
fun main(args: Array<String>) {
    // need: change initiated values
    val pathToDotFile: String
    val pathToBaseVehicleConfig: String
    val pathToEmergencyEventConfig: String
    // still need logic for that in commandline or parser
    val maxTick: Int
    // var outputPath: String

    val simulation: Simulation

    val cmdParser = CommandLineParser(args)

    // Logger.setOutputFile(cmdParser.getOutputPath())

    pathToDotFile = cmdParser.getMapPath()
    pathToBaseVehicleConfig = cmdParser.getAssetsPath()
    pathToEmergencyEventConfig = cmdParser.getScenarioPath()
    // here we have all necessary path, and start parsing

    if (DotParser(pathToDotFile).parseMap()) {
        Logger.logSuccessfulParse(pathToDotFile)
    } else {
        Logger.logUnsuccessfulParse(pathToDotFile)
        return
    }
    val assetsParser = VehicleBaseParser(pathToBaseVehicleConfig)
    val allBasesList = assetsParser.parseBases()
    val allVehiclesList = assetsParser.helperParseVehicles()
    // val baseValidator = BaseValidator(assetsParser.getValidFlag(), assetsParser)
    if (!assetsParser.getValidFlag()) {
        Logger.logUnsuccessfulParse(cmdParser.getAssetsName())
        return
    } else {
        Logger.logSuccessfulParse(cmdParser.getAssetsName())
    }

    val scenarioParser = EmergencyEventParser(pathToEmergencyEventConfig)
    val allEventsList = scenarioParser.parseEvents(allVehiclesList)
    val allEmergenciesList = scenarioParser.parseEmergencies()
    if (scenarioParser.getValidFlag()) {
        Logger.logSuccessfulParse(cmdParser.getScenarioName())
    } else {
        Logger.logUnsuccessfulParse(cmdParser.getScenarioName())
        return
    }

    // create emcc, ecc and add necessary
    assignAllVehiclesToBases(allVehiclesList, allBasesList)
    // fills all watertrucks with the max amount of water
    fillUpWaterTrucks(allVehiclesList)

    // errors in emcc class @MAX
    val emcc = EMCC(allEmergenciesList.toMutableList(), mutableListOf<Emergency>(), allBasesList, 0)
    addEMCCToBases(allBasesList, emcc)
    // set services is also done in the create method above
    val ecc = ECC(allEventsList, allVehiclesList)
    Map.setBasesToVertices(allBasesList)
    maxTick = getMaxTick(cmdParser.getMaxTickFromCmd(), allEmergenciesList)
    simulation = Simulation(emcc, ecc, maxTick)

    // initializes Navigation class
    Navigation.initNavi(allBasesList)

    Logger.logPrintInfo("Simulation starts")
    // only for detekt
    // Logger.logEmergencyFailed(0)
    // Logger.logEmergencyResolved(0)
    // Logger.logEmergencyAssignment(0, 0)
    simulation.runSimulation()
}

/**
 * set all the vehicle's state to at base
 */
private fun assignAllVehiclesToBases(allVehiclesList: List<Vehicle>, allBasesList: List<Base>) {
    for (base in allBasesList) {
        for (vehicle in allVehiclesList) {
            vehicle.vehicleState = VehicleState.AT_BASE
            assignVehicleToBase(vehicle, base)

            if (vehicle.homeBaseId == base.baseID) {
                vehicle.route.add(base.location)
            }
        }
    }
}

/**
 * TODO
 *
 * @param vehicle
 * @param base
 */
private fun assignVehicleToBase(
    vehicle: Vehicle,
    base: Base
) {
    if (vehicle.homeBaseId == base.baseID) {
        when (base) {
            is FireStation -> base.assignedVehicles.add(vehicle as FireVehicle)
            is Hospital -> base.assignedVehicles.add(vehicle as MedicalVehicle)
            is PoliceStation -> base.assignedVehicles.add(vehicle as PoliceVehicle)
        }
    }
}

/**
 * add emcc to all the bases
 */
private fun addEMCCToBases(allBasesList: List<Base>, emcc: EMCC) {
    for (i in 0 until allBasesList.size) {
        allBasesList[i].emcc = emcc
    }
}

/**
 * get maxtick, either from emergency or commandline
 */
private fun getMaxTick(cmdTick: Int?, allEmList: List<Emergency>): Int {
    var currMaxTick = 0
    if (cmdTick != null) {
        return cmdTick
    } else {
        for (i in 0 until allEmList.size) {
            val temp = allEmList[i].maxDuration + allEmList[i].occurringTick
            if (temp > currMaxTick) {
                currMaxTick = temp
            }
        } // for loop end
        return currMaxTick
    }
}

private fun fillUpWaterTrucks(allVehiclesList: List<Vehicle>) {
    val waterTrucks = allVehiclesList.filter { it.vehicleType == VehicleType.FIRE_TRUCK_WATER }
    for (waterTruck in waterTrucks) {
        if (waterTruck is FireVehicle) {
            waterTruck.waterLevel = waterTruck.waterCapacity
        }
    }
}
