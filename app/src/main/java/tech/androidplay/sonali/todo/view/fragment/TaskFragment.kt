package tech.androidplay.sonali.todo.view.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentTaskBinding
import tech.androidplay.sonali.todo.view.adapter.TodoAdapter
import tech.androidplay.sonali.todo.utils.Constants.IS_FIRST_TIME
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.isNetworkAvailable
import tech.androidplay.sonali.todo.utils.shareApp
import tech.androidplay.sonali.todo.utils.viewLifecycleLazy
import tech.androidplay.sonali.todo.viewmodel.TaskViewModel
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
    lateinit var dialog: AlertDialog.Builder

    @Inject
    lateinit var assignedTaskAdapter: TodoAdapter

    @Inject
    lateinit var overdueAdapter: TodoAdapter

    @Inject
    lateinit var upcomingAdapter: TodoAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences.edit().putBoolean(IS_FIRST_TIME, false).apply()
        setUpScreen()
        setListeners()
        setObservers()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finishAffinity()
        }
    }

    private fun setUpScreen() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.layoutAssignedTask.rvAssignedTaskList.adapter = assignedTaskAdapter
        binding.layoutUpcomingTask.rvUpcomingTaskList.adapter = upcomingAdapter
        binding.layoutOverdueTask.rvOverdueTaskList.adapter = overdueAdapter

    }

    private fun setListeners() {
        binding.layoutTaskBar.imgMenu.setOnClickListener { showPopupMenu(binding.layoutTaskBar.imgMenu) }
        binding.layoutTaskBar.imgCreate.setOnClickListener { showCreateMenu(binding.layoutTaskBar.imgCreate) }
        binding.layoutNoTask.btnAddTask.setOnClickListener { goToCreateTaskFragment() }
    }

    private fun setObservers() {
        viewModel.assignedTaskList.observe(viewLifecycleOwner, {
            assignedTaskAdapter.submitList(it)
        })

        viewModel.overdueTaskList.observe(viewLifecycleOwner, {
            overdueAdapter.submitList(it)
        })

        viewModel.upcomingTaskList.observe(viewLifecycleOwner, {
            upcomingAdapter.submitList(it)
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

    private fun showCreateMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_create_options, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_create_task -> goToCreateTaskFragment()
            }
            true
        }
    }

    private fun goToCreateTaskFragment() {
        if (isNetworkAvailable())
            findNavController().navigate(R.id.action_taskFragment_to_taskCreateFragment)
        else showSnack(requireView(), "Check Internet!")
    }

    @SuppressLint("CommitPrefEdits")
    private fun logOutUser() {
        dialog.setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialogInterface, _ ->
                sharedPreferences.edit().putBoolean(IS_FIRST_TIME, true).apply()
                viewModel.logoutUser()
                dialogInterface.dismiss()
                findNavController().navigate(R.id.action_taskFragment_to_splashFragment)
            }.create().show()
    }

}