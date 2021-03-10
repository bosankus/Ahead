package tech.androidplay.sonali.todo.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentTaskEditBinding
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.*
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
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

    @Inject
    lateinit var dialog: AlertDialog.Builder

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var pendingIntent: PendingIntent

    @Inject
    lateinit var dateTimePicker: DateTimePicker

    private val viewModel: EditTaskViewModel by viewModels()
    private var taskId: String? = ""
    private var pickedImage: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
        setListener()
    }

    private fun setUpScreen() {
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        taskId = arguments?.getString(TASK_DOC_ID)
        taskId?.let { fetchTask(it) } ?: showSnack(requireView(), "Can't find document Id")
    }

    private fun setListener() {
        binding.apply {
            imgUploadTaskImg.setOnClickListener {
                if (isNetworkAvailable()) selectImage()
                else showSnack(requireView(), "Check Internet!")
            }
            cbCompleteTask.setOnClickListener {
                if (binding.cbCompleteTask.isChecked) viewmodel?.changeTaskStatus(true)
                else viewmodel?.changeTaskStatus(false)
            }
            tvDeleteTask.setOnClickListener { deleteTask(taskId) }
            btnSaveTask.setOnClickListener { updateTask() }
            tvSelectDate.setOnClickListener { selectNotificationDateTime() }
        }
    }

    private fun selectNotificationDateTime() {
        dateTimePicker.openDateTimePicker(requireContext())
        dateTimePicker.epochFormat.observe(viewLifecycleOwner, { epoch ->
            epoch?.let { dateTime -> viewModel.initialTaskDate.set("$dateTime") }
        })
    }

    private fun fetchTask(taskId: String) {
        if (isNetworkAvailable()) {
            viewModel.getTaskByTaskId(taskId)
            viewModel.viewState.observe(viewLifecycleOwner, { response ->
                response?.let { if (it is ResultData.Success) binding.task = it.data as Todo }
            })
        } else showSnack(requireView(), "Check internet connection.")
    }

    private fun updateTask() {
        if (isNetworkAvailable()) {
            viewModel.updateTask()
            viewModel.updateTaskState.observe(viewLifecycleOwner, { response ->
                response?.let {
                    if (it is ResultData.Success) {
                        requireContext().startAlarmedNotification(
                            viewModel.initialTaskId.get()!!,
                            viewModel.initialTaskBody.get()!!,
                            viewModel.initialTaskDesc.get()!!,
                            viewModel.initialTaskDate.get()?.toLong()!!,
                            alarmManager
                        )
                        showToast(requireContext(), "Task Saved")
                    }
                }
            })
        } else showSnack(requireView(), "Check internet connection.")
    }

    private fun changeImage(pickedImage: Uri?) {
        if (isNetworkAvailable()) {
            lifecycleScope.launch {
                val compressedImage = pickedImage?.compressImage(requireContext())
                viewModel.uploadImage(compressedImage, taskId!!)
                viewModel.imageUploadState.observe(viewLifecycleOwner, { response ->
                    response?.let {
                        if (it is ResultData.Success) viewModel.initialTaskImage.set("${it.data}")
                    }
                })
            }
        } else showSnack(requireView(), "Check internet connection.")
    }

    private fun deleteTask(docId: String?) {
        if (isNetworkAvailable()) {
            dialog.setPositiveButton("Yes") { dialogInterface, _ ->
                viewModel.deleteTask()
                viewModel.deleteTaskState.observe(viewLifecycleOwner, { response ->
                    if (response is ResultData.Success<*>) {
                        requireContext().cancelAlarmedNotification(docId!!)
                        findNavController().navigate(R.id.action_taskEditFragment_to_taskFragment)
                        dialogInterface.dismiss()
                        showSnack(requireView(), "Task deleted")
                    }
                })
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