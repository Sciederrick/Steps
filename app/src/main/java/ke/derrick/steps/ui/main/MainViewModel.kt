package ke.derrick.steps.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import ke.derrick.steps.StepsApplication
import ke.derrick.steps.data.repository.Repository
import ke.derrick.steps.domain.CreateWorkoutReminderUseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(private val repo: Repository,
                    private val createWorkoutReminder: CreateWorkoutReminderUseCase): ViewModel() {
    fun createScheduleReminder(mContext: Context, dayOfTheWeek: Int, mHour: Int, mMinute: Int) = viewModelScope.launch {
        createWorkoutReminder(mContext, dayOfTheWeek, mHour, mMinute)
        repo.persistSchedule(dayOfTheWeek)
    }

    fun getSevenDayWorkoutStatusAsync(): Deferred<Array<Int>> = viewModelScope.async {
        repo.getSevenDayWorkoutStatus()
    }

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