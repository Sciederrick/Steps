package ke.derrick.steps.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ke.derrick.steps.data.local.daos.StepsDao
import ke.derrick.steps.data.local.entities.Steps

@Database(entities = [Steps::class], exportSchema = true, version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun Steps(): StepsDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "steps_database")
                .build()
        }

    }
}