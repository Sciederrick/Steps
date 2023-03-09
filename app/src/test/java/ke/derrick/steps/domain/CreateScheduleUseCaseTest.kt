package ke.derrick.steps.domain

import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime

class CreateScheduleUseCaseTest {
    private val tomorrow = 1L
    private val time: LocalTime = LocalTime.of(6, 30)
    private val expectedValue =
        LocalDate.now().plusDays(tomorrow).toString().plus("T").plus(time)

    private val createScheduleUseCase = CreateScheduleUseCase()


    @Test
    fun `CreateSchedule should return the correct datetime string` () {
        val result = createScheduleUseCase(3, 6, 30)
        assert(
            result == expectedValue
        ) {
            "expected: $expectedValue, got $result"
        }
    }
}