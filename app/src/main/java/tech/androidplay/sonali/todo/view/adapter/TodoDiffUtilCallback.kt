package tech.androidplay.sonali.todo.view.adapter

import androidx.recyclerview.widget.DiffUtil
import tech.androidplay.sonali.todo.model.Todo

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 8/11/2020, 4:56 PM
 */
class TodoDiffUtilCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.docId == newItem.docId
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}