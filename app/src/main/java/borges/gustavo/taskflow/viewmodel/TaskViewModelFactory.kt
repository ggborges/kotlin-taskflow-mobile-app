package borges.gustavo.taskflow.viewmodel

import TaskViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import borges.gustavo.taskflow.database.dao.TaskDao

class TaskViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TaskViewModel::class.java) -> {
                TaskViewModel(taskDao) as T  // Aqui garantimos que o tipo T é um TaskViewModel
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}