package borges.gustavo.taskflow.model

import borges.gustavo.taskflow.database.entity.TaskEntity
import borges.gustavo.taskflow.utils.Util.parseDateTime


data class Task (
    val id: Int? = null, // Inicializa vazio e gera após a criação do TaskEntity
    val title: String,
    val description: String,
    val priority: String,
    val dateTime: String? = null, // Pode ser null se a tarefa não tiver data
    var isCompleted: Boolean = false
)  : java.io.Serializable {

    fun toEntity(): TaskEntity {
        return TaskEntity(
            id = this.id ?: 0, // Room ignora se for 0
            title = this.title,
            description = this.description,
            priority = this.priority,
            scheduledAt = parseDateTime(this.dateTime),
            isCompleted = this.isCompleted,// Conversão de String para Long (timestamp)
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }

}