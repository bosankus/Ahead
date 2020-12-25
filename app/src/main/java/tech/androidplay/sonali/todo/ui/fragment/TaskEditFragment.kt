package tech.androidplay.sonali.todo.ui.fragment

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
import kotlinx.android.synthetic.main.fragment_task_edit.*
import kotlinx.coroutines.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.utils.Constants.IS_AFTER
import tech.androidplay.sonali.todo.utils.Constants.IS_BEFORE
import tech.androidplay.sonali.todo.utils.Constants.IS_EQUAL
import tech.androidplay.sonali.todo.utils.Constants.TASK_DATE
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_BODY
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_DESC
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import tech.androidplay.sonali.todo.utils.Constants.TASK_IMAGE_URL
import tech.androidplay.sonali.todo.utils.DateTimePicker
import tech.androidplay.sonali.todo.utils.Extensions.beautifyDateTime
import tech.androidplay.sonali.todo.utils.Extensions.compareWithToday
import tech.androidplay.sonali.todo.utils.Extensions.compressImage
import tech.androidplay.sonali.todo.utils.Extensions.loadImageCircleCropped
import tech.androidplay.sonali.todo.utils.Extensions.selectImage
import tech.androidplay.sonali.todo.utils.Extensions.setTint
import tech.androidplay.sonali.todo.utils.Extensions.toLocalDateTime
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.alarmutils.cancelAlarmedNotification
import tech.androidplay.sonali.todo.utils.alarmutils.startAlarmedNotification
import tech.androidplay.sonali.todo.utils.isNetworkAvailable
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

    @Inject
    lateinit var dialog: AlertDialog.Builder

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var pendingIntent: PendingIntent

    @Inject
    lateinit var dateTimePicker: DateTimePicker


    private val taskViewModel: TaskViewModel by viewModels()

    private var taskId: String? = ""
    private var taskBody: String = ""
    private var taskDesc: String = ""
    private var taskTimeStamp: String? = ""
    private var taskImage: String? = ""
    private var newTaskTimeStamp: String? = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
        setListener()
    }

    private fun setUpScreen() {
        taskId = arguments?.getString(TASK_DOC_ID)
        taskBody = arguments?.getString(TASK_DOC_BODY)!!
        taskDesc = arguments?.getString(TASK_DOC_DESC)!!

        taskTimeStamp = arguments?.getString(TASK_DATE)
        newTaskTimeStamp = taskTimeStamp // setting initial value to same

        taskImage = arguments?.getString(TASK_IMAGE_URL)
        handleTaskImage(taskImage)

        etTaskBody.setText(taskBody)
        etTaskDesc.setText(taskDesc)
        // Need to change
        tvSelectDate.text = taskTimeStamp?.toLocalDateTime()?.beautifyDateTime().toString()
        if (taskTimeStamp?.compareWithToday() == IS_BEFORE)
            tvSelectDate.text = "You were notified on " +
                    "${taskTimeStamp?.toLocalDateTime()?.beautifyDateTime()}. Tap here to change."
        else tvSelectDate.text = "You will be notified on " +
                "${taskTimeStamp?.toLocalDateTime()?.beautifyDateTime()}. Tap here to change."
    }

    private fun setListener() {
        imgUploadTaskImg.setOnClickListener {
            if (isNetworkAvailable()) selectImage() else showSnack(
                requireView(),
                "Check Internet!"
            )
        }
        tvDeleteTask.setOnClickListener { deleteTask(taskId) }
        btnSaveTasks.setOnClickListener { saveTask() }
        tvSelectDate.setOnClickListener { dateTimePicker.openDateTimePicker(requireContext()) }
        dateTimePicker.epochFormat.observe(viewLifecycleOwner, { epoch ->
            epoch?.let {
                newTaskTimeStamp = "$it"
                tvSelectDate.text = "You will be notified on ${
                    it.toString().toLocalDateTime()?.beautifyDateTime()
                }. Tap here to change."
            }
        })
    }


    // TODO: create alarm on change of date
    private fun saveTask() {
        if (isNetworkAvailable()) {
            val taskBody = etTaskBody.text.toString().trim()
            val taskDesc = etTaskDesc.text.toString().trim()
            val taskDate = newTaskTimeStamp

            if ((this.taskBody.compareTo(taskBody) != 0 || this.taskDesc.compareTo(taskDesc) != 0)) {
                taskViewModel.updateTask(taskId!!, taskBody, taskDesc, taskDate)
                showToast(requireContext(), "Task Saved")
            } else if (taskDate?.compareTo(this.taskTimeStamp.toString()) != 0 &&
                taskDate?.compareWithToday() == IS_AFTER
            ) {
                taskViewModel.updateTask(taskId!!, taskBody, taskDesc, taskDate)
                startAlarmedNotification(
                    taskId!!,
                    taskBody,
                    taskDesc,
                    taskDate.toLong(),
                    alarmManager
                )
                showToast(requireContext(), "Task Saved")
            } else if (taskDate?.compareTo(this.taskTimeStamp.toString()) != 0
                && (taskDate?.compareWithToday() == IS_BEFORE || taskDate?.compareWithToday() == IS_EQUAL)
            ) {
                taskViewModel.updateTask(taskId!!, taskBody, taskDesc, taskDate)
                showToast(requireContext(), "Task Saved")
            } else showToast(requireContext(), "You didn't make any change yet.")
        } else showSnack(requireView(), "Check internet connection.")
    }

    private fun changeImage(pickedImage: Uri?) {
        lifecycleScope.launch {
            val compressedImage = pickedImage?.compressImage(requireContext())
            withContext(Dispatchers.Main) {
                taskViewModel.uploadImage(compressedImage, taskId!!)
                    .observe(viewLifecycleOwner, { imageUrl ->
                        when (imageUrl) {
                            is ResultData.Loading -> {
                                tvNoTaskImg.text = "Uploading Image..."
                                showToast(requireContext(), "Uploading Image")
                            }
                            is ResultData.Success -> {
                                val url = imageUrl.data
                                handleTaskImage(url)
                                showToast(requireContext(), "Great! Image uploaded")
                            }
                            is ResultData.Failed -> showToast(
                                requireContext(),
                                "Something went wrong"
                            )
                        }
                    })
            }
        }
    }

    private fun handleTaskImage(url: String?) {
        if (!url.isNullOrEmpty()) {
            tvNoTaskImg.visibility = View.GONE
            imgTask.visibility = View.VISIBLE
            imgTask.loadImageCircleCropped(url)
            imgUploadTaskImg.setTint(R.color.white)
        } else {
            tvNoTaskImg.visibility = View.VISIBLE
            imgTask.visibility = View.GONE
            imgUploadTaskImg.setTint(R.color.dribblePink)
        }
    }

    private fun deleteTask(docId: String?) {
        dialog.setPositiveButton("Yes") { dialogInterface, _ ->
            taskViewModel.deleteTask(docId)?.observe(viewLifecycleOwner, {
                when (it) {
                    is ResultData.Loading -> {
                    }
                    is ResultData.Success -> {
                        cancelAlarmedNotification(docId!!)
                        findNavController().navigate(R.id.action_taskEditFragment_to_taskFragment)
                        dialogInterface.dismiss()
                        showToast(requireContext(), "Task deleted")
                    }
                    is ResultData.Failed -> showSnack(requireView(), it.toString())
                }
            })
        }.create().show()
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            pickedImage = data.data
            imgTask.loadImageCircleCropped(pickedImage.toString())
            if (isNetworkAvailable()) changeImage(pickedImage) else showSnack(
                requireView(),
                "Connection lost!"
            )
        }
    }

    companion object {
        var pickedImage: Uri? = null
    }

}