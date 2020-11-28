package tech.androidplay.sonali.todo.ui.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.databinding.LayoutMainTaskListBinding
import java.util.*
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 7/22/2020, 10:08 PM
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TodoAdapter @Inject constructor(
    private val viewModel: TaskViewModel,
    private val dialog: AlertDialog.Builder
) : ListAdapter<Todo, TodoViewHolder>(TodoDiffUtilCallback()) {

    private var unfilteredList = listOf<Todo>()

    fun modifyList(list: List<Todo>) {
        unfilteredList = list
        submitList(list)
    }

    fun filterList(query: CharSequence?) {
        val list = mutableListOf<Todo>()
        query?.let {
            list.addAll(unfilteredList.filter {
                it.todoBody.toLowerCase(Locale.getDefault())
                    .contains(query.toString().toLowerCase(Locale.getDefault()))
            })
        } ?: list.addAll(unfilteredList)
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutMainTaskListBinding.inflate(layoutInflater, parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = getItem(position)
        holder.bind(viewModel, todoItem, dialog)
        holder.itemView.setOnClickListener {

        }
    }
}