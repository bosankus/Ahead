package tech.androidplay.sonali.todo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.databinding.LayoutMainTaskListBinding

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 7/22/2020, 10:08 PM
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TodoAdapter : ListAdapter<Todo, TodoViewHolder>(TodoDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutMainTaskListBinding.inflate(layoutInflater, parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = getItem(position)
        holder.bind(todoItem)
    }
}