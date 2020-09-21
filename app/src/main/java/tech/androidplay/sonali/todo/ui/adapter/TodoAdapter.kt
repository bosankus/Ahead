package tech.androidplay.sonali.todo.ui.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.databinding.LayoutMainTodoListBinding
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 7/22/2020, 10:08 PM
 */

class TodoAdapter @Inject constructor(
    private val viewModel: TaskViewModel,
    private val dialog: AlertDialog.Builder
) :
    ListAdapter<Todo, TodoViewHolder>(TodoDiffUtilCallback()), TodoEventListener {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutMainTodoListBinding.inflate(layoutInflater, parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = getItem(position)
        holder.bind(viewModel, todoItem, dialog)
        holder.itemView.setOnClickListener { }
    }

    override fun completeTask(todo: Todo) {
        logMessage(todo.todoBody)
    }

}