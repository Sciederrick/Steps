package ke.derrick.steps.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ke.derrick.steps.StepsApplication
import ke.derrick.steps.utils.convertToTwoDigitNumberString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class AddDefaultStepCountWorker(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val repo = (applicationContext as StepsApplication).repo
                val today = LocalDate.now().toString()
                val todaysStepCount = repo.getStepCount(today)
                if (todaysStepCount == null) {
                    val steps = repo.createSteps(
                        count = 0,
                        day = convertToTwoDigitNumberString(
                            LocalDate.parse(today).dayOfMonth
                        )
                    )
                    repo.insertStepCount(steps)
                }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }
}