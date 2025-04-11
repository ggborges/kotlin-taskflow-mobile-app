package borges.gustavo.taskflow.adapter

import TaskViewModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import borges.gustavo.taskflow.R
import borges.gustavo.taskflow.activity.TaskCardActivity
import borges.gustavo.taskflow.model.Task

class TaskAdapter(
    private val context: Context,
    private val taskViewModel: TaskViewModel // Adicionando o TaskViewModel no Adapter
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txtTaskCardTitle)
        val description: TextView = itemView.findViewById(R.id.txtTaskCardDescription)
        val priority: TextView = itemView.findViewById(R.id.txtTaskCardPriority)
        val dateTime: TextView = itemView.findViewById(R.id.txtTaskCardDateTime)
        val checkboxTaskCompleted = itemView.findViewById<CheckBox>(R.id.checkboxTaskCompleted)
        val btnViewTask: Button = itemView.findViewById(R.id.btnViewTask)  // Botão de visualizar
        val btnDeleteTask: Button = itemView.findViewById(R.id.btnDeleteTask)  // Botão de deletar
    }

    // DiffUtil Callback para otimizar as mudanças na lista
    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            // Comparar os IDs das tarefas (assumindo que elas têm um ID único)
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            // Comparar os conteúdos das tarefas
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position) // Usar getItem() ao invés de acessar diretamente a lista
        holder.title.text = task.title
        holder.description.text = task.description
        holder.priority.text = context.getString(R.string.priority_label, task.priority)
        holder.dateTime.text = task.dateTime ?: context.getString(R.string.dateTime_label)

        holder.checkboxTaskCompleted.isChecked = task.isCompleted

        // Aplicar cor verde claro se a tarefa estiver concluída
        if (task.isCompleted) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.green_light))
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white)) // Cor original
        }

        // Lidar com o clique no CheckBox para concluir a tarefa
        holder.checkboxTaskCompleted.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked  // Atualizar o estado da tarefa

            task.id?.let { taskViewModel.updateTaskCompletion(it, isChecked) }  // Atualiza a tarefa no banco de dados

            // Atualizar a cor do card conforme a conclusão
            if (isChecked) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.green_light))
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }
        }

        // Clique para visualizar a tarefa
        holder.btnViewTask.setOnClickListener {
            val intent = Intent(context, TaskCardActivity::class.java)
            intent.putExtra("taskId", task.id) // Passando o ID da tarefa
            context.startActivity(intent)
        }

        // Clique para deletar a tarefa
        holder.btnDeleteTask.setOnClickListener {
            taskViewModel.deleteTask(task)
        }
    }

    /* Implementação sem ListAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.title.text = task.title
        holder.description.text = task.description
        holder.priority.text = context.getString(R.string.priority_label, task.priority)
        holder.dateTime.text = task.dateTime ?: context.getString(R.string.dateTime_label)
    }

    override fun getItemCount() = taskList.size
     */
}