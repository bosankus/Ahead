package tech.androidplay.sonali.todo.viewmodel

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.repository.QuoteRepository
import tech.androidplay.sonali.todo.data.repository.TaskRepository
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.Constants.IS_AFTER
import tech.androidplay.sonali.todo.utils.Constants.IS_BEFORE
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
import tech.androidplay.sonali.todo.utils.compareWithToday
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 1:39 AM
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi

class TaskViewModel @ViewModelInject constructor(
    private val taskSource: TaskRepository,
        private val quoteSource: QuoteRepository,
) : ViewModel() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    /*private var _userId = taskSource.userDetails?.uid
    val userId get() = _userId*/

    private var _firstName = MutableLiveData<String>()
    val firstName get() = _firstName

    private var _loadingState = MutableLiveData<Boolean>()
    val loadingState get() = _loadingState

    private var _taskListSize = MutableLiveData<Int>()
    val taskListSize get() = _taskListSize

    private var _assignedTaskList = MutableLiveData<List<Todo>>()
    val assignedTaskList get() = _assignedTaskList

    private var _completedTaskList = MutableLiveData<List<Todo>>()
    val completedTaskList get() = _completedTaskList

    private var _upcomingTaskList = MutableLiveData<List<Todo>>()
    val upcomingTaskList get() = _upcomingTaskList

    private var _overdueTaskList = MutableLiveData<List<Todo>>()
    val overdueTaskList get() = _overdueTaskList

    private var _quoteText = MutableLiveData<String?>()
    val quote get() = _quoteText

    private var _quoteAuthor = MutableLiveData<String?>()
    val author get() = _quoteAuthor

    init {
        _loadingState.value = true
        getUserFirstName()
    }

    private fun getUserFirstName() {
        viewModelScope.launch {
            _firstName.value = "Good Day, ${taskSource.getUserFirstName()}"
            getAllTasks()
            getAllAssignedTasks()
        }
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            try {
                taskSource.fetchAllUnassignedTask().collect { allTodoList ->
                    _taskListSize.value = allTodoList.size
                    if (allTodoList.size == 0) getQuote()
                    _completedTaskList.value =
                        allTodoList.filter { it.isCompleted }.sortedByDescending { it.todoDate }
                    _upcomingTaskList.value =
                        allTodoList.filter { it.todoDate.compareWithToday() == IS_AFTER && !it.isCompleted }
                            .sortedByDescending { it.todoDate }
                    _overdueTaskList.value =
                        allTodoList.filter { it.todoDate.compareWithToday() == IS_BEFORE && !it.isCompleted }
                            .sortedByDescending { it.todoDate }
                    _loadingState.value = false
                }
            } catch (e: Exception) {
                logMessage("TaskViewModel-getAllTasks: ${e.message}")
            }
        }
    }

    private fun getAllAssignedTasks() {
        viewModelScope.launch {
            try {
                taskSource.fetchOnlyAssignedTask().collect { assignedTask ->
                    _assignedTaskList.value = assignedTask.sortedByDescending { it.todoDate }
                    _loadingState.value = false
                }
            } catch (e: Exception) {
                logMessage("TaskViewModel-getAllAssignedTasks: ${e.message}")
                _loadingState.value = false
            }
        }
    }

    private fun getQuote() {
        viewModelScope.launch {
            val todayQuote = quoteSource.fetchQuote()
            _quoteText.value = "${todayQuote.text}"
            _quoteAuthor.value = "- ${todayQuote.author}"
            _loadingState.value = false
        }
    }

    fun logoutUser() = viewModelScope.launch { taskSource.signOut() }

}

