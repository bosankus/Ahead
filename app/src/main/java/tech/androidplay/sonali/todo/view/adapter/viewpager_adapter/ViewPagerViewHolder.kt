package tech.androidplay.sonali.todo.view.adapter.viewpager_adapter

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.databinding.LayoutAssignedCardListBinding
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.view.fragment.TaskFragmentDirections

class ViewPagerViewHolder(private val binding: LayoutAssignedCardListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItem: Todo) {
        binding.todo = todoItem
        binding.executePendingBindings()

        binding.llAssignedCardItemList.setOnClickListener {
            val action =
                TaskFragmentDirections.actionTaskFragmentToTaskEditFragment(todoItem.docId)
            it?.findNavController()?.navigate(action)
        }
    }
}