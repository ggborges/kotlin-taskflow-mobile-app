package borges.gustavo.taskflow.activity

import TaskViewModel
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import borges.gustavo.taskflow.R
import borges.gustavo.taskflow.adapter.TaskAdapter
import borges.gustavo.taskflow.database.db.RoomDB
import borges.gustavo.taskflow.databinding.ActivityTaskListBinding
import borges.gustavo.taskflow.viewmodel.TaskViewModelFactory
import com.google.firebase.auth.FirebaseAuth


class TaskListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskViewModel: TaskViewModel

    // private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = FirebaseAuth.getInstance().currentUser?.displayName
        binding.welcomeTextView.text = getString(R.string.welcome_user, userName)

        // ViewModel com o singleton do banco de dados
        val db = RoomDB.getDatabase(applicationContext)
        val taskDao = db.taskDao()
        val factory = TaskViewModelFactory(taskDao)
        taskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        // Inicializa RecyclerView
        taskAdapter = TaskAdapter(this, taskViewModel)
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = taskAdapter

        // Observa o LiveData para atualizações na lista de tarefas
        taskViewModel.allTasks.observe(this, Observer { tasks ->
            taskAdapter.submitList(tasks) // Atualiza a lista do RecyclerView
        })

        /*
        Pegando Task através de retorno de Intent (deprecated)
        val launcher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getSerializableExtra("new_task", Task::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    result.data?.getSerializableExtra("new_task") as? Task
                }
                task?.let {
                    taskList.add(it)
                    taskAdapter.notifyItemInserted(taskList.size - 1)
                }
            }
        }
         */

        // FAB para criar nova tarefa
        binding.fabAddTask.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
        }

        /*
        Hardcode de Task na Lista
        taskList.add(
            Task(
                title = "Estudar Android",
                description = "Terminar lista de tarefas com RecyclerView",
                priority = "Alta",
                dateTime = "10/04/2025 às 15:00"
            )
        )
        taskAdapter.notifyItemInserted(taskList.size - 1)
         */
    }

}