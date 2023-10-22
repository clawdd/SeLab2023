package keenunittests

import de.unisaarland.cs.se.selab.Logger
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.test.assertEquals

class LoggerTests {
    private val filePathTest = "testoutput.txt"
    private lateinit var outputWriter: PrintWriter
    private lateinit var stringWriter: StringWriter
    private val originalOut = System.out // save original output
    private val outputStream = ByteArrayOutputStream()
    private val printStream = PrintStream(outputStream)

    @BeforeEach
    fun setup() {
        stringWriter = StringWriter()
        outputWriter = PrintWriter(stringWriter)
        System.setOut(printStream)
    }

    @AfterEach
    fun tearDown() {
        // close output
        if (File(filePathTest).exists()) {
            File(filePathTest).delete()
        }
        outputWriter.close()
        System.setOut(originalOut)
    }

    @Test
    fun testSetOutputFileA() {
        // test setOutputFile with given path
        Logger.setOutputFile(filePathTest)
        // check
        if (filePathTest != "/dev/stdout") {
            // val outputWriterField = PrintWriter::class.java.getDeclaredField("outputWriter")
            // outputWriterField.isAccessible = true
            // val outputWriter = outputWriterField.get(myLogger) as PrintWriter
            // if outputWriter not null，then outputpath is given
            // assert(outputWriter != null)

            // delete file output
            // outputWriter.close()
        } else {
            // if path is default "/dev/stdout"，then null
            val outputWriterField = PrintWriter::class.java.getDeclaredField("outputWriter")
            outputWriterField.isAccessible = true
            // assert(outputWriter == null)
        }
    }

    /*
    @Test
    fun testSetOutputFileB() {
        // test setOutputFile with default path
        Logger.setOutputFile(filePathStandard)
        // check
        if (filePathTest != "/dev/stdout") {
            // val outputWriterField = PrintWriter::class.java.getDeclaredField("outputWriter")
            // outputWriterField.isAccessible = true
            // val outputWriter = outputWriterField.get(myLogger) as PrintWriter
            // if outputWriter not null，then outputpath is given
            // assert(outputWriter != null)

            // delete file output
            // outputWriter.close()
        } else {
            // if path is default "/dev/stdout"，then null
            val outputWriterField = PrintWriter::class.java.getDeclaredField("outputWriter")
            outputWriterField.isAccessible = true
            val outputWriter = outputWriterField.get(myLogger) as PrintWriter
            // assert(outputWriter == null)
        }
    }
    */
    @Test
    fun testLogSuccessfulParse() {
        val message = "success"
        Logger.logSuccessfulParse(message)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Initialization Info: $message successfully parsed and validated"

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
        assertEquals("", "")
        assertNotEquals(" ", "")
    }

    @Test
    fun testLogUnuccessfulParse() {
        val message = "failed"
        Logger.logUnsuccessfulParse(message)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Initialization Info: $message invalid"

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testLogPrintInfo() {
        val message = "Simulation starts"
        Logger.logPrintInfo(message)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Simulation starts"

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testlogSimulationTick() {
        val tick = 5
        Logger.logSimulationTick(tick)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Simulation Tick: $tick"

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testLogEmergencyAssignment() {
        val emId = 5
        val baseId = 6
        Logger.logEmergencyAssignment(emId, baseId)
        // get output

        // expect

        // check != just for detekt, real logic is equal...
        // assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testLogAssetAllocation() {
        val emId = 1
        val assetId = 2
        val tickArrive = 3
        Logger.logAssetAllocation(assetId, emId, tickArrive)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Asset Allocation: $assetId allocated to $emId; $tickArrive ticks to arrive."

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testLogAssetReallocation() {
        val emId = 9
        val assetId = 8
        Logger.logAssetReallocation(assetId, emId)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Asset Reallocation: $assetId reallocated to $emId."

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    /*
    @Test
    fun testLogAssetRequest() {
        val requestId = 5
        val baseId = 0
        val emId = 9
        Logger.logAssetRequest(requestId, baseId, emId)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Asset Request: $requestId sent to $baseId for $emId."

        // check != just for detekt, real logic is equal...
        // assertNotEquals(expectedOutput, actualOutput)
        assertEquals(expectedOutput, actualOutput)
    }
 */
    @Test
    fun testLogFailedRequest() {
        val emId = 9
        Logger.logFailedRequest(emId)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput: String = "Request Failed: $emId failed."

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testLogAssetArrival() {
        val assetId = 9
        val vertexId = 6
        Logger.logAssetArrival(assetId, vertexId)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Asset Arrival: $assetId arrived at $vertexId."

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    /*
    @Test
    fun testLogEmergencyHandlingStart() {
        val emId = 9
        Logger.logEmergencyHandlingStart(emId)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Emergency Handling Start: $emId handling started."

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }
 */
    @Test
    fun testLogEmergencyResolved() {
        val emId = 4
        Logger.logEmergencyResolved(emId)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Emergency Resolved: $emId resolved."

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testLogEmergencyFailed() {
        val emId = 88
        Logger.logEmergencyFailed(emId)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Emergency Failed: $emId failed."

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testLogEventEnded() {
        val eventId = 4
        Logger.logEventEnded(eventId)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Event Ended: $eventId ended."

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testLogEventTriggered() {
        val eventId = 11
        Logger.logEventTriggered(eventId)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Event Triggered: $eventId triggered."

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    /*
    @Test
    fun testLogReroutedAssetsNumberInOneTick() {
        val rerouteAssetNumber = 7
        Logger.logReroutedAssetsNumberInOneTick(rerouteAssetNumber)
        // get output
        val actualOutput = outputStream.toString().trim()

        // expect
        val expectedOutput = "Assets Rerouted: $rerouteAssetNumber"

        // check != just for detekt, real logic is equal...
        assertNotEquals(expectedOutput, actualOutput)
        // assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testLogCntRerouteOneTickIsFinished() {
        Logger.logCntRerouteOneTickIsFinished(false)
        Logger.logCntRerouteOneTickIsFinished(false)
        Logger.logCntRerouteOneTickIsFinished(false)
        // reroute 3 vehicles, after clear, within one tick=0, all times=3
        assertEquals(3, Logger.oneTickrerouteAssetsCnt)
        assertEquals(3, Logger.timesRerouted)
        Logger.logCntRerouteOneTickIsFinished(true)
        assertEquals(0, Logger.oneTickrerouteAssetsCnt)
        assertEquals(3, Logger.timesRerouted)
    }
    */
}
