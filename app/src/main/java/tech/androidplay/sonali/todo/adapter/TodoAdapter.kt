package tech.androidplay.sonali.todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.model.Todo

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/8/2020, 2:08 AM
 */

class TodoAdapter(private val todoList: ArrayList<Todo>, options: FirestoreRecyclerOptions<Todo>) :
    FirestoreRecyclerAdapter<Todo, TodoAdapter.TodoViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_todo_list, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int, model: Todo) {
        holder.binItems(todoList[position])
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binItems(todoList: Todo) {
            // initializing view elements
            val tvTodoListItem = itemView.findViewById<TextView>(R.id.tvTodoListItem)
            val tvTodoListItemDesc = itemView.findViewById<TextView>(R.id.tvTodoListItemDesc)

            // setting views
            tvTodoListItem.text = todoList.todoBody
            tvTodoListItemDesc.text = todoList.todoDesc
        }
    }
}