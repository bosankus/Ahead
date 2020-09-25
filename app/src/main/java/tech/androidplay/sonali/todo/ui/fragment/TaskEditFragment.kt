package tech.androidplay.sonali.todo.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task_edit.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Sep/2020
 * Email: ankush@androidplay.in
 */

@AndroidEntryPoint
class TaskEditFragment : Fragment(R.layout.fragment_task_edit) {

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        fetchTaskDetails()
    }

    private fun setListener() {

    }

    private fun fetchTaskDetails() {
        val taskId = arguments?.getString(TASK_DOC_ID)
        taskId?.let {
            taskViewModel.fetchTaskDetails(taskId).observe(viewLifecycleOwner, {
                it?.let {
                    when (it) {
                        is ResultData.Loading -> {}
                        is ResultData.Success -> {
                            etTaskBody.setText(it.data?.todoBody)
                            etTaskDesc.setText(it.data?.todoDesc)
                        }
                        is ResultData.Failed -> showSnack(requireView(), "Couldn't fetch task")
                    }
                }
            })
        } ?: showSnack(requireView(), "Couldn't fetch task")
    }
}