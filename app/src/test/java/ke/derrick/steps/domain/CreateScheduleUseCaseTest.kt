package ke.derrick.steps.domain

import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class CreateScheduleUseCaseTest {
    private val tomorrow = 1L
    private val time: LocalTime = LocalTime.of(6, 30)
    private val expectedValue =
        LocalDate.now().plusDays(tomorrow).toString().plus("T").plus(time)

    private val createWorkoutReminderUseCase = CreateWorkoutReminderUseCase()


//    @Test
//    fun `CreateSchedule should return the correct datetime string` () {
//        val result = createWorkoutReminderUseCase(3, 6, 30)
//        assert(
//            result == expectedValue
//        ) {
//            "expected: $expectedValue, got $result"
//        }
//    }
}