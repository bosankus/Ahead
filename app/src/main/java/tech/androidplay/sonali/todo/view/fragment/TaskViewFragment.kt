package tech.androidplay.sonali.todo.view.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentTaskViewBinding
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.isNetworkAvailable
import tech.androidplay.sonali.todo.utils.viewLifecycleLazy
import tech.androidplay.sonali.todo.viewmodel.ViewTaskViewModel

/**Created by
Author: Ankush Bose
Date: 10,May,2021
 **/

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TaskViewFragment : Fragment(R.layout.fragment_task_view) {

    private val binding by viewLifecycleLazy { FragmentTaskViewBinding.bind(requireView()) }

    private val viewModel: ViewTaskViewModel by viewModels()

    private val animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_bottom)
    }

    private val taskIdFromArgs by lazy {
        TaskViewFragmentArgs.fromBundle(requireArguments()).taskId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
        setListener()
        setObservers()
    }


    private fun setUpScreen() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        taskIdFromArgs?.let { fetchTask(it) } ?: showSnack(requireView(), "Can't find task Id")
    }


    private fun setListener() {
        binding.apply {
            fragmentTaskViewImgCalendar.setOnClickListener { }
        }
    }


    private fun setObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, { response ->
            response?.let {
                if (it is ResultData.Success)
                    binding.fragmentViewBottomLayout.startAnimation(animation)
            }
        })
    }


    private fun fetchTask(taskId: String) {
        if (isNetworkAvailable()) viewModel.getTaskByTaskId(taskId)
        else showSnack(requireView(), "Check internet connection.")
    }
}