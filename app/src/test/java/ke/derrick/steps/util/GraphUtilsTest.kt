package ke.derrick.steps.util

import junit.framework.Assert.assertEquals
import ke.derrick.steps.data.local.entities.Steps
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class GraphUtilsTest {
    private lateinit var expectedResultA: List<Triple<Float, String, Long>>
    private lateinit var expectedResultB: List<Triple<Float, String, Long>>
    private lateinit var stepsList: List<Steps>
    @Before
    fun setup() {
        expectedResultA = listOf(Triple(0F, "01", 0L), Triple(0F, "02", 0L), Triple(0F, "03", 0L),
            Triple(0F, "04", 0L), Triple(0F, "05", 0L), Triple(0F, "06", 0L), Triple(0F, "07", 0L))

        expectedResultB = listOf(Triple(0F, "11", 0L), Triple(0F, "12", 0L), Triple(0F, "13", 0L),
            Triple(0F, "14", 0L), Triple(0F, "15", 0L), Triple(500F, "16", 0L), Triple(500F, "17", 0L))

        stepsList = listOf(
            Steps(0L, 500L,
                convertToTwoDigitNumberString(LocalDateTime.parse("2023-04-17T17:24:36.270986").minusDays(1).dayOfMonth),
                LocalDateTime.parse("2023-04-17T17:24:36.270986").minusDays(1).toString(),
                LocalDateTime.parse("2023-04-17T17:24:36.270986").minusDays(1).toString()),
            Steps(0L, 500L,
                convertToTwoDigitNumberString(LocalDateTime.parse("2023-04-17T17:24:36.270986").dayOfMonth),
                LocalDateTime.parse("2023-04-17T17:24:36.270986").toString(),
                LocalDateTime.parse("2023-04-17T17:24:36.270986").toString()))
    }

    @Test
    fun `function fillNullArray returns the correct output` () {
        val result = fillWithDefaultPoints(7)
        assertEquals(result, expectedResultA)
    }

    @Test
    fun `function fillGapPoints returns the correct output` () {
        val result = fillGapPoints(7, stepsList)
        assertEquals(result, expectedResultB)
    }
}