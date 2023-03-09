package ke.derrick.steps

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import ke.derrick.steps.data.repository.Repository

class StepsApplication: Application() {
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "steps")
    val repo by lazy { Repository(dataStore = dataStore) }
}