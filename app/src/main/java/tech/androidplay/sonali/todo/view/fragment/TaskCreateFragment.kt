package tech.androidplay.sonali.todo.view.fragment

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
import tech.androidplay.sonali.todo.utils.UIHelper.hideKeyboard
import tech.androidplay.sonali.todo.utils.UIHelper.isEmailValid
import tech.androidplay.sonali.todo.utils.UIHelper.setSvgTint
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
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
    private lateinit var authManager: AuthManager
    private val viewModel: TaskCreateViewModel by viewModels()
    private var taskTimeStamp: String? = null

    private var isAssigneeShowing: Boolean = false
    private var isImageAdded: Boolean = false

    private var taskImage: Uri? = null
    private var assigneeId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authManager = AuthManager(requireActivity())
        setUpScreen()
        setListeners()
    }

    private fun setUpScreen() {
        binding.layoutTaskInput.tvTaskInput.requestFocus()
    }

    private fun setListeners() {
        binding.layoutSetAlarm.tvSelectDate.setOnClickListener {
            dateTimePicker.openDateTimePicker(requireContext())
        }

        dateTimePicker.epochFormat.observe(viewLifecycleOwner, {
            taskTimeStamp = it.toString()
            binding.layoutSetAlarm.tvSelectDate.text =
                taskTimeStamp?.toLocalDateTime()?.beautifyDateTime()
        })


        binding.layoutCreateTaskFeatures.addImage.setOnClickListener {
            selectImage()
            if (isImageAdded) binding.layoutCreateTaskFeatures.addImage.setSvgTint(R.color.dribblePink)
            else if (!isImageAdded) binding.layoutCreateTaskFeatures.addImage.setSvgTint(R.color.grey3)
        }

        binding.layoutTaskImage.btnImgPhotoRemove.setOnClickListener {
            binding.apply {
                layoutTaskImage.imgPhoto.setImageDrawable(null)
                layoutTaskImage.clImagePlaceHolder.visibility = View.GONE
                layoutCreateTaskFeatures.addImage.setSvgTint(R.color.grey3)
                isImageAdded = false
            }
        }

        binding.layoutCreateTaskFeatures.addUser.setOnClickListener {
            if (isAssigneeShowing)
                showAssigneeOption(binding.layoutCreateTaskFeatures.addUser, false)
            else if (!isAssigneeShowing) {
                showAssigneeOption(binding.layoutCreateTaskFeatures.addUser, true)
                assigneeId = null
            }
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

        binding.btCreateTask.setOnClickListener {
            requireActivity().hideKeyboard()
            if ((binding.layoutTaskInput.tvTaskInput.text.length) <= 0)
                showSnack(requireView(), "Fields can not be empty!")
            else if (taskTimeStamp.isNullOrEmpty())
                showSnack(requireView(), "Please select notification time")
            else if (!isNetworkAvailable()) showSnack(requireView(), "No Internet!")
            else createTask()
        }
    }

    private fun showAssigneeOption(icon: ImageView, show: Boolean) {
        isAssigneeShowing = if (show) {
            binding.apply {
                layoutAssigneeUser.clAssignUser.visibility = View.VISIBLE
                icon.setSvgTint(R.color.dribblePink)
            }
            true
        } else {
            binding.apply {
                layoutAssigneeUser.clAssignUser.visibility = View.GONE
                icon.setSvgTint(R.color.grey3)
            }
            false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkAssigneeAvailability(email: String) {
        viewModel.checkAssigneeAvailability(email).observe(viewLifecycleOwner, { result ->
            result?.let {
                when (it) {
                    is ResultData.Loading -> binding.layoutAssigneeUser.tvAssigneeAvailability.text =
                        "Checking..."
                    is ResultData.Success -> {
                        if (it.data == viewModel.currentUser?.uid)
                            binding.layoutAssigneeUser.tvAssigneeAvailability.text =
                                "⚠️Can not assign task to yourself"
                        else {
                            assigneeId = it.data
                            binding.layoutAssigneeUser.tvAssigneeAvailability.text = " ✔️User added"
                        }
                    }
                    is ResultData.Failed -> {
                        binding.layoutAssigneeUser.tvAssigneeAvailability.text = "❗No user found"
                        assigneeId = null
                    }
                }
            }
        })
    }


    private fun createTask() {
        requireActivity().hideKeyboard()
        val todoBody = binding.layoutTaskInput.tvTaskInput.text.toString().trim()
        val todoDesc = binding.layoutTaskInput.tvTaskDescInput.text.toString().trim()
        val todoDate = taskTimeStamp
        val assigneeList = arrayOf(assigneeId)

        // creating task via viewmodel
        if (taskImage == null) {
            viewModel.createTask(todoBody, todoDesc, todoDate, assigneeList)
                .observe(viewLifecycleOwner, {
                    when (it) {
                        is ResultData.Loading -> showSnack(requireView(), "Creating...")
                        is ResultData.Success -> {
                            showSnack(requireView(), "Task Created")
                            findNavController().navigateUp()
                        }
                        is ResultData.Failed -> showSnack(requireView(), "Something went wrong")
                    }
                })
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
        if (resultCode == Activity.RESULT_OK && data != null) {
            taskImage = data.data
            isImageAdded = true
            binding.apply {
                layoutCreateTaskFeatures.addImage.setSvgTint(R.color.grey2)
                layoutTaskImage.clImagePlaceHolder.visibility = View.VISIBLE
                layoutTaskImage.imgPhoto.loadImageCircleCropped(taskImage.toString())
            }
        }
    }
}
