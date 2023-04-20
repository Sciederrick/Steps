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

    @Query("SELECT * FROM `steps` WHERE `created_at` LIKE :today")
    fun getStepCount(today: String): Steps?

    @Query("SELECT * FROM `steps` WHERE id >= :start LIMIT :limit")
    fun getStepCount(start: Long, limit: Int): List<Steps>?

    @Query("SELECT MAX(`count`) FROM `steps`")
    fun getMaxStepCount(): Long?

    @Update
    fun update(steps: Steps)

}