package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.Logger

/**
 *to parse commandline
 */
class CommandLineParser(private val args: Array<String>) {
    private var mapPath: String? = null
    private var assetsPath: String? = null
    private var scenarioPath: String? = null
    private var ticks: Int? = null
    private var outputPath: String? = null

    init {
        parseArgs(args)
    }

    /**
     *to parse commandline
     */
    private fun parseArgs(args: Array<String>) {
        // use this.args visit
        for (i in 0 until args.size) {
            when (args[i]) {
                "--map", "-m" -> mapPath = args.getOrNull(i + 1)
                "--assets", "-a" -> assetsPath = args.getOrNull(i + 1)
                "--scenario", "-s" -> scenarioPath = args.getOrNull(i + 1)
                "--ticks", "-t" -> ticks = args.getOrNull(i + 1)?.toIntOrNull()
                "--out", "-o" -> outputPath = args.getOrNull(i + 1)
                "--help" -> {
                    printUsage()
                    return
                }
                else -> {
                    continue
                }
            }
        }

        /*
        if (mapPath == null || assetsPath == null || scenarioPath == null) {
            Logger.logPrintInfo("Required parameters are missing. Use --help for usage information.")
            return
        }
        */
        // if need to output result, then write it into file or print to stdout
        /*
        if (outputPath != null) {
            // 将结果写入指定的文件
        } else {
            // print to stdout
        }
        */
    }

    private fun printUsage() {
        Logger.logPrintInfo("Usage:")
        Logger.logPrintInfo("--map Path to the map. (always required) String")
        Logger.logPrintInfo("--assets Path to the file with assets. (always required) String")
        Logger.logPrintInfo("--scenario Path to the scenario file. (always required) String")
        Logger.logPrintInfo("--ticks Maximum allowed number of simulation ticks. Int")
        Logger.logPrintInfo("--out Path to output file. Uses 'stdout' by default. String")
        Logger.logPrintInfo("--help Usage info")
    }

    /**
     *to get map config
     */
    fun getMapPath(): String {
        return mapPath.toString()
    }

    /*
     *to get map config name

    fun getMapName(): String {
        return mapPath.toString().substringAfterLast("/")
    }
    */

    /**
     *to get assets config
     */
    fun getAssetsPath(): String {
        return assetsPath.toString()
    }

    /**
     *to get asset config name
     */
    fun getAssetsName(): String {
        return assetsPath.toString().substringAfterLast("/")
    }

    /**
     *to get scenario config
     */
    fun getScenarioPath(): String {
        return scenarioPath.toString()
    }

    /**
     *to get scenario config name
     */
    fun getScenarioName(): String {
        return scenarioPath.toString().substringAfterLast("/")
    }

    /**
     *to get maxtick
     */
    fun getMaxTickFromCmd(): Int? {
        return ticks
    }

    /**
     *
     * @return output path
     */
    fun getOutputPath(): String {
        return outputPath.toString()
    }
}
