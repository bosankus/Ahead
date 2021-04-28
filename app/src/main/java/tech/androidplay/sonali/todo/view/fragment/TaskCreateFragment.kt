package tech.androidplay.sonali.todo.view.fragment

import android.app.Activity
import android.app.AlarmManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.work.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentTaskCreateBinding
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.*
import tech.androidplay.sonali.todo.utils.UIHelper.hideKeyboard
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.view.adapter.setPriorityText
import tech.androidplay.sonali.todo.viewmodel.TaskCreateViewModel
import tech.androidplay.sonali.todo.workers.TaskCreationWorker
import tech.androidplay.sonali.todo.workers.TaskCreationWorker.Companion.TASK_ASSIGNEE
import tech.androidplay.sonali.todo.workers.TaskCreationWorker.Companion.TASK_BODY
import tech.androidplay.sonali.todo.workers.TaskCreationWorker.Companion.TASK_DATE
import tech.androidplay.sonali.todo.workers.TaskCreationWorker.Companion.TASK_DESC
import tech.androidplay.sonali.todo.workers.TaskCreationWorker.Companion.TASK_IMAGE_URI
import tech.androidplay.sonali.todo.workers.TaskImageUploadWorker
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/3/2020, 12:22 PM
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class TaskCreateFragment : Fragment(R.layout.fragment_task_create) {

    private var binding: FragmentTaskCreateBinding? = null
    private val viewModel: TaskCreateViewModel by activityViewModels()
    private var taskTimeStamp: String? = null
    private var taskImage: Uri? = null
    private var assigneeId: String? = null

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var dateTimePicker: DateTimePicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_create, container, false)
        return binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
        setListeners()
        setObservers()
    }


    private fun setUpScreen() {
        binding?.layoutTaskInput?.tvTaskInput?.requestFocus()
    }

    private fun setListeners() {
        binding?.apply {
            imgBack.setOnClickListener { findNavController().navigateUp() }

            layoutCreateTaskFeatures.addImage.setOnClickListener { selectImage() }

            layoutSetAlarm.tvSelectDate.setOnClickListener {
                dateTimePicker.openDateTimePicker(requireContext())
            }

            layoutCreateTaskFeatures.addPriority.setOnClickListener {
                findNavController()
                    .navigate(R.id.action_taskCreateFragment_to_prioritySelectionDialog)
            }

            layoutTaskImage.btnImgPhotoRemove.setOnClickListener {
                layoutTaskImage.imgPhoto.setImageDrawable(null)
                layoutTaskImage.clImagePlaceHolder.visibility = View.GONE
            }
        }
    }


    private fun setObservers() {
        binding?.apply {
            dateTimePicker.epochFormat.observe(viewLifecycleOwner, {
                layoutSetAlarm.tvSelectDate.text =
                    taskTimeStamp?.toLocalDateTime()?.beautifyDateTime()
                viewModel.todo.todoDate = it.toString()
            })

            viewModel.taskPriority.observe(viewLifecycleOwner, { priority ->
                layoutCreateTaskFeatures.addPriority.setPriorityText(priority)
            })

            viewModel.taskCreationStatus.observe(viewLifecycleOwner, { status ->
                when (status) {
                    is ResultData.Loading -> showSnack(requireView(), "Creating")
                    is ResultData.Failed -> showSnack(requireView(), status.message.toString())
                    is ResultData.Success -> {
                        val taskItem = status.data as Todo
                        taskItem.todoDate?.let {
                            requireContext().startAlarmedNotification(
                                taskItem.docId,
                                taskItem.todoBody,
                                taskItem.todoDesc.toString(),
                                it.toLong(),
                                alarmManager
                            )
                        }.also {
                            showSnack(requireView(), "Task Created")
                            findNavController().navigateUp()
                        }
                    }
                    else -> {}
                }
            })
        }
    }


    private fun createTask() {
        requireActivity().hideKeyboard()
        val todoBody = binding?.layoutTaskInput?.tvTaskInput?.text.toString().trim()
        val todoDesc = binding?.layoutTaskInput?.tvTaskDescInput?.text.toString().trim()
        val todoDate = taskTimeStamp
        val assigneeList = arrayOf(assigneeId)

        if (taskImage == null) {
        } else {
            // creating task via work manager
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val inputData = workDataOf(
                TASK_BODY to todoBody,
                TASK_DESC to todoDesc,
                TASK_DATE to todoDate,
                TASK_ASSIGNEE to assigneeList,
                TASK_IMAGE_URI to taskImage.toString()
            )

            val taskImageUploadWorker =
                OneTimeWorkRequestBuilder<TaskImageUploadWorker>()
                    .setConstraints(constraints)
                    .setInputData(inputData)
                    .build()

            val taskCreationWorker =
                OneTimeWorkRequestBuilder<TaskCreationWorker>()
                    .setConstraints(constraints)
                    .build()

            WorkManager.getInstance(requireContext()).let { manager ->
                manager.beginWith(taskImageUploadWorker)
                    .then(taskCreationWorker)
                    .enqueue()
                taskImage = null
                findNavController().navigateUp()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            viewModel.todo.taskImage = data.dataString
            binding?.apply {
                layoutTaskImage.clImagePlaceHolder.visibility = View.VISIBLE
                layoutTaskImage.imgPhoto.loadImage(data.dataString)
            }
        }
    }
}
