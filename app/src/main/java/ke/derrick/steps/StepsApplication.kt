package ke.derrick.steps

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import ke.derrick.steps.data.local.database.AppDatabase
import ke.derrick.steps.data.repository.Repository
import ke.derrick.steps.utils.NotifUtils

class StepsApplication: Application() {
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "steps")
    private val stepsDatabase by lazy { AppDatabase.getInstance(this) }
    val repo by lazy { Repository(dataStore = dataStore, stepsDao = stepsDatabase.Steps())}
    private lateinit var notification: NotifUtils

    override fun onCreate() {
        super.onCreate()
        notification = NotifUtils(this)
        notification.createNotificationChannels()
    }
}