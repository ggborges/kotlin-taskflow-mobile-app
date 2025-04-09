package borges.gustavo.taskflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import borges.gustavo.taskflow.R
import borges.gustavo.taskflow.model.Task

class TaskAdapter(private val context: Context, private val taskList: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txtTaskCardTitle)
        val description: TextView = itemView.findViewById(R.id.txtTaskCardDescription)
        val priority: TextView = itemView.findViewById(R.id.txtTaskCardPriority)
        val dateTime: TextView = itemView.findViewById(R.id.txtTaskCardDateTime)
    }

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
}