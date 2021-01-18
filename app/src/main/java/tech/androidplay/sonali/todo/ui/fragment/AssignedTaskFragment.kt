package tech.androidplay.sonali.todo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.databinding.FragmentAssignedTaskBinding
import tech.androidplay.sonali.todo.ui.adapter.TodoAdapter
import tech.androidplay.sonali.todo.utils.Constants.ARG_OBJECT
import tech.androidplay.sonali.todo.utils.viewLifecycleLazy
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 15/Jan/2021
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class AssignedTaskFragment : Fragment(R.layout.fragment_assigned_task) {

    private val binding by viewLifecycleLazy { FragmentAssignedTaskBinding.bind(requireView()) }

    @Inject
    lateinit var todoAdapter: TodoAdapter

    private val viewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpScreen()
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val tabNumber = getInt(ARG_OBJECT)
            showSelectedFragment(tabNumber)
        }
    }

    private fun setUpScreen() {
        binding.rvAssignedTaskList.adapter = todoAdapter
    }

    private fun showSelectedFragment(selectedTab: Int) {
        when (selectedTab) {
            0 -> observeAssignedTask()
            1 -> observeInProgressTask()
            2 -> observeCompletedTask()
            3 -> observeOverdueTask()
        }
    }

    private fun observeAssignedTask() {
        viewModel.upcomingTaskList.observe(viewLifecycleOwner, {
            todoAdapter.submitList(it)
        })
    }

    private fun observeInProgressTask() {
        viewModel.upcomingTaskList.observe(viewLifecycleOwner, {
            todoAdapter.submitList(it)
        })
    }

    private fun observeCompletedTask() {
        viewModel.completedTaskList.observe(viewLifecycleOwner, {
            todoAdapter.submitList(it)
        })
    }

    private fun observeOverdueTask() {
        viewModel.overdueTaskList.observe(viewLifecycleOwner, {
            todoAdapter.submitList(it)
        })
    }
}