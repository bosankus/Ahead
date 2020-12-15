package tech.androidplay.sonali.todo.ui.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.AuthViewModel
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.databinding.FragmentTaskBinding
import tech.androidplay.sonali.todo.ui.adapter.TodoAdapter
import tech.androidplay.sonali.todo.utils.Extensions.shareApp
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.viewLifecycleLazy
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Sep/2020
 * Email: ankush@androidplay.in
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@SuppressLint("SetTextI18n")
class TaskFragment : Fragment(R.layout.fragment_task) {

    private val binding by viewLifecycleLazy { FragmentTaskBinding.bind(requireView()) }

    @Inject
    lateinit var overdueTodoAdapter: TodoAdapter

    @Inject
    lateinit var todayTodoAdapter: TodoAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val taskViewModel: TaskViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var showFab: Animation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
        setListeners()
        setObservers()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finishAffinity()
        }
    }

    private fun setUpScreen() {
        binding.viewmodel = taskViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.layoutTaskBar.tvTaskHeader.text = "Tasks"
        showFab = AnimationUtils.loadAnimation(requireContext(), R.anim.btn_up_animation)
        binding.efabAddTask.startAnimation(showFab)
    }

    private fun setListeners() {
        binding.layoutTaskBar.imgMenu.setOnClickListener {
            showPopupMenu(binding.layoutTaskBar.imgMenu)
        }

        binding.efabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_taskCreateFragment)
        }
    }

    private fun setObservers() {
        binding.layoutTodayTask.rvTodayList.adapter = todayTodoAdapter
        binding.layoutOverdueTask.rvOverdueList.adapter = overdueTodoAdapter
        taskViewModel.todayTaskList.observe(viewLifecycleOwner, {
            todayTodoAdapter.submitList(it)
        })

        taskViewModel.overdueTaskList.observe(viewLifecycleOwner, {
            overdueTodoAdapter.submitList(it)
        })
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.fragment_task_menu, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_logout -> logOutUser()
                R.id.menu_share_app -> shareApp()
                R.id.menu_feedback ->
                    findNavController().navigate(R.id.action_taskFragment_to_feedbackFragment)
                R.id.menu_task_history ->
                    findNavController().navigate(R.id.action_taskFragment_to_taskCompletedFragment)
            }
            true
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun logOutUser() {
        sharedPreferences.edit().clear()
        authViewModel.logoutUser()
        showToast(requireContext(), "You are logged out.")
        findNavController().navigate(R.id.action_taskFragment_to_authFragment)
    }

}