package borges.gustavo.taskflow.activity


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import borges.gustavo.taskflow.databinding.ActivityCreateTaskBinding
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

            // APRIMORAR: SALVAR TAREFA OU USO DE INTENT PARA OUTRA TELA

            // Se a data/hora foi selecionada, criar evento no calendário
            selectedDate?.let { calendar ->
                val startMillis = calendar.timeInMillis
                val endMillis = startMillis + 60 * 60 * 1000 // Evento de 1 hora

                val intent = Intent(Intent.ACTION_INSERT).apply {
                    data = CalendarContract.Events.CONTENT_URI
                    putExtra(CalendarContract.Events.TITLE, title)
                    putExtra(CalendarContract.Events.DESCRIPTION, description)
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                    putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Nenhum app de calendário disponível", Toast.LENGTH_SHORT).show()
                }
            }

            /*val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, title)
                putExtra(CalendarContract.Events.DESCRIPTION, description)
                putExtra(CalendarContract.Events.EVENT_LOCATION, "") // opcional
                putExtra(CalendarContract.Events.ALL_DAY, true) // ou false se quiser data/hora

                // Exemplo: evento hoje
                val startMillis = System.currentTimeMillis()
                val endMillis = startMillis + 60 * 60 * 1000 // 1h depois

                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            }*/

            // Verifica se há app de calendário


            Toast.makeText(this, "Tarefa criada: $title ($priority)", Toast.LENGTH_SHORT).show()
        }
    }
}