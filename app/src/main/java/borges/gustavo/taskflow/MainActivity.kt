package borges.gustavo.taskflow

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import borges.gustavo.taskflow.activity.CreateTaskActivity
import borges.gustavo.taskflow.adapter.TaskAdapter
import borges.gustavo.taskflow.databinding.ActivityMainBinding
import borges.gustavo.taskflow.model.Task

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa RecyclerView
        taskAdapter = TaskAdapter(this, taskList)
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = taskAdapter

        // FAB para criar nova tarefa
        binding.fabAddTask.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
        }

        // (Exemplo) Adicionando tarefa fake para teste
        taskList.add(
            Task(
                title = "Estudar Android",
                description = "Terminar lista de tarefas com RecyclerView",
                priority = "Alta",
                dateTime = "10/04/2025 às 15:00"
            )
        )
        taskAdapter.notifyItemInserted(taskList.size - 1)
    }
}