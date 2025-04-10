package borges.gustavo.taskflow.model

import borges.gustavo.taskflow.database.entity.TaskEntity


data class Task (
    val title: String,
    val description: String,
    val priority: String,
    val dateTime: String? = null // Pode ser null se a tarefa não tiver data
)  : java.io.Serializable {

    fun toEntity(id: Int = 0): TaskEntity {
        return TaskEntity(
            id = id,
            title = this.title,
            description = this.description,
            priority = this.priority,
            scheduledAt = parseDateTime(this.dateTime), // Conversão de String para Long (timestamp)
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }

}