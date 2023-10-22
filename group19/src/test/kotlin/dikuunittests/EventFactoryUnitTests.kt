package dikuunittests

import de.unisaarland.cs.se.selab.ECC
import de.unisaarland.cs.se.selab.event.EventFactory
import de.unisaarland.cs.se.selab.map.Map
import de.unisaarland.cs.se.selab.map.PrimaryRoadType
import de.unisaarland.cs.se.selab.map.Road
import de.unisaarland.cs.se.selab.map.SecondaryRoadType
import de.unisaarland.cs.se.selab.map.Vertex
import de.unisaarland.cs.se.selab.vehicle.Vehicle
import de.unisaarland.cs.se.selab.vehicle.VehicleIntParameters
import de.unisaarland.cs.se.selab.vehicle.VehiclePoliceFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test

class EventFactoryUnitTests {

    // --- Event Factory Basic UnitTests ---

    @Test
    fun testRushHourSingleRoadType() {
        val factory = EventFactory()

        val args = listOf(0, 1, 2, 3) // ID 0, start on Tick 1, lasts for 2 ticks
        val roadTypes = listOf(PrimaryRoadType.MAIN_STREET)
        val weightFactor = 2

        val rushHour = factory.createRushHour(args, roadTypes, weightFactor)

        assertEquals(0, rushHour.id)
        assertEquals(1, rushHour.startingTick)
        assertEquals(2, rushHour.eventDuration)
        // I do not check ending tick on purpose here, since I'm trying to refactor it out.
        assertEquals(roadTypes, rushHour.affectedRoadTypes)
        assertEquals(weightFactor, rushHour.weightFactor)
    }

    @Test
    fun testRushHourMultipleRoadTypes() {
        val factory = EventFactory()

        val args = listOf(80, 4, 5, 0) // ID 80, start on Tick 4, lasts for 2 ticks
        // Notice how I'm purposely making a conflicting ending tick here
        val roadTypes = listOf(PrimaryRoadType.MAIN_STREET, PrimaryRoadType.COUNTY_ROAD)
        val weightFactor = 5

        val rushHour = factory.createRushHour(args, roadTypes, weightFactor)

        assertEquals(80, rushHour.id)
        assertEquals(4, rushHour.startingTick)
        assertEquals(5, rushHour.eventDuration)
        assertEquals(roadTypes, rushHour.affectedRoadTypes)
        assertEquals(weightFactor, rushHour.weightFactor)
    }

    @Test
    fun testCreateTrafficJam() {
        val factory = EventFactory()
        val args = listOf(2, 2, 3, 10) // ID 2, Starting Tick 2, Duration 3
        val vertices = listOf(Vertex(1), Vertex(3))
        val weightFactor = 5

        val trafficJam = factory.createTrafficJam(args, vertices, weightFactor)

        assertEquals(2, trafficJam.id)
        assertEquals(2, trafficJam.startingTick)
        assertEquals(3, trafficJam.eventDuration)
        assertEquals(vertices[0], trafficJam.sourceVertex)
        assertEquals(vertices[1], trafficJam.targetVertex)
        assertEquals(weightFactor, trafficJam.weightFactor)
    }

    @Test
    fun testCreateConstructionSite() {
        val factory = EventFactory()
        val args = listOf(1, 2, 3, 4)
        val vertices = listOf(Vertex(5), Vertex(10))
        val oneWay = true
        val weightFactor = 5

        val constructionSite = factory.createConstructionSite(args, vertices, oneWay, weightFactor)

        assertEquals(1, constructionSite.id)
        assertEquals(2, constructionSite.startingTick)
        assertEquals(3, constructionSite.eventDuration)
        assertEquals(4, constructionSite.endingTick)
        assertEquals(vertices[0], constructionSite.sourceVertex)
        assertEquals(vertices[1], constructionSite.targetVertex)
        assertEquals(oneWay, constructionSite.turnIntoOneWay)
        assertEquals(weightFactor, constructionSite.weightFactor)
    }

    @Test
    fun testCreateClosedRoad() {
        val factory = EventFactory()
        val args = listOf(1, 2, 3, 4)
        val vertices = listOf(Vertex(11), Vertex(13))

        val closedRoad = factory.createClosedRoad(args, vertices)

        assertEquals(1, closedRoad.id)
        assertEquals(2, closedRoad.startingTick)
        assertEquals(3, closedRoad.eventDuration)
        assertEquals(4, closedRoad.endingTick)
        assertEquals(vertices[0], closedRoad.sourceVertex)
        assertEquals(vertices[1], closedRoad.targetVertex)
    }

    /*
    @Test
    fun testCreateVehicleUnavailable() {
        val factory = EventFactory()
        val args = listOf(1, 2, 3, 4)
        val unVehicleId = 42

        val vehicleUnavailable = factory.createVehicleUnavailable(args, unVehicleId)

        assertEquals(1, vehicleUnavailable.id)
        assertEquals(2, vehicleUnavailable.startingTick)
        assertEquals(3, vehicleUnavailable.eventDuration)
        assertEquals(4, vehicleUnavailable.endingTick)
        assertEquals(unVehicleId, vehicleUnavailable.unavailableVehicleId)
    }*/
}

class EventFactoryTriggerTests {

    companion object {
        lateinit var factory: EventFactory

        /**
         * Pre-initialize the factory so that all the subsequent tests can use it
         */
        @BeforeAll
        @JvmStatic
        fun setUp() {
            factory = EventFactory()
        }
    }

    // --- Test the Triggering and subsequent reversal of each event type. ---

    @Test
    fun testTrafficJamTriggerReverse() {
        // Make a traffic jam with said attributes
        val args = listOf(2, 2, 3, 10) // ID 2, Starting Tick 2, Duration 3
        val vertices = listOf(Vertex(1), Vertex(3))
        val weightFactor = 5
        val trafficJam = factory.createTrafficJam(args, vertices, weightFactor)

        // Make the road it's going to happen on
        val road = Road(
            vertices[0],
            vertices[1],
            PrimaryRoadType.MAIN_STREET,
            SecondaryRoadType.NONE,
            "Road1",
            "Village1",
            1.0,
            1
        )
        // Add the road to the map so triggerEvent can find it
        Map.setAttributes(vertices, listOf(road), "")
        val result = trafficJam.triggerEvent()

        // Verify that the event was triggered
        assertTrue(result)
        assertEquals(5.0, road.weight)
        assertEquals(trafficJam, road.event)

        // Then, reveerse it and verify that that works properly too.
        trafficJam.reverseEvent()
        assertEquals(1.0, road.weight)
        assertNotEquals(trafficJam, road.event)
    }

    @Test
    fun testConstructionSiteTriggerReverse() {
        // Create a ConstructionSite with the specified attributes
        val args = listOf(3, 4, 5, 6) // ID 3, Starting Tick 4, Duration 5
        val vertices = listOf(Vertex(2), Vertex(4))
        val oneWay = true
        val weightFactor = 3
        val constructionSite = factory.createConstructionSite(args, vertices, oneWay, weightFactor)

        // Create the road it's going to happen on
        val road = Road(
            vertices[0],
            vertices[1],
            PrimaryRoadType.MAIN_STREET,
            SecondaryRoadType.TUNNEL,
            "Road2",
            "Village2",
            1.5,
            1
        )

        // Add the road to the map so triggerEvent can find it
        Map.setAttributes(vertices, listOf(road), "")

        // Trigger the event and verify that it was triggered successfully
        val result = constructionSite.triggerEvent()
        assertTrue(result)
        assertEquals(4.5, road.weight)
        assertEquals(SecondaryRoadType.ONE_WAY_ROAD, road.secondaryType)
        assertEquals(constructionSite, road.event)

        // Reverse the event and verify that it was reversed properly
        constructionSite.reverseEvent()
        assertEquals(1.5, road.weight)
        assertEquals(SecondaryRoadType.TUNNEL, road.secondaryType) // check if we preserved road typ
        assertNotEquals(constructionSite, road.event)
    }

    @Test
    fun testRushHourTriggerReverse() {
        // Road Creation
        val listvertices = listOf(Vertex(1), Vertex(2), Vertex(10), Vertex(20), Vertex(42), Vertex(43))

        val road1 = Road( // Make a road going from 1 to 2
            listvertices[0],
            listvertices[1],
            PrimaryRoadType.MAIN_STREET,
            SecondaryRoadType.NONE,
            "Road1",
            "Village2",
            1.5,
            3
        )
        val road2 = Road( // Make a road going from 10 to 20
            listvertices[2],
            listvertices[3],
            PrimaryRoadType.COUNTY_ROAD,
            SecondaryRoadType.ONE_WAY_ROAD,
            "Road2",
            "",
            10.0,
            3
        )

        val road3 = Road( // Make a road going from 10 to 20
            Vertex(42),
            Vertex(43),
            PrimaryRoadType.SIDE_STREET,
            SecondaryRoadType.NONE,
            "Road3",
            "",
            10.0,
            3
        )

        // Add the road to the map so triggerEvent can find it
        Map.setAttributes(listvertices, listOf(road1, road2, road3), "")

        // Create a RushHour with the specified attributes
        val args = listOf(23, 12, 30, 0) // ID 23, Starting Tick 12, Duration 30
        val listRoadTypes = listOf(PrimaryRoadType.MAIN_STREET, PrimaryRoadType.COUNTY_ROAD)
        val weightFactor = 5
        val rushhour = factory.createRushHour(args, listRoadTypes, weightFactor)

        // Trigger the event and verify that it was triggered successfully
        val result = rushhour.triggerEvent()
        assertTrue(result)
        assertEquals(7.5, road1.weight)
        assertEquals(50.0, road2.weight)
        assertEquals(10.0, road3.weight) // Make sure it didn't proc on the wrong type of road
        assertEquals(rushhour, road1.scheduledGlobalEvent)
        assertEquals(rushhour, road2.scheduledGlobalEvent)
        assertNotEquals(rushhour, road3.scheduledGlobalEvent)

        // Reverse the event and verify that it was reversed properly
        rushhour.reverseEvent()
        assertEquals(1.5, road1.weight)
        assertEquals(10.0, road2.weight)
        assertEquals(10.0, road3.weight)
        assertEquals(null, road1.scheduledGlobalEvent)
        assertEquals(null, road2.scheduledGlobalEvent)
        assertEquals(null, road3.scheduledGlobalEvent)
    }

    @Test
    fun testClosedRoadTriggerReverse() {
        // Create a ClosedRoad with the specified attributes
        val args = listOf(4, 5, 6, 7) // ID 4, Starting Tick 5, Duration 6
        val vertices = listOf(Vertex(3), Vertex(5))
        val closedRoad = factory.createClosedRoad(args, vertices)

        // Create the road it's going to happen on
        val road = Road(
            vertices[0],
            vertices[1],
            PrimaryRoadType.MAIN_STREET,
            SecondaryRoadType.NONE,
            "Road3",
            "Village3",
            1.0,
            1
        )

        // Add the road to the map so triggerEvent can find it
        Map.setAttributes(vertices, listOf(road), "")

        // Trigger the event and verify that it was triggered successfully
        val result = closedRoad.triggerEvent()
        assertTrue(result)
        assertEquals(Double.MAX_VALUE, road.weight)
        assertEquals(closedRoad, road.event)

        // Reverse the event and verify that it was reversed properly
        closedRoad.reverseEvent()
        assertEquals(1.0, road.weight)
        assertNotEquals(closedRoad, road.event)
    }

    /*
    @Test
    fun testVehicleUnavailableTrigger() {
        // Make the vehicle we're going to destroy first
        val vehiclemedicfac = VehicleMedicalFactory()
        val vehicleargs = VehicleIntParameters(0, 0, 4, 3) // ID 0, Base ID , Staff cap 4, Vehicle Height 3
        val veh1 = vehiclemedicfac.createAmbulance(vehicleargs)

        // Then the event itself
        val eventargs = listOf(4, 0, 2, 7) // ID 4, Starting Tick 0, Duration 2
        val vehid = 0
        val event1 = factory.createVehicleUnavailable(eventargs, vehid)

        // Add both and make an ECC
        val ecc = ECC(listOf(event1), listOf(veh1))

        var currentTick = 0
        // Trigger event1
        ecc.receiveNewEvents(currentTick)
        // But make sure it's not in ongoingevents, as it doesn't affect rerouting +
        // The vehicle takes care of it
        assertFalse(ecc.ongoingEvents.contains(event1))
        // Yes, I know this is outside the scope for this class
        // But I need this as preamble anyway to check if the vehicle is unavailable, look\

        // Check if the vehicle is scheduled to be unavailable for 2 ticks
        assertEquals(eventargs[2], veh1.unavailableFor)

        currentTick = 3
        ecc.endEvents(currentTick)
        ecc.receiveNewEvents(currentTick)
    }
    */
}

class EventFactoryDeterministicLogicTests {

    companion object {
        lateinit var factory: EventFactory
        lateinit var veh1: Vehicle
        lateinit var listofroads: List<Road>
        lateinit var listofvertices: List<Vertex>

        /**
         * Pre-initialize the factory so that all the subsequent tests can use it
         */
        @BeforeAll
        @JvmStatic
        fun setUp() {
            factory = EventFactory()

            // Make a vehicle we can use to create the ECC
            val vehicletoadd = VehiclePoliceFactory()
            val vehicleargs = VehicleIntParameters(0, 0, 2, 3) // ID 0, Base ID , Staff cap 2, Vehicle Height 3
            val criminalcap = 2
            veh1 = vehicletoadd.createPoliceCar(vehicleargs, criminalcap)

            // Create a list of vertices on the Map and 2 connected roads
            // This is put here because A - it's long and
            // B - Lets me reuse the roads for tests without worrying about side effects from triggering
            listofvertices = listOf(Vertex(1), Vertex(2), Vertex(10), Vertex(20))

            val road1 = Road( // Make a road going from 1 to 2
                listofvertices[0],
                listofvertices[1],
                PrimaryRoadType.MAIN_STREET,
                SecondaryRoadType.NONE,
                "Road1",
                "Village2",
                1.0,
                3
            )
            val road2 = Road( // Make a road going from 10 to 20
                listofvertices[2],
                listofvertices[3],
                PrimaryRoadType.SIDE_STREET,
                SecondaryRoadType.ONE_WAY_ROAD,
                "Road2",
                "",
                1.0,
                3
            )
            listofroads = listOf(road1, road2)
        }
    }

    /**
     * Check if a construction site procs if there's already a global
     * event on the road. (It shouldn't)
     */
    @Test
    fun testRushHourProcrastinationForConstructionSite() {
        val road1 = Road(
            listofvertices[0],
            listofvertices[1],
            PrimaryRoadType.MAIN_STREET,
            SecondaryRoadType.NONE,
            "Road1",
            "Village2",
            1.0,
            3
        )
        val road2 = Road(
            listofvertices[2],
            listofvertices[3],
            PrimaryRoadType.SIDE_STREET,
            SecondaryRoadType.ONE_WAY_ROAD,
            "Road2",
            "",
            1.0,
            3
        )
        listofroads = listOf(road1, road2)
        // Add the road to the map so triggerEvent can find it
        Map.setAttributes(listofvertices, listofroads, "")

        // Create a RushHour with the specified attributes
        var eventargs = listOf(15, 0, 2, 0) // ID 15, Starting Tick 0, Duration 2
        val listRoadTypes = listOf(PrimaryRoadType.MAIN_STREET, PrimaryRoadType.COUNTY_ROAD)
        var weightFactor = 5
        val rushhour = factory.createRushHour(eventargs, listRoadTypes, weightFactor)

        // Create a ConstructionSite with the specified attributes
        eventargs = listOf(3, 1, 3, 6) // ID 3, Starting Tick 1, Duration 3
        val oneWay = true
        weightFactor = 3
        val constructionsite = factory.createConstructionSite(eventargs, listofvertices, oneWay, weightFactor)

        // Add events + vehicles and make an ECC
        val ecc = ECC(listOf(rushhour, constructionsite), listOf(veh1))

        var currentTick = 0
        // Trigger event1, tick 0
        ecc.receiveNewEvents(currentTick)
        // and make sure it's actually triggered and is there
        // just for detekt comment out the following line
        assertTrue(ecc.ongoingEvents.contains(rushhour))
        assertEquals(5.0, listofroads[0].weight) // Triggered
        assertEquals(1.0, listofroads[1].weight) // Didn't trigger, since wrong type of road
        assertEquals(rushhour, listofroads[0].scheduledGlobalEvent)
        assertNotEquals(rushhour, listofroads[1].scheduledGlobalEvent)

        currentTick++
        // Attempt to trigger the second event on tick 1
        ecc.receiveNewEvents(currentTick)
        // And it should return false, since there's a global event already!
        assertFalse(ecc.ongoingEvents.contains(constructionsite))
        // But the rush hour is still active, so it *should* be in the ongoing events list.
        assertTrue(ecc.ongoingEvents.contains(rushhour))

        currentTick++ // Tick 2
        ecc.endEvents(currentTick)
        ecc.receiveNewEvents(currentTick)
        assertFalse(ecc.ongoingEvents.contains(rushhour)) // this should have ended!
        assertTrue(ecc.ongoingEvents.contains(constructionsite)) // but this should have started
        // because rush hour is gone now!
    }

    @Test
    fun testTrafficJamProcrastinationForRushHour() {
        val road1 = Road(
            listofvertices[0],
            listofvertices[1],
            PrimaryRoadType.MAIN_STREET,
            SecondaryRoadType.NONE,
            "Road1",
            "Village2",
            1.0,
            3
        )
        val road2 = Road(
            listofvertices[2],
            listofvertices[3],
            PrimaryRoadType.SIDE_STREET,
            SecondaryRoadType.ONE_WAY_ROAD,
            "Road2",
            "",
            1.0,
            3
        )
        listofroads = listOf(road1, road2)
        // Add the road to the map so triggerEvent can find it
        Map.setAttributes(listofvertices, listofroads, "")

        // Create a RushHour with the specified attributes
        var eventargs = listOf(15, 0, 2, 0) // ID 15, Starting Tick 0, Duration 2
        val listRoadTypes = listOf(PrimaryRoadType.MAIN_STREET, PrimaryRoadType.COUNTY_ROAD)
        var weightFactor = 5
        val rushhour = factory.createRushHour(eventargs, listRoadTypes, weightFactor)

        // Create a ConstructionSite with the specified attributes
        eventargs = listOf(3, 1, 3, 6) // ID 3, Starting Tick 1, Duration 3
        weightFactor = 3
        val trafficjam2 = factory.createTrafficJam(eventargs, listofvertices, weightFactor)

        // Add events + vehicles and make an ECC
        val ecc = ECC(listOf(rushhour, trafficjam2), listOf(veh1))

        var currentTick = 0
        // Trigger event1, tick 0
        ecc.receiveNewEvents(currentTick)
        // and make sure it's actually triggered and is there
        assertTrue(ecc.ongoingEvents.contains(rushhour))
        assertEquals(5.0, listofroads[0].weight) // Triggered
        assertEquals(1.0, listofroads[1].weight) // Didn't trigger, since wrong type of road
        assertEquals(rushhour, listofroads[0].scheduledGlobalEvent)
        assertNotEquals(rushhour, listofroads[1].scheduledGlobalEvent)

        currentTick++
        // Attempt to trigger the second event on tick 1
        ecc.receiveNewEvents(currentTick)
        // And it should return false, since there's a global event already!
        assertFalse(ecc.ongoingEvents.contains(trafficjam2))
        // But the rush hour is still active, so it *should* be in the ongoing events list.
        assertTrue(ecc.ongoingEvents.contains(rushhour))

        currentTick++ // Tick 2
        ecc.endEvents(currentTick)
        ecc.receiveNewEvents(currentTick)
        assertFalse(ecc.ongoingEvents.contains(rushhour)) // this should have ended!
        assertTrue(ecc.ongoingEvents.contains(trafficjam2)) // but this should have started
        // because rush hour is gone now!
    }

    // Todo for me : Make a system test such that there's an emergency, but
    // the road has been closed in a prior tick. This is so that we can test
    // the delayRoadClosing behaviour and coverage.
    // todo : an emergency, it opens but an event tries getting in and then a global event on the
    // next tick. the global event should NOT take precedence.
    // Todo : idk just think of more tests
}
