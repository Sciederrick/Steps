package ke.derrick.steps.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps")
data class Steps(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val count: Long,
    val day: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") var updatedAt: String
) {
    override fun toString(): String {
        return "steps: $count, day: $day"
    }
}