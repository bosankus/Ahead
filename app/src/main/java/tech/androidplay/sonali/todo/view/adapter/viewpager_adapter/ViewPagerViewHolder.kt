package tech.androidplay.sonali.todo.view.adapter.viewpager_adapter

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.databinding.LayoutAssignedCardListBinding
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.viewmodel.TaskViewModel

class ViewPagerViewHolder(private val binding: LayoutAssignedCardListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItem: Todo) {
        binding.todo = todoItem
        binding.executePendingBindings()
    }
}