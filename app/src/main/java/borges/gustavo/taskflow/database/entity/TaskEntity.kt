package borges.gustavo.taskflow.database.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import borges.gustavo.taskflow.model.Task
import borges.gustavo.taskflow.utils.Util.formatDateTime

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "priority")
    val priority: String,

    @ColumnInfo(name = "scheduled_at")
    val scheduledAt: Long?, // Timestamp em Long

    @ColumnInfo(name = "isCompleted")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
) {

    fun toModel(): Task {
        return Task(
            id = this.id,
            title = this.title,
            description = this.description,
            priority = this.priority,
            dateTime = formatDateTime(this.scheduledAt), // Conversão de Long (timestamp) para String
            isCompleted = this.isCompleted
        )
    }

}
