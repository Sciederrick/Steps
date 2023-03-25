package ke.derrick.steps

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import ke.derrick.steps.data.repository.Repository
import ke.derrick.steps.utils.createNotificationChannels

class StepsApplication: Application() {
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "steps")
    val repo by lazy { Repository(dataStore = dataStore) }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels(this)
    }
}