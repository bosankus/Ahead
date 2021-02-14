package tech.androidplay.sonali.todo.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentTaskCreateBinding
import tech.androidplay.sonali.todo.utils.*
import tech.androidplay.sonali.todo.utils.Constants.IS_AFTER
import tech.androidplay.sonali.todo.utils.UIHelper.hideKeyboard
import tech.androidplay.sonali.todo.utils.UIHelper.isEmailValid
import tech.androidplay.sonali.todo.utils.UIHelper.setSvgTint
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.alarmutils.startAlarmedNotification
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

    private val binding by viewLifecycleLazy { FragmentTaskCreateBinding.bind(requireView()) }

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var dateTimePicker: DateTimePicker

    private val viewModel: TaskCreateViewModel by viewModels()
    private var taskTimeStamp: String? = null

    private var isAssigneeShowing = false
    private var isImageAdded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
        setListeners()
    }

    private fun setUpScreen() {
        binding.layoutTaskInput.tvTaskInput.requestFocus()

    }

    private fun setListeners() {
        dateTimePicker.epochFormat.observe(viewLifecycleOwner, {
            taskTimeStamp = it.toString()
            binding.layoutSetAlarm.tvSelectDate.text =
                taskTimeStamp?.toLocalDateTime()?.beautifyDateTime()
        })

        binding.btCreateTask.setOnClickListener {
            requireActivity().hideKeyboard()
            if ((binding.layoutTaskInput.tvTaskInput.text.length) <= 0)
                showSnack(requireView(), "Fields can not be empty!")
            else if (taskTimeStamp.isNullOrEmpty())
                showSnack(requireView(), "Please select notification time")
            else if (!isNetworkAvailable()) showSnack(requireView(), "No Internet!")
            else createTask()
        }

        binding.layoutCreateTaskFeatures.addImage.setOnClickListener {
            selectImage()
            if (isImageAdded) binding.layoutCreateTaskFeatures.addImage.setSvgTint(R.color.dribblePink)
            else if (!isImageAdded) binding.layoutCreateTaskFeatures.addImage.setSvgTint(R.color.grey3)
        }

        binding.layoutTaskImage.btnImgPhotoRemove.setOnClickListener {
            binding.layoutTaskImage.imgPhoto.setImageDrawable(null)
            binding.layoutTaskImage.clImagePlaceHolder.visibility = View.GONE
            isImageAdded = false
        }

        binding.layoutCreateTaskFeatures.addUser.setOnClickListener {
            if (isAssigneeShowing)
                showAssigneeOption(binding.layoutCreateTaskFeatures.addUser, false)
            else if (!isAssigneeShowing)
                showAssigneeOption(binding.layoutCreateTaskFeatures.addUser, true)
        }

        binding.layoutAssigneeUser.etAssigneeUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?, p1: Int, p2: Int, p3: Int
            ) {
            }

            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                charSequence?.let { if (it.isEmailValid()) checkAssigneeAvailability(it.toString()) }
            }
        })

        binding.layoutSetAlarm.tvSelectDate.setOnClickListener {
            dateTimePicker.openDateTimePicker(
                requireContext()
            )
        }
    }

    private fun showAssigneeOption(icon: ImageView, show: Boolean) {
        isAssigneeShowing = if (show) {
            icon.setSvgTint(R.color.dribblePink)
            binding.layoutAssigneeUser.clAssignUser.visibility = View.VISIBLE
            true
        } else {
            icon.setSvgTint(R.color.grey3)
            binding.layoutAssigneeUser.clAssignUser.visibility = View.GONE
            false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkAssigneeAvailability(email: String) {
        viewModel.checkAssigneeAvailability(email).observe(viewLifecycleOwner, { result ->
            result?.let {
                when (it) {
                    is ResultData.Loading -> binding.layoutAssigneeUser.lottieAvailabilityLoading.visibility =
                        View.VISIBLE
                    is ResultData.Success -> {
                        binding.layoutAssigneeUser.apply {
                            lottieAvailabilityLoading.visibility = View.INVISIBLE
                            tvAssigneeAvailability.visibility = View.VISIBLE
                            tvAssigneeAvailability.text = "User is available"
                        }
                        assigneeId = it.data
                    }
                    is ResultData.Failed -> {
                        binding.layoutAssigneeUser.apply {
                            lottieAvailabilityLoading.visibility = View.INVISIBLE
                            tvAssigneeAvailability.visibility = View.VISIBLE
                            tvAssigneeAvailability.text = "No user found"
                        }
                        assigneeId = null
                    }
                }
            }
        })
    }


    private fun createTask() {
        val todoBody = binding.layoutTaskInput.tvTaskInput.text.toString().trim()
        val todoDesc = binding.layoutTaskInput.tvTaskDescInput.text.toString().trim()
        val todoDate = taskTimeStamp
        val assignee = assigneeId
        /*lifecycleScope.launch {
            val compressedImage = taskImage?.compressImage(requireContext())
            withContext(Dispatchers.Main) {
                viewModel.createTask(todoBody, todoDesc, todoDate, assignee, compressedImage)
                    .observe(viewLifecycleOwner, {
                        it?.let {
                            when (it) {
                                is ResultData.Loading -> handleLoading()
                                is ResultData.Success -> handleSuccess(
                                    it.data!!, todoBody, todoDesc
                                )
                                is ResultData.Failed -> handleFailure()
                            }
                        }
                    })
            }
        }*/

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = workDataOf(
            TASK_BODY to todoBody,
            TASK_DESC to todoDesc,
            TASK_DATE to todoDate,
            TASK_ASSIGNEE to assignee,
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

            findNavController().navigateUp()
        }

    }

    private fun handleLoading() {
        binding.clCreateTask.visibility = View.INVISIBLE
        binding.lottiCreateTaskLoading.visibility = View.VISIBLE
        binding.lottiCreateTaskLoading.playAnimation()
    }

    private fun handleSuccess(taskId: String, todoBody: String, todoDesc: String) {
        taskImage = null
        if (taskTimeStamp?.compareWithToday() == IS_AFTER)
            taskTimeStamp?.let {
                startAlarmedNotification(taskId, todoBody, todoDesc, it.toLong(), alarmManager)
            }
        binding.lottiCreateTaskLoading.cancelAnimation()
        findNavController().navigate(R.id.action_taskCreateFragment_to_taskFragment)
    }

    private fun handleFailure() {
        binding.lottiCreateTaskLoading.cancelAnimation()
        showToast(requireContext(), "Something went wrong")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            taskImage = data.data
            isImageAdded = true
            binding.layoutTaskImage.apply {
                clImagePlaceHolder.visibility = View.VISIBLE
                imgPhoto.loadImageCircleCropped(taskImage.toString())
            }
        }
    }

    companion object {
        var taskImage: Uri? = null
        var assigneeId: String? = null
    }
}
