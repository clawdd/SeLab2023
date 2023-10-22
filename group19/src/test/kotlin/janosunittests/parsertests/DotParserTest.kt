package janosunittests.parsertests

import de.unisaarland.cs.se.selab.parser.DotParser
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DotParserTest {

    @Test
    fun testMapParsing() {
        val dotParser = DotParser("src/systemtest/resources/mapFiles/example_map.dot")
        assertTrue { dotParser.parseMap() }
    }

    @Test
    fun testMapParsingWithUnderscore() {
        val dotParser = DotParser("src/systemtest/resources/mapFiles/navigationTestMaps/example_with_underscore.dot")
        assertTrue { dotParser.parseMap() }
    }

    @Test
    fun testMapParsingHamburgMap() {
        val dotParser = DotParser("src/systemtest/resources/mapFiles/Hamburg.dot")
        assertTrue { dotParser.parseMap() }
    }

    @Test
    fun testMapParsingBerlinFireMap() {
        val dotParser = DotParser("src/systemtest/resources/mapFiles/berlin_fire_map.dot")
        assertTrue { dotParser.parseMap() }
    }

    @Test
    fun testNegativeVertexID() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/negative_vertices.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testNegativeRoadWeight() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/negative_road_weight.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testNegativeRoadHeight() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/negative_road_height.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testRoadHeightBelowOne() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/road_height_below_one.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testTunnelLargerThree() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/tunnel_larger_three.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testNewRoadSameVertices() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/new_road_between_two_verties.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testUnconnectedVertex() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/unconnected_vertex.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testRoadnameNotUnique() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/road_name_in_village_not_unique.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testVertexToItselft() {
        val dotParser = DotParser("src/systemtest/resources/mapFiles/invalidMaps/vertex_to_itself.dot")
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testNoMainStreetInVillage() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/no_main_street_in_village.dot"
        )
        val a = dotParser.parseMap()
        assertFalse { a }
    }

    @Test
    fun testNoSideStreetOnMap() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/no_side_street_on_map.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testCountyRoadNameNotCountyName() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/county_road_name_not_county_name.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testDirectionConnectionOtherVillage() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/direct_connection_other_village.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testTextAfterLastBracket() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/text_after_last_bracket.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testZeroBeforeNumber() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/zero_before_number.dot"
        )
        assertFalse { dotParser.parseMap() }
    }

    @Test
    fun testZeroBeforeNumberRoads() {
        val dotParser = DotParser(
            "src/systemtest/resources/mapFiles/invalidMaps/zero_before_number_roads.dot"
        )
        assertFalse { dotParser.parseMap() }
    }
}
