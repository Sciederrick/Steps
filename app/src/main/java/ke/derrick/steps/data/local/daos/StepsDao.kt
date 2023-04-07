package ke.derrick.steps.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ke.derrick.steps.data.local.entities.Steps

@Dao
interface StepsDao {
    @Insert
    fun insert(steps: Steps)

    @Query("SELECT * FROM `steps` LIMIT 1")
    fun getLastStepCount(): Steps?

    @Update
    fun update(steps: Steps)

}