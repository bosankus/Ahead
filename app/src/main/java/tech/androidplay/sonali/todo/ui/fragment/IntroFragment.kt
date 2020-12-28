package tech.androidplay.sonali.todo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentIntroBinding
import tech.androidplay.sonali.todo.utils.viewLifecycleLazy

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Dec/2020
 * Email: ankush@androidplay.in
 */
class IntroFragment : Fragment(R.layout.fragment_intro) {

    private val binding by viewLifecycleLazy { FragmentIntroBinding.bind(requireView()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finishAffinity()
        }
        binding.layoutIntroScreen.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_authFragment)
        }
    }
}
