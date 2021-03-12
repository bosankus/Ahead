package tech.androidplay.sonali.todo.view.adapter.viewpager_adapter

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.LayoutAssignedCardListBinding
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import tech.androidplay.sonali.todo.view.adapter.main_adapter.TodoViewHolder
import tech.androidplay.sonali.todo.viewmodel.TaskViewModel

class ViewPagerViewHolder(private val binding: LayoutAssignedCardListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItem: Todo) {
        binding.todo = todoItem
        binding.executePendingBindings()

        binding.llAssignedCardItemList.setOnClickListener {
            val bundle = bundleOf(TASK_DOC_ID to todoItem.docId)
            it?.findNavController()?.navigate(R.id.action_global_taskEditFragment, bundle)
        }
    }
}