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
import androidx.recyclerview.widget.RecyclerView
import com.iammert.library.ui.multisearchviewlib.MultiSearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.AuthViewModel
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.ui.adapter.TodoAdapter
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
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
class TaskFragment : Fragment(R.layout.fragment_task) {

    @Inject
    lateinit var todoAdapter: TodoAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val taskViewModel: TaskViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var showFab: Animation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
        setListeners()
        loadTasks()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finishAffinity()
        }
    }

    private fun setUpScreen() {
        showFab = AnimationUtils.loadAnimation(requireContext(), R.anim.btn_up_animation)
        efabAddTask.startAnimation(showFab)
    }

    private fun setListeners() {

        efabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_taskCreateFragment)
        }

        rvTodoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) efabAddTask.hide()
                else if (dy < 0) efabAddTask.show()
            }
        })

        searchTask.setSearchViewListener(object : MultiSearchView.MultiSearchViewListener {
            override fun onItemSelected(index: Int, s: CharSequence) {
                todoAdapter.filterList(s)
            }

            override fun onSearchComplete(index: Int, s: CharSequence) {
                todoAdapter.filterList(s)
            }

            override fun onSearchItemRemoved(index: Int) {
                loadTasks()
            }

            override fun onTextChanged(index: Int, s: CharSequence) {
                todoAdapter.filterList(s)
            }
        })

        imgAllTaskMenu.setOnClickListener { showPopupMenu(imgAllTaskMenu) }
    }

    private fun loadTasks() {
        rvTodoList.apply {
            adapter = todoAdapter
        }
        taskViewModel.fetchTasksRealtime().observe(viewLifecycleOwner, {
            it.let {
                when (it) {
                    is ResultData.Loading -> shimmerFrameLayout.visibility = View.VISIBLE
                    is ResultData.Success -> {
                        shimmerFrameLayout.visibility = View.GONE
                        frameNoTodo.visibility = View.GONE
                        it.data?.let { list -> todoAdapter.modifyList(list) }
                    }
                    is ResultData.Failed -> {
                        shimmerFrameLayout.visibility = View.GONE
                        frameNoTodo.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.fragment_task_menu, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_logout -> {
                    showToast(requireContext(), "You are logged out.")
                    logOutUser()
                }
                R.id.menu_share_app -> {}
            }
            true
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun logOutUser() {
        sharedPreferences.edit().clear()
        authViewModel.logoutUser()
        findNavController().navigate(R.id.action_taskFragment_to_authFragment)
    }
}