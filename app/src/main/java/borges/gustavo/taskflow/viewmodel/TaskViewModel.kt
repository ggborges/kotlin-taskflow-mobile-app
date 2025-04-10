import androidx.lifecycle.*
import borges.gustavo.taskflow.database.dao.TaskDao
import borges.gustavo.taskflow.model.Task
import kotlinx.coroutines.launch
import androidx.lifecycle.map

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    // LiveData para armazenar e observar a lista de tarefas
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks().map { list ->
        list.map { it.toModel() }
    }

    /* Init (deprecated)
    init {
        // Carregar as tarefas em um coroutine scope
        viewModelScope.launch {
            _allTasks.value = taskDao.getAllTasks().map { it.toModel() }
        }
    }
    */

    // Função para inserir uma nova tarefa
    suspend fun insertTask(task: Task): Long {
        return taskDao.insert(task.toEntity())  // Convertendo Task para TaskEntity
    }

    // Função para atualizar uma tarefa
    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.update(task.toEntity())  // Convertendo Task para TaskEntity
        }
    }

    // Função para excluir uma tarefa
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task.toEntity())  // Convertendo Task para TaskEntity
        }
    }
}
