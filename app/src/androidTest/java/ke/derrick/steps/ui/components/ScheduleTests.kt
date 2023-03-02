package ke.derrick.steps.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import ke.derrick.steps.DaysOfTheWeek
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ScheduleTests {
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var weekDays: Array<DaysOfTheWeek>

    @Before
    fun setup() {
        weekDays = DaysOfTheWeek.values()
        composeTestRule.setContent {
            val dayWithWorkoutStatus by rememberSaveable{ mutableStateOf(hashMapOf<Int, Int>()) }
            dayWithWorkoutStatus[0] = 0; dayWithWorkoutStatus[1] = 1; dayWithWorkoutStatus[2] = 0; dayWithWorkoutStatus[3] = 1;
            dayWithWorkoutStatus[4] = 2; dayWithWorkoutStatus[5] = 3
            Schedule(dayWithWorkoutStatus)
        }
    }

    @Test
    fun scheduleCardComponent_hasText_withCorrectShortDayOfTheWeek() {
        weekDays.forEachIndexed { index, day ->
            composeTestRule.onAllNodes(hasTestTag("ScheduleCard"))[index].assert(hasParent(
                hasAnyChild(hasText(day.shortName))
            ))
        }

    }

    @Test
    fun scheduleCardComponent_hasIcon() {
        weekDays.forEachIndexed { index, _ ->
            composeTestRule.onAllNodes(hasTestTag("ScheduleCard"))[index].assert(hasParent(
                hasAnyChild(hasContentDescription("exercise icon", substring = true, ignoreCase = true))
            ))
        }

    }
}