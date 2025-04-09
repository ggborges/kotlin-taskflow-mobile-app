package borges.gustavo.taskflow.model


data class Task(
    val title: String,
    val description: String,
    val priority: String,
    val dateTime: String? = null // Pode ser null se a tarefa não tiver data
)