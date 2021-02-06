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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task_create.*
import kotlinx.android.synthetic.main.layout_assign_user.*
import kotlinx.android.synthetic.main.layout_assign_user.view.*
import kotlinx.android.synthetic.main.layout_create_task_features.view.*
import kotlinx.android.synthetic.main.layout_task_app_bar.view.*
import kotlinx.coroutines.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.*
import tech.androidplay.sonali.todo.utils.Constants.IS_AFTER
import tech.androidplay.sonali.todo.utils.UIHelper.hideKeyboard
import tech.androidplay.sonali.todo.utils.UIHelper.isEmailValid
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.alarmutils.startAlarmedNotification
import tech.androidplay.sonali.todo.viewmodel.TaskCreateViewModel
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

    private val viewModel: TaskCreateViewModel by viewModels()
    private var taskTimeStamp: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
        setListeners()
    }

    private fun setUpScreen() {
        layoutTaskBar.tvUserFName.text = "Create Task"
        layoutTaskBar.imgCreate.visibility = View.GONE
        layoutTaskBar.imgMenu.visibility = View.GONE
        tvTaskInput.requestFocus()
    }

    private fun setListeners() {
        layoutCreateTaskFeatures.addImage.setOnClickListener { selectImage() }

        tvSelectDate.setOnClickListener { dateTimePicker.openDateTimePicker(requireContext()) }
        dateTimePicker.epochFormat.observe(viewLifecycleOwner, {
            taskTimeStamp = it.toString()
            tvSelectDate.text = taskTimeStamp?.toLocalDateTime()?.beautifyDateTime()
        })

        btCreateTask.setOnClickListener {
            requireActivity().hideKeyboard()
            if ((tvTaskInput.text.length) <= 0)
                showSnack(requireView(), "Fields can not be empty!")
            else if (taskTimeStamp.isNullOrEmpty())
                showSnack(requireView(), "Please select notification time")
            else if (!isNetworkAvailable()) showSnack(requireView(), "No Internet!")
            else createTask()
        }

        layoutAssigneeUser.etAssigneeUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?, p1: Int, p2: Int, p3: Int
            ) {
            }

            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                charSequence?.let { if (it.isEmailValid()) checkAssigneeAvailability(it.toString()) }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun checkAssigneeAvailability(email: String) {
        viewModel.checkAssigneeAvailability(email).observe(viewLifecycleOwner, { result ->
            result?.let {
                when (it) {
                    is ResultData.Loading -> lottieAvailabilityLoading.visibility = View.VISIBLE
                    is ResultData.Success -> {
                        lottieAvailabilityLoading.visibility = View.INVISIBLE
                        layoutAssigneeUser.tvAssigneeAvailability.text = "User is available"
                        assigneeId = it.data
                    }
                    is ResultData.Failed -> {
                        lottieAvailabilityLoading.visibility = View.INVISIBLE
                        layoutAssigneeUser.tvAssigneeAvailability.text = "No user found"
                        assigneeId = null
                    }
                }
            }
        })
    }


    private fun createTask() {
        val todoBody = tvTaskInput.text.toString().trim()
        val todoDesc = tvTaskDescInput.text.toString().trim()
        val todoDate = taskTimeStamp
        val assignee = assigneeId
        lifecycleScope.launch {
            val compressedImage = taskImage?.compressImage(requireContext())
            withContext(Dispatchers.Main) {
                viewModel.createTask(todoBody, todoDesc, todoDate, assignee, compressedImage)
                    .observe(viewLifecycleOwner, {
                        it?.let {
                            when (it) {
                                is ResultData.Loading -> handleLoading()
                                is ResultData.Success -> handleSuccess(
                                    it.data!!,
                                    todoBody,
                                    todoDesc
                                )
                                is ResultData.Failed -> handleFailure()
                            }
                        }
                    })
            }
        }
    }

    private fun handleLoading() {
        clCreateTask.visibility = View.INVISIBLE
        lottiCreateTaskLoading.visibility = View.VISIBLE
        lottiCreateTaskLoading.playAnimation()
    }

    private fun handleSuccess(taskId: String, todoBody: String, todoDesc: String) {
        // clearing taskImage value
        taskImage = null
        // checking if taskTimeStamp is after the current time
        if (taskTimeStamp?.compareWithToday() == IS_AFTER)
            taskTimeStamp?.let {
                startAlarmedNotification(taskId, todoBody, todoDesc, it.toLong(), alarmManager)
            }
        lottiCreateTaskLoading.cancelAnimation()
        findNavController().navigate(R.id.action_taskCreateFragment_to_taskFragment)
    }

    private fun handleFailure() {
        lottiCreateTaskLoading.cancelAnimation()
        showToast(requireContext(), "Something went wrong")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            taskImage = data.data
            tvSelectImage.text = taskImage.toString()
        }
    }

    companion object {
        var taskImage: Uri? = null
        var assigneeId: String? = null
    }
}
