package borges.gustavo.taskflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import borges.gustavo.taskflow.R
import borges.gustavo.taskflow.model.Task

class TaskAdapter(private val context: Context) :
    ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txtTaskCardTitle)
        val description: TextView = itemView.findViewById(R.id.txtTaskCardDescription)
        val priority: TextView = itemView.findViewById(R.id.txtTaskCardPriority)
        val dateTime: TextView = itemView.findViewById(R.id.txtTaskCardDateTime)
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