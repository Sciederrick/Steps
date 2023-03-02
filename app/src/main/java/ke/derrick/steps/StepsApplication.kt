package ke.derrick.steps

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class StepsApplication: Application() {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "schedule")
}