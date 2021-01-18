package tech.androidplay.sonali.todo.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.databinding.FragmentTaskCompletedBinding
import tech.androidplay.sonali.todo.ui.adapter.TodoAdapter
import tech.androidplay.sonali.todo.utils.viewLifecycleLazy
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 13/Dec/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class TaskCompletedFragment : Fragment(R.layout.fragment_task_completed) {

    private val binding by viewLifecycleLazy { FragmentTaskCompletedBinding.bind(requireView()) }

    @Inject
    lateinit var adapter: TodoAdapter
    private val viewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScreen()
        setObservers()
    }

    private fun setScreen() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.layoutTaskBar.imgMenu.visibility = View.GONE
        binding.layoutNoTask.tvEmptyTask.text = "Oh boi! No Completed task"
        binding.layoutCompletedTask.tvTodayHeader.text = "COMPLETED TASK"
    }

    private fun setObservers() {
        binding.layoutCompletedTask.rvTodayList.adapter = adapter
        viewModel.completedTaskList.observe(viewLifecycleOwner, { list ->
            list?.let {
                adapter.submitList(it)
                // Crashing: If clicked on items as no direction is present from this fragment to edit fragment
            }
        })
    }
}