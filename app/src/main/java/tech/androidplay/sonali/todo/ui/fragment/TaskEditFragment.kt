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
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task_edit.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.utils.Constants.TASK_DATE
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_BODY
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_DESC
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import tech.androidplay.sonali.todo.utils.Constants.TASK_IMAGE_URL
import tech.androidplay.sonali.todo.utils.Extensions.beautifyDateTime
import tech.androidplay.sonali.todo.utils.Extensions.loadImageCircleCropped
import tech.androidplay.sonali.todo.utils.Extensions.selectImage
import tech.androidplay.sonali.todo.utils.Extensions.toLocalDateTime
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.alarmutils.DateTimeUtil
import tech.androidplay.sonali.todo.utils.alarmutils.cancelAlarmedNotification
import tech.androidplay.sonali.todo.utils.alarmutils.generateRequestCode
import tech.androidplay.sonali.todo.utils.alarmutils.startAlarmedNotification
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
    lateinit var dateTimeUtil: DateTimeUtil

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
        taskImage = arguments?.getString(TASK_IMAGE_URL)
        newTaskTimeStamp = taskTimeStamp // setting initial value to same

        etTaskBody.setText(taskBody)
        etTaskDesc.setText(taskDesc)
        tvSelectDate.text = taskTimeStamp?.toLocalDateTime()?.beautifyDateTime().toString()
        /*tvSelectDate.text = dateTimeUtil.convertEpochMilliToDateTime(taskTimeStamp?.toLong())*/
        if (!taskImage.isNullOrEmpty()) {
            imgTask.loadImageCircleCropped(taskImage!!)
            imgTaskNoImage.visibility = View.INVISIBLE
            imgTask.visibility = View.VISIBLE
        } else {
            imgTaskNoImage.visibility = View.VISIBLE
            imgTask.visibility = View.INVISIBLE
        }
    }

    private fun setListener() {
        tvSelectDate.setOnClickListener { dateTimeUtil.openDateTimePicker(requireContext()) }
        /*dateTimeUtil.dateTimeFormat.observe(viewLifecycleOwner, { tvSelectDate.text = it })*/
        dateTimeUtil.epochFormat.observe(viewLifecycleOwner, {
            newTaskTimeStamp = "$it"
            tvSelectDate.text = it.toString().toLocalDateTime()?.beautifyDateTime()
        })

        /*btnDeleteTask.setOnClickListener { deleteTask(taskId) }*/

        imgTask.setOnClickListener { selectImage(this) }
        imgTaskNoImage.setOnClickListener { selectImage(this) }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            saveTask()
            showSnack(requireView(), "Task Saved")
            findNavController().navigate(R.id.action_taskEditFragment_to_taskFragment)
        }
    }


    // TODO: create alarm on change of date
    private fun saveTask() {
        val taskBody = etTaskBody.text.toString()
        val taskDesc = etTaskDesc.text.toString()
        val taskDate = newTaskTimeStamp

        if (this.taskBody.compareTo(taskBody) != 0 ||
            this.taskDesc.compareTo(taskDesc) != 0
        ) {
            taskViewModel.updateTask(taskId!!, taskBody, taskDesc, taskDate)
        } else if (this.taskTimeStamp?.compareTo(taskDate!!) != 0) {
            taskViewModel.updateTask(taskId!!, taskBody, taskDesc, taskDate)

            val requestCode = taskId!!.generateRequestCode()
            taskDate?.toLong()?.let {
                startAlarmedNotification(requestCode, taskBody, taskDesc, it, alarmManager)
            }
        } else return
    }


    private fun changeImage(pickedImage: Uri?) {
        taskViewModel.uploadImage(pickedImage, taskId!!).observe(viewLifecycleOwner, { imageUrl ->
            when (imageUrl) {
                is ResultData.Loading -> showToast(requireContext(), "Uploading Image")
                is ResultData.Success -> {
                    val url = imageUrl.data
                    url?.let { imgTask.loadImageCircleCropped(it) }
                    imgTaskNoImage.visibility = View.INVISIBLE
                    imgTask.visibility = View.VISIBLE
                }
                is ResultData.Failed -> showToast(requireContext(), "Something went wrong")
            }
        })
    }

    private fun deleteTask(docId: String?) {
        dialog.setPositiveButton("Yes") { dialogInterface, _ ->
            val requestCode = docId!!.generateRequestCode()
            taskViewModel.deleteTask(docId)
            cancelAlarmedNotification(requestCode)
            findNavController().navigate(R.id.action_taskEditFragment_to_taskFragment)
            dialogInterface.dismiss()
            showToast(requireContext(), "Task deleted.")
        }.create().show()
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            pickedImage = data.data
            changeImage(pickedImage)
        }
    }

    companion object {
        var pickedImage: Uri? = null
    }

}