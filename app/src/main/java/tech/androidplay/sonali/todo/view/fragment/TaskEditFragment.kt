package tech.androidplay.sonali.todo.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentTaskEditBinding
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.DateTimePicker
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.hideKeyboard
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.cancelAlarmedNotification
import tech.androidplay.sonali.todo.utils.isNetworkAvailable
import tech.androidplay.sonali.todo.utils.selectImage
import tech.androidplay.sonali.todo.utils.viewLifecycleLazy
import tech.androidplay.sonali.todo.view.adapter.setNotificationDateTime
import tech.androidplay.sonali.todo.view.adapter.setPriorityText
import tech.androidplay.sonali.todo.viewmodel.EditTaskViewModel
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Sep/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class TaskEditFragment : Fragment(R.layout.fragment_task_edit) {

    private val binding by viewLifecycleLazy { FragmentTaskEditBinding.bind(requireView()) }

    private val viewModel: EditTaskViewModel by viewModels()

    private val taskIdFromArgs by lazy {
        TaskEditFragmentArgs.fromBundle(requireArguments()).taskId
    }

    @Inject
    lateinit var dialog: AlertDialog.Builder

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var dateTimePicker: DateTimePicker

    private var pickedImage: Uri? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
        setListener()
        handleBackStackEntry()
        setObservers()
    }

    private fun handleBackStackEntry() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.taskEditFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("PRIORITY")) {
                val result = navBackStackEntry.savedStateHandle.get<Int>("PRIORITY")
                result?.let {
                    binding.tvTaskPriority.setPriorityText(it)
                    viewModel.todo.priority = it
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
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
            imgUploadTaskImg.setOnClickListener {
                if (isNetworkAvailable()) selectImage()
                else showSnack(requireView(), "Check Internet!")
            }
            tvTaskPriority.setOnClickListener {
                findNavController().navigate(R.id.action_taskEditFragment_to_prioritySelectionDialog)
            }
            tvDeleteTask.setOnClickListener { deleteTask(taskIdFromArgs) }
            tvSelectDate.setOnClickListener { dateTimePicker.openDateTimePicker(requireContext()) }
        }
    }


    private fun setObservers() {
        dateTimePicker.epochFormat.observe(viewLifecycleOwner) { epoch ->
            epoch?.let { dateTime ->
                binding.tvSelectDate.setNotificationDateTime(dateTime.toString())
                viewModel.todo.todoDate = dateTime.toString()
            }
        }
        viewModel.updateTaskState.observe(viewLifecycleOwner) { state ->
            state?.let { if (it is ResultData.Loading) hideKeyboard() }
        }
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            state?.let { if (it is ResultData.Success) binding.task = it.data as Todo }
        }
    }


    private fun fetchTask(taskId: String) {
        if (isNetworkAvailable()) viewModel.getTaskByTaskId(taskId)
        else showSnack(requireView(), "Check internet connection.")
    }


    private fun changeImage(pickedImage: Uri?) {
        if (isNetworkAvailable()) {
            lifecycleScope.launch {
                viewModel.uploadImage(pickedImage, taskIdFromArgs!!)
                viewModel.imageUploadState.observe(viewLifecycleOwner) { response ->
                    response?.let {
                        if (it is ResultData.Success) {
                            logMessage("${it.data}")
                            viewModel.todo.taskImage = "${it.data}"
                        }
                    }
                }
            }
        } else showSnack(requireView(), "Check internet connection.")
    }


    private fun deleteTask(docId: String?) {
        if (isNetworkAvailable()) {
            dialog
                .setMessage("Do you want to delete the task")
                .setCancelable(true)
                .setPositiveButton("Yes") { dialogInterface, _ ->
                viewModel.deleteTask()
                viewModel.deleteTaskState.observe(viewLifecycleOwner) { response ->
                    if (response is ResultData.Success<*>) {
                        requireContext().cancelAlarmedNotification(docId!!)
                        findNavController().navigate(R.id.action_taskEditFragment_to_taskFragment)
                        dialogInterface.dismiss()
                        showSnack(requireView(), "Task deleted")
                    }
                }
            }.create().show()
        } else showSnack(requireView(), "Check internet connection.")
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            pickedImage = data.data
            if (isNetworkAvailable()) changeImage(pickedImage)
            else showSnack(requireView(), "Connection lost!")
        }
    }
}