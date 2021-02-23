package tech.androidplay.sonali.todo.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task_completed.*
import kotlinx.android.synthetic.main.layout_task_app_bar.view.*
import kotlinx.android.synthetic.main.layout_task_empty.view.*
import kotlinx.android.synthetic.main.layout_task_upcoming.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.view.adapter.main_adapter.TodoAdapter
import tech.androidplay.sonali.todo.viewmodel.TaskViewModel
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

    @Inject
    lateinit var adapter: TodoAdapter
    private val viewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScreen()
        setObservers()
    }

    private fun setScreen() {
        layoutTaskBar.apply {
            tvUserFName.text = "Completed task"
            imgCreate.visibility = View.GONE
            imgMenu.visibility = View.GONE
        }
        layoutNoTask.btnAddTask.text = "It's empty here!"
    }

    private fun setObservers() {
        layoutCompletedTask.rvUpcomingTaskList.adapter = adapter
        viewModel.completedTaskList.observe(viewLifecycleOwner, { list ->
            list?.let { adapter.submitList(it) }
        })
    }
}