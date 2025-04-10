package borges.gustavo.taskflow.activity

import TaskViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import borges.gustavo.taskflow.database.db.RoomDB
import borges.gustavo.taskflow.databinding.ActivityTaskCardBinding
import borges.gustavo.taskflow.viewmodel.TaskViewModelFactory

class TaskCardActivity : AppCompatActivity() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var binding: ActivityTaskCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskId = intent.getIntExtra("taskId", -1)

        if (taskId != -1) {
            // Configurar o ViewModel
            val db = RoomDB.getDatabase(applicationContext)
            val taskDao = db.taskDao()
            val factory = TaskViewModelFactory(taskDao)
            taskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

            // Buscar a tarefa pelo ID
            taskViewModel.getTaskById(taskId).observe(this, Observer { task ->
                binding.txtTaskTitle.text = task.title
                binding.txtTaskDescription.text = task.description
                binding.txtTaskPriority.text = task.priority
                binding.txtTaskDateTime.text = task.dateTime
            })
        }
    }
}

