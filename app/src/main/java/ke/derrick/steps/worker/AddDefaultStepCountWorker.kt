package ke.derrick.steps.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class AddDefaultStepCountWorker(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        TODO("create task")
    }
}