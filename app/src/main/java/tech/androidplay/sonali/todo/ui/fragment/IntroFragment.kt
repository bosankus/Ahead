package tech.androidplay.sonali.todo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_intro.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentIntroBinding
import tech.androidplay.sonali.todo.ui.adapter.ViewPagerAdapter
import tech.androidplay.sonali.todo.utils.viewLifecycleLazy

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Dec/2020
 * Email: ankush@androidplay.in
 */
class IntroFragment : Fragment(R.layout.fragment_intro) {

    private val binding by viewLifecycleLazy { FragmentIntroBinding.bind(requireView()) }
    private val introText = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUi()
        setListeners()
    }

    private fun setUi() {
        postToList()
        introScreenViewpager.adapter = ViewPagerAdapter(introText)
        introScreenViewpagerIndicator.setViewPager(introScreenViewpager)
    }

    private fun setListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finishAffinity()
        }
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_authFragment)
        }
    }

    private fun addToList(text: String) = introText.add(text)

    private fun postToList() {
        addToList("Sometimes we forget important things in our life...\uD83D\uDE25 We aren't perfect \uD83D\uDE04")
        addToList("And I am there to make you remember things, which matters to you...\uD83E\uDD17")
        addToList("Click the button below to check me out \uD83E\uDD70")
        addToList("Make life a little better \uD83D\uDE4C")
    }
}
