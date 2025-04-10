package borges.gustavo.taskflow.activity


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
import borges.gustavo.taskflow.databinding.ActivityCreateTaskBinding
import borges.gustavo.taskflow.model.Task
import borges.gustavo.taskflow.utils.Util
import java.util.Calendar
import java.util.TimeZone

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

            // APRIMORAR: SALVAR TAREFA EM MEMÓRIA OU BANCO DE DADOS

            val dateTimeFormatted = selectedDate?.let { Util.formatCalendarDateTime(it) }

            val newTask = Task(
                title = title,
                description = description,
                priority = priority,
                dateTime = dateTimeFormatted
            )

            Toast.makeText(this, "Tarefa criada: $title ($priority)", Toast.LENGTH_SHORT).show()

            // Retornar a tarefa criada para a MainActivity
            val resultIntent = Intent().apply {
                putExtra("new_task", newTask)
                Log.d("RESULT_INTENT", "putExtra executado")
            }
            setResult(Activity.RESULT_OK, resultIntent)
            Log.d("SET_RESULT", "setResult executado")
            finish()
        }

        binding.buttonBack.setOnClickListener {
            finish() // Fecha a activity e volta pra anterior
        }
    }
}