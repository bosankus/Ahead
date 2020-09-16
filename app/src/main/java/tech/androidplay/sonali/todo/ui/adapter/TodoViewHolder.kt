package tech.androidplay.sonali.todo.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_main_todo_list.view.*
import tech.androidplay.sonali.todo.data.model.Todo

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 8/11/2020, 4:54 PM
 */
class TodoViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    fun bind(todoItem: Todo) {
        itemView.tvTodoListItem.text = todoItem.todoBody
    }
}