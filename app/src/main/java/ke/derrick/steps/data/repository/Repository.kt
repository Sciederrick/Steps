package ke.derrick.steps.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ke.derrick.steps.ADD_DEFAULT_STEP_COUNT_WORK_NAME
import ke.derrick.steps.DaysOfTheWeek
import ke.derrick.steps.STARTING_STEP_COUNT_KEY
import ke.derrick.steps.WorkoutStatus
import ke.derrick.steps.data.local.daos.StepsDao
import ke.derrick.steps.data.local.entities.Steps
import ke.derrick.steps.worker.AddDefaultStepCountWorker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class Repository(private val workManager: WorkManager,
                 private val dataStore: DataStore<Preferences>, private val stepsDao: StepsDao) {
    init {
        addDefaultStepsValueToDB()
    }

    suspend fun getSevenDayWorkoutStatus(): Array<Int> {
        val workoutStatusArr = IntArray(7)
        for (day in DaysOfTheWeek.values()) {
            val key = intPreferencesKey(day.ordinal.toString())
            workoutStatusArr[day.ordinal] = dataStore.data
                .map { preferences -> preferences[key] ?: WorkoutStatus.MISSED.ordinal }.first()
        }
        return workoutStatusArr.toTypedArray()
    }

    suspend fun persistSchedule(dayOfTheWeekOrdinal: Int) {
        val weeklyScheduleKey = intPreferencesKey(dayOfTheWeekOrdinal.toString())
        dataStore.edit { store ->  store[weeklyScheduleKey] = WorkoutStatus.SCHEDULED.ordinal }
    }

    fun createSteps(id: Long = 0, count: Long, day: String) :Steps {
        return Steps(id = id, count = count, day = day, createdAt = LocalDateTime.now().toString(),
            updatedAt = LocalDateTime.now().toString())
    }

    fun insertStepCount(steps: Steps) = stepsDao.insert(steps)


    fun getLastStepCount(): Steps? {
        return stepsDao.getLastStepCount()
    }

    fun getStepCount(today: String): Steps? {
        return stepsDao.getStepCount(today)
    }

    fun getStepCount(start: Long, limit: Int): List<Steps>? {
        return stepsDao.getStepCount(start, limit)
    }

    fun getMaxStepCount(): Long? {
        return stepsDao.getMaxStepCount()
    }

    fun updateStepCount(steps: Steps) = stepsDao.update(steps)

    // Figures for the step counter sensor are cumulative, I therefore need to store the last figure
    // recorded inorder to calculate the difference and get the periodic step count that is then
    // displayed on the UI
    suspend fun getInitialStepCount() :Long? {
        val key = longPreferencesKey(STARTING_STEP_COUNT_KEY)
        return dataStore.data.map { data -> data[key] }.first()
    }

    suspend fun persistInitialStepCount(mValue: Long) { // Will apply in the next session
        val key = longPreferencesKey(STARTING_STEP_COUNT_KEY)
        dataStore.edit { data -> data[key] = mValue }
    }

    private fun addDefaultStepsValueToDB() {
        val addDefaultStepCountWorkRequest =
            PeriodicWorkRequestBuilder<AddDefaultStepCountWorker>(
                repeatInterval = 24,
                repeatIntervalTimeUnit = TimeUnit.HOURS
            ).build()

        workManager.enqueueUniquePeriodicWork(
            ADD_DEFAULT_STEP_COUNT_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            addDefaultStepCountWorkRequest)
    }

}