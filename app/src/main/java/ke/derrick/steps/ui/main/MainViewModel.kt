package ke.derrick.steps.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import ke.derrick.steps.StepsApplication
import ke.derrick.steps.data.local.entities.Steps
import ke.derrick.steps.data.repository.Repository
import ke.derrick.steps.domain.CreateWorkoutReminderUseCase
import ke.derrick.steps.util.convertToTwoDigitNumberString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

class MainViewModel(private val repo: Repository,
                    private val createWorkoutReminder: CreateWorkoutReminderUseCase): ViewModel() {
    val fakeListOfSteps: ArrayList<Steps> = List(350) { Steps(0, 0L, "00", "", "") } as ArrayList<Steps>
    init {
//        viewModelScope.launch {
//            getInitialStepCountAsync()
//        }
        setStepCountAsyncFake()
    }

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

    private fun setStepCountAsyncFake() {
        val mNow = LocalDateTime.now()
        val mToday = LocalDate.now()
        for (i in 0 until 350) {
            fakeListOfSteps[i] =
                Steps(i.toLong(), Random.nextLong(0, 10000), convertToTwoDigitNumberString(mToday.plusDays(
                    i.toLong()
                ).dayOfMonth),
                    mNow.plusDays(i.toLong()).toString(), mNow.plusDays(i.toLong()).toString())
        }
    }

    fun getStepCountFakeAsync(start: Long, limit: Int) = viewModelScope.async(Dispatchers.IO) {
        delay(2000L)
        fakeListOfSteps.subList(start.toInt(), (start + limit).toInt())
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