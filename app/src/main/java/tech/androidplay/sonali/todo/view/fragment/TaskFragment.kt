package tech.androidplay.sonali.todo.view.fragment

import android.annotation.*
import android.app.*
import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.*
import androidx.databinding.*
import androidx.fragment.app.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.*
import tech.androidplay.sonali.todo.utils.*
import tech.androidplay.sonali.todo.utils.Constants.IS_FIRST_TIME
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.view.adapter.main_adapter.*
import tech.androidplay.sonali.todo.view.adapter.viewpager_adapter.*
import tech.androidplay.sonali.todo.viewmodel.*
import javax.inject.*

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

    private var binding: FragmentTaskBinding? = null

    @Inject
    lateinit var dialog: AlertDialog.Builder

    @Inject
    lateinit var assignedTaskAdapter: ViewPagerAdapter

    @Inject
    lateinit var overdueAdapter: TodoAdapter

    @Inject
    lateinit var upcomingAdapter: TodoAdapter

    @Inject
    lateinit var completedAdapter: TodoAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var authManager: AuthManager

    private val viewModel: TaskViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)
        return binding?.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences.edit().putBoolean(IS_FIRST_TIME, false).apply()

        authManager = AuthManager(requireActivity())

        setUpScreen()
        setListeners()
        setObservers()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finishAffinity()
        }
    }

    private fun setUpScreen() {
        binding?.apply {
            layoutTaskHolder.layoutUpcomingTask.rvUpcomingTaskList.adapter = upcomingAdapter
            layoutTaskHolder.layoutOverdueTask.rvOverdueTaskList.adapter = overdueAdapter
            layoutTaskHolder.layoutCompletedTask.rvCompletedTaskList.adapter = completedAdapter
            layoutTaskHolder.layoutAssignedTask.vpAssignedTaskList.apply {
                adapter = assignedTaskAdapter
                offscreenPageLimit = 3
                setPageTransformer(SliderTransformer(3))
            }
        }
    }

    private fun setListeners() {
        binding?.apply {
            layoutTaskBar.imgMenu.setOnClickListener { showPopupMenu(binding?.layoutTaskBar!!.imgMenu) }
            layoutTaskHolder.layoutNoTask.btnAddTask.setOnClickListener { goToCreateTaskFragment() }
            btnCreateTask.setOnClickListener { goToCreateTaskFragment() }
        }
    }

    private fun setObservers() {
        viewModel.assignedTaskList.observe(viewLifecycleOwner) { list ->
            list?.let { assignedTaskAdapter.submitList(it) }
        }

        viewModel.overdueTaskList.observe(viewLifecycleOwner) { list ->
            list?.let { overdueAdapter.submitList(it) }
        }

        viewModel.upcomingTaskList.observe(viewLifecycleOwner) { list ->
            list?.let { upcomingAdapter.submitList(it) }
        }

        viewModel.completedTaskList.observe(viewLifecycleOwner) { list ->
            list?.let { completedAdapter.submitList(it) }
        }
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
                authManager.signOut().observe(viewLifecycleOwner) {
                    if (it is ResultData.Success) {
                        sharedPreferences.edit().putBoolean(IS_FIRST_TIME, true).apply()
                        dialogInterface.dismiss()
                        findNavController().navigate(R.id.action_taskFragment_to_splashFragment)
                    } else if (it is ResultData.Failed) showSnack(requireView(), "Logout failed!")
                }
            }.create().show()
    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}