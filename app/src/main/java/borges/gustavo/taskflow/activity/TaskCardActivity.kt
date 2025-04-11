package borges.gustavo.taskflow.activity

import TaskViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import borges.gustavo.taskflow.R
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
                binding.taskTitle.text = task.title
                binding.taskDescription.text = task.description
                binding.taskPriority.text = task.priority
                binding.taskDateTime.text = task.dateTime

                // Marcar checkbox se a tarefa estiver concluída
                binding.checkboxCompleted.isChecked = task.isCompleted

                // Atualizar cor do CardView com base no status da tarefa
                val cardColor = if (task.isCompleted) {
                    getColor(R.color.green_light) // ou ContextCompat.getColor(this, R.color.light_green)
                } else {
                    getColor(android.R.color.white)
                }
                binding.cardTask.setCardBackgroundColor(cardColor)

                // Listener para atualização de conclusão
                binding.checkboxCompleted.setOnCheckedChangeListener { _, isChecked ->
                    task.isCompleted = isChecked
                    task.id?.let { taskViewModel.updateTaskCompletion(it, isChecked) }

                    if (isChecked) {
                        binding.cardTask.setCardBackgroundColor(getColor(R.color.green_light))
                    } else {
                        binding.cardTask.setCardBackgroundColor(getColor(android.R.color.white))
                    }
                }
            })

            binding.buttonBack.setOnClickListener {
                finish()
            }
        }
    }
}

