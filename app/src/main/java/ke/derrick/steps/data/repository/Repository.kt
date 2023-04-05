package ke.derrick.steps.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import ke.derrick.steps.DaysOfTheWeek
import ke.derrick.steps.WorkoutStatus
import ke.derrick.steps.data.local.daos.StepsDao
import ke.derrick.steps.data.local.entities.Steps
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class Repository(private val dataStore: DataStore<Preferences>, private val stepsDao: StepsDao) {
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

    fun getTodaysStepCount(mDate: String): Steps? {
        return stepsDao.getTodaysStepCount(mDate)
    }

    fun updateStepCount(steps: Steps) = stepsDao.update(steps)
}