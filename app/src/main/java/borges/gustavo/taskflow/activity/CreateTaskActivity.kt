package borges.gustavo.taskflow.activity


import TaskViewModel
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import borges.gustavo.taskflow.databinding.ActivityCreateTaskBinding
import borges.gustavo.taskflow.model.Task
import borges.gustavo.taskflow.utils.Util
import java.util.Calendar
import java.util.TimeZone
import borges.gustavo.taskflow.database.db.RoomDB
import borges.gustavo.taskflow.viewmodel.TaskViewModelFactory
import kotlinx.coroutines.launch

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding
    private var selectedDate: Calendar? = null // Armazena data/hora completa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurando Spinner de prioridades
        val priorities = listOf("Alta", "Média", "Baixa")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = adapter

        // Botão de selecionar data
        binding.btnSelectDate.setOnClickListener {
            val today = Calendar.getInstance()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day = today.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->
                if (selectedDate == null) selectedDate = Calendar.getInstance()
                selectedDate!!.set(Calendar.YEAR, y)
                selectedDate!!.set(Calendar.MONTH, m)
                selectedDate!!.set(Calendar.DAY_OF_MONTH, d)
                Toast.makeText(this, "Data: $d/${m + 1}/$y", Toast.LENGTH_SHORT).show()
            }, year, month, day).show()
        }

        // Botão de selecionar hora
        binding.btnSelectTime.setOnClickListener {
            val now = Calendar.getInstance()
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val minute = now.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, h, m ->
                if (selectedDate == null) selectedDate = Calendar.getInstance()
                selectedDate!!.set(Calendar.HOUR_OF_DAY, h)
                selectedDate!!.set(Calendar.MINUTE, m)
                selectedDate!!.set(Calendar.SECOND, 0)
                Toast.makeText(this, "Hora: %02d:%02d".format(h, m), Toast.LENGTH_SHORT).show()
            }, hour, minute, true).show()
        }

        // Criar tarefa
        binding.btnCreateTsk.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            val priority = binding.spinnerPriority.selectedItem.toString()

            // Se a data/hora foi selecionada, criar evento no calendário
            selectedDate?.let { calendar ->
                val startMillis = calendar.timeInMillis
                val endMillis = startMillis + 60 * 60 * 1000 // Evento de 1 hora

                val calendarIntent = Intent(Intent.ACTION_EDIT).apply {
                    type = "vnd.android.cursor.item/event"
                    putExtra(CalendarContract.Events.TITLE, title)
                    putExtra(CalendarContract.Events.DESCRIPTION, description)
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                    putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                }

                try {
                    startActivity(calendarIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "Nenhum app de calendário disponível", Toast.LENGTH_SHORT).show()
                }
                /*val resolvedActivity = calendarIntent.resolveActivity(packageManager)
                Log.d("CALENDAR_INTENT", "Resolved activity: $resolvedActivity")

                if (resolvedActivity != null) {
                    startActivity(calendarIntent)
                } else {
                    // Fallback: tentar abrir diretamente o app do Google Calendar
                    val fallbackIntent = packageManager.getLaunchIntentForPackage("com.google.android.calendar")
                    if (fallbackIntent != null) {
                        startActivity(fallbackIntent)
                    } else {
                        Toast.makeText(this, "Nenhum app de calendário disponível", Toast.LENGTH_SHORT).show()
                    }
                }*/
            }

            val dateTimeFormatted = selectedDate?.let { Util.formatCalendarDateTime(it) }

            val newTask = Task(
                title = title,
                description = description,
                priority = priority,
                dateTime = dateTimeFormatted
            )

            // Acessando instância do Singleton, salvando nova Task no DB e enviando a Task com ID para a MainActivity (lista de tarefas)
            val db = RoomDB.getDatabase(applicationContext)
            val taskDao = db.taskDao()
            val factory = TaskViewModelFactory(taskDao)
            val viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java] // substitui o get por index

            lifecycleScope.launch {
                val id = viewModel.insertTask(newTask)
                Log.d("TASK_ID", "ID gerado pelo Room: $id")
                val taskWithId = newTask.copy(id = id.toInt())
                /*
                Retornar Task para a MainActivity (deprecated)
                val resultIntent = Intent().apply {
                    putExtra("new_task", taskWithId)
                    Log.d("RESULT_INTENT", "putExtra executado")
                }
                setResult(Activity.RESULT_OK, resultIntent)
                Log.d("SET_RESULT", "setResult executado")
                 */
                finish()
            }

            Toast.makeText(this, "Tarefa criada: $title ($priority)", Toast.LENGTH_SHORT).show()
            /* Retornar a tarefa criada para a MainActivity (deprecated)
            val resultIntent = Intent().apply {
                putExtra("new_task", taskWithId)
                Log.d("RESULT_INTENT", "putExtra executado")
            }
            setResult(Activity.RESULT_OK, resultIntent)
            Log.d("SET_RESULT", "setResult executado")
            finish()
             */
        }

        binding.buttonBack.setOnClickListener {
            finish() // Fecha a activity e volta pra anterior
        }
    }
}