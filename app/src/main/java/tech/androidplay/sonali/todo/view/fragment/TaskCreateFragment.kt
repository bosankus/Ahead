package tech.androidplay.sonali.todo.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentTaskCreateBinding
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.Constants.ANDROID_S
import tech.androidplay.sonali.todo.utils.Constants.ANDROID_T
import tech.androidplay.sonali.todo.utils.Constants.DEVICE_ANDROID_VERSION
import tech.androidplay.sonali.todo.utils.DateTimePicker
import tech.androidplay.sonali.todo.utils.DocIdGenerator.generateDocId
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.beautifyDateTime
import tech.androidplay.sonali.todo.utils.checkNotificationPermission
import tech.androidplay.sonali.todo.utils.loadImage
import tech.androidplay.sonali.todo.utils.selectImage
import tech.androidplay.sonali.todo.utils.startAlarmedNotification
import tech.androidplay.sonali.todo.utils.toLocalDateTime
import tech.androidplay.sonali.todo.view.adapter.setPriorityText
import tech.androidplay.sonali.todo.viewmodel.TaskCreateViewModel
import tech.androidplay.sonali.todo.workers.TaskCreationWorker.Companion.TASK_CREATION_WORKER_TAG
import tech.androidplay.sonali.todo.workers.TaskImageUploadWorker.Companion.IMAGE_UPLOAD_WORKER_TAG
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

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var dateTimePicker: DateTimePicker

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var dialog: AlertDialog.Builder

    private var binding: FragmentTaskCreateBinding? = null

    private val viewModel: TaskCreateViewModel by viewModels()

    private val requestMultiplePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                if (it.value) {
                    when (it.key) {
                        Manifest.permission.POST_NOTIFICATIONS -> viewModel.updateNotificationPermissionStatus(
                            true
                        )

                        Manifest.permission.SCHEDULE_EXACT_ALARM -> viewModel.updateScheduleAlarmPermissionStatus(
                            true
                        )
                    }
                }
            }
        }

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
        binding?.layoutTaskInput?.tvTaskInput?.requestFocus()
        handleBackStackEntry()
        setListeners()
        setObservers()
    }

    private fun handleBackStackEntry() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.taskCreateFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("PRIORITY")) {
                val result = navBackStackEntry.savedStateHandle.get<Int>("PRIORITY")
                result?.let {
                    binding?.layoutCreateTaskFeatures?.addPriority?.setPriorityText(it)
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

    private fun setListeners() {
        binding?.apply {

            // Navigate back
            imgBack.setOnClickListener { findNavController().navigateUp() }

            // Add task image from gallery.(non-mandatory field)
            // Check for permissions before proceeding.
            layoutCreateTaskFeatures.addImage.setOnClickListener {
                @SuppressLint("InlinedApi")
                if (DEVICE_ANDROID_VERSION >= ANDROID_T && !requireContext().checkNotificationPermission()) {
                    showDialog(
                        message = "Media and notification permissions are required to proceed.",
                        positiveButton = {
                            requestMultiplePermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.POST_NOTIFICATIONS,
                                    Manifest.permission.READ_MEDIA_IMAGES
                                )
                            )
                        },
                        negativeButton = { findNavController().navigateUp() }
                    )
                } else {
                    selectImage()
                }
            }

            // Set task date and time
            // Check for permissions before proceeding.
            layoutSetAlarm.tvSelectDate.setOnClickListener {
                @SuppressLint("InlinedApi")
                if (DEVICE_ANDROID_VERSION >= ANDROID_S && !alarmManager.canScheduleExactAlarms()) {
                    showDialog(
                        message = "Scheduling alarm permission is required to set alarmed task notification.",
                        positiveButton = {
                            val intent = Intent().apply {
                                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                            }
                            startActivity(intent)
                        },
                        negativeButton = { findNavController().navigateUp() }
                    )
                } else {
                    dateTimePicker.openDateTimePicker(requireContext())
                }
            }

            // Add task priority. (non-mandatory field)
            layoutCreateTaskFeatures.addPriority.setOnClickListener {
                findNavController()
                    .navigate(R.id.action_taskCreateFragment_to_prioritySelectionDialog)
            }

            // To removed added photo for task.
            layoutTaskImage.btnImgPhotoRemove.setOnClickListener {
                layoutTaskImage.imgPhoto.setImageDrawable(null)
                layoutTaskImage.clImagePlaceHolder.visibility = View.GONE
                viewModel.todo.taskImage = null
            }
        }
    }

    private fun setObservers() {
        // Observe the selected date time and convert it to understandable format for UI.
        dateTimePicker.epochFormat.observe(viewLifecycleOwner) { epochLong ->
            viewModel.todo.todoDate = epochLong.toString()
            binding?.layoutSetAlarm?.tvSelectDate?.text =
                epochLong.toString().toLocalDateTime()?.beautifyDateTime()
        }

        // Observe the task creation status and navigate back to home screen on success.
        viewModel.taskCreationStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ResultData.Loading -> showToast(requireContext(), "Creating")
                is ResultData.Failed -> showToast(requireContext(), status.message.toString())
                is ResultData.Success -> {
                    val taskItem = (status.data as Todo).apply { this.docId = generateDocId() }
                    setTaskAlarm(taskItem)
                    showToast(requireContext(), "Task Created")
                    findNavController().navigateUp()
                }
                else -> {
                }
            }
        }

        // Observe the image upload status and show snackbar on success/failure.
        workManager.getWorkInfosByTagLiveData(IMAGE_UPLOAD_WORKER_TAG).observe(
            viewLifecycleOwner
        ) { workInfoList ->
            if (workInfoList.size != 0 && workInfoList != null) {
                val workInfo = workInfoList[0]
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> showSnack(requireView(), "Image Uploaded")
                    WorkInfo.State.FAILED -> showSnack(requireView(), "Upload failed!")
                    else -> {
                    }
                }
            }
        }

        // Observe the task creation status and navigate back to home screen on success.
        workManager.getWorkInfosByTagLiveData(TASK_CREATION_WORKER_TAG).observe(
            viewLifecycleOwner
        ) { workInfoList ->
            if (workInfoList.size != 0 && workInfoList != null) {
                val workInfo = workInfoList[0]
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        showSnack(requireView(), "Task Created")
                        workManager.pruneWork()
                        findNavController().navigateUp()
                    }
                    WorkInfo.State.FAILED -> showSnack(requireView(), "Task creation failed!")
                    else -> {
                    }
                }
            }
        }
    }

    private fun setTaskAlarm(todo: Todo) {
        try {
            requireContext().startAlarmedNotification(
                todo.docId,
                todo.todoBody,
                todo.todoDesc.toString(),
                todo.todoDate?.toLong()!!,
                alarmManager
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("InlinedApi")
    private fun showDialog(
        message: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit,
    ) {
        dialog
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Accept") { dialogInterface, _ ->
                positiveButton()
                dialogInterface.dismiss()
            }
            .setNegativeButton("Go back") { dialogInterface, _ ->
                negativeButton()
                dialogInterface.dismiss()
            }
            .create().show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            viewModel.todo.taskImage = data.dataString
            binding?.apply {
                layoutTaskImage.clImagePlaceHolder.visibility = View.VISIBLE
                layoutTaskImage.imgPhoto.loadImage(data.dataString)
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
