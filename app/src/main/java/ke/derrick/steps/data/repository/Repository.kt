package ke.derrick.steps.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import ke.derrick.steps.DaysOfTheWeek
import ke.derrick.steps.WorkoutStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class Repository(private val dataStore: DataStore<Preferences>) {
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

}