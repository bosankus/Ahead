package tech.androidplay.sonali.todo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.ListAdapter
import dagger.hilt.android.AndroidEntryPoint
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 7/22/2020, 10:08 PM
 */

class TodoAdapter : ListAdapter<Todo, TodoViewHolder>(TodoDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_main_todo_list, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = getItem(position)
        holder.bind(todoItem)
    }

}