package tech.androidplay.sonali.todo.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentFeedbackBinding
import tech.androidplay.sonali.todo.utils.AppEventTracking
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.hideKeyboard
import tech.androidplay.sonali.todo.viewmodel.FeedbackViewModel
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 28/Nov/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class FeedbackFragment : Fragment(R.layout.fragment_feedback) {

    @Inject
    lateinit var appEventTracking: AppEventTracking
    private val viewModel: FeedbackViewModel by viewModels()
    private var binding: FragmentFeedbackBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feedback, container, false)
        return binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnBack?.setOnClickListener { findNavController().navigateUp() }

        viewModel.feedbackLiveResponse.observe(viewLifecycleOwner, { resultData ->
            if (resultData != ResultData.DoNothing) hideKeyboard()
            if (resultData is ResultData.Success) {
                binding?.etFeedbackDescription?.setText("")
                binding?.etFeedbackTopic?.setText("")
                findNavController().navigateUp()
            }
        })
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}