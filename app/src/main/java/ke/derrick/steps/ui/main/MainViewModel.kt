package ke.derrick.steps.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import ke.derrick.steps.StepsApplication
import ke.derrick.steps.data.repository.Repository
import ke.derrick.steps.domain.CreateWorkoutReminderUseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(private val repo: Repository,
                    private val createWorkoutReminder: CreateWorkoutReminderUseCase): ViewModel() {
//    init {
//        viewModelScope.launch {
//            getInitialStepCountAsync()
//        }
//    }

    fun createScheduleReminder(mContext: Context, dayOfTheWeek: Int, mHour: Int, mMinute: Int)
        = viewModelScope.launch(Dispatchers.IO) {
        createWorkoutReminder(mContext, dayOfTheWeek, mHour, mMinute)
        repo.persistSchedule(dayOfTheWeek)
    }

    fun getSevenDayWorkoutStatusAsync() = viewModelScope.async(Dispatchers.IO) {
        repo.getSevenDayWorkoutStatus()
    }

    fun getStepCountAsync(start: Long, limit: Int) = viewModelScope.async(Dispatchers.IO) {
        repo.getStepCount(start, limit)
    }



//    private suspend fun getInitialStepCountAsync() {
//        val initialStepCount = viewModelScope.async {
//            repo.getInitialStepCount()
//        }.await()
//        Log.d(TAG, "initial step count: $initialStepCount")
//    }

    companion object {
        const val TAG = "MainViewModel"
        fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.AndroidViewModelFactory()  {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return MainViewModel((application as StepsApplication).repo, 
                    createWorkoutReminder = CreateWorkoutReminderUseCase()) as T
            }
        }
    }
}