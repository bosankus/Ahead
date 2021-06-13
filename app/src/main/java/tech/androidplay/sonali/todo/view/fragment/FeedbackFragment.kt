package tech.androidplay.sonali.todo.view.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentFeedbackBinding
import tech.androidplay.sonali.todo.utils.AppEventTracking
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.hideKeyboard
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.viewAnimation
import tech.androidplay.sonali.todo.utils.viewLifecycleLazy
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
    private val binding by viewLifecycleLazy { FragmentFeedbackBinding.bind(requireView()) }
    private val viewModel: FeedbackViewModel by viewModels()
    private val animFadeIn by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_animation)
    }
    private val animFadeOut by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out_animation)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }


    private fun setListener() {
        binding.btnProvideFeedback.setOnClickListener {
            requireActivity().hideKeyboard()
            val topic = binding.etFeedbackTopic.text.toString()
            val description = binding.etFeedbackDescription.text.toString()
            if (topic.isEmpty() || description.isEmpty()) showSnack(
                requireView(),
                "Fields can't be empty"
            ) else sendFeedback(topic, description)
        }
    }


    private fun sendFeedback(topic: String, description: String) {
        viewModel.provideFeedback(topic, description).observe(viewLifecycleOwner, { result ->
            result?.let {
                when (it) {
                    is ResultData.Loading -> showLoading()
                    is ResultData.Success -> {
                        appEventTracking.trackFeedbackProvidedEvent()
                        binding.etFeedbackTopic.text = null
                        binding.etFeedbackDescription.text = null
                        hideLoading("Feedback submitted successfully")
                    }
                    is ResultData.Failed -> hideLoading("Oops! Please resubmit")
                }
            }
        })
    }


    private fun showLoading() {
        viewAnimation(binding.btnProvideFeedback, animFadeIn, false)
        viewAnimation(binding.lottieFeedbackLoading, null, true)
    }


    private fun hideLoading(message: String = "") {
        showSnack(requireView(), message)
        viewAnimation(binding.btnProvideFeedback, animFadeOut, true)
        viewAnimation(binding.lottieFeedbackLoading, null, false)
    }
}