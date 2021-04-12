package tech.androidplay.sonali.todo.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.LayoutPrioritySelectionDialogBinding
import tech.androidplay.sonali.todo.utils.Constants.LOW_PRIORITY
import tech.androidplay.sonali.todo.utils.Constants.MEDIUM_PRIORITY
import tech.androidplay.sonali.todo.utils.Constants.TOP_PRIORITY
import tech.androidplay.sonali.todo.viewmodel.TaskCreateViewModel

@ExperimentalCoroutinesApi
class PrioritySelectionDialog : BottomSheetDialogFragment() {

    private var binding: LayoutPrioritySelectionDialogBinding? = null
    private val viewModel: TaskCreateViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_priority_selection_dialog,
            container,
            false
        )
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }


    private fun setListeners() {
        binding?.apply {
            tvPriorityHeader.setOnClickListener { dismiss() }

            tvTopPriority.setOnClickListener {
                viewModel.taskPriority.value = TOP_PRIORITY
                dismiss()
            }

            tvMediumPriority.setOnClickListener {
                viewModel.taskPriority.value = MEDIUM_PRIORITY
                dismiss()
            }

            tvLowPriority.setOnClickListener {
                viewModel.taskPriority.value = LOW_PRIORITY
                dismiss()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}