package tech.androidplay.sonali.todo.view.fragment

import android.app.Activity
import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import tech.androidplay.sonali.todo.utils.*
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
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

    private var binding: FragmentTaskCreateBinding? = null

    private val viewModel: TaskCreateViewModel by viewModels()


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
                viewModel.todo.taskImage = null
            }
        }
    }


    private fun setObservers() {
        dateTimePicker.epochFormat.observe(viewLifecycleOwner, { epochLong ->
            viewModel.todo.todoDate = epochLong.toString()
            binding?.layoutSetAlarm?.tvSelectDate?.text =
                epochLong.toString().toLocalDateTime()?.beautifyDateTime()
        })

        viewModel.taskCreationStatus.observe(viewLifecycleOwner, { status ->
            when (status) {
                is ResultData.Loading -> showToast(requireContext(), "Creating")
                is ResultData.Failed -> showToast(requireContext(), status.message.toString())
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
                        showToast(requireContext(), "Task Created")
                        findNavController().navigateUp()
                    }
                }
                else -> {
                }
            }
        })

        workManager.getWorkInfosByTagLiveData(IMAGE_UPLOAD_WORKER_TAG).observe(viewLifecycleOwner,
            { workInfoList ->
                if (workInfoList.size != 0 && workInfoList != null) {
                    val workInfo = workInfoList[0]
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> showSnack(requireView(), "Image Uploaded")
                        WorkInfo.State.FAILED -> showSnack(requireView(), "Upload failed!")
                        else -> {
                        }
                    }
                }
            })

        workManager.getWorkInfosByTagLiveData(TASK_CREATION_WORKER_TAG).observe(viewLifecycleOwner,
            { workInfoList ->
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
            })
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


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
