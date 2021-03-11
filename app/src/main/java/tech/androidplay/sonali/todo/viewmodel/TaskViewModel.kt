package tech.androidplay.sonali.todo.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.repository.QuoteRepository
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.Constants.IS_AFTER
import tech.androidplay.sonali.todo.utils.Constants.IS_BEFORE
import tech.androidplay.sonali.todo.utils.Constants.QUOTE
import tech.androidplay.sonali.todo.utils.Constants.QUOTE_AUTHOR
import tech.androidplay.sonali.todo.utils.compareWithToday
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 1:39 AM
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskSource: TodoRepository,
    private val quoteSource: QuoteRepository,
    private val preferences: SharedPreferences
) : ViewModel() {

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
            val firstName = taskSource.userDetails?.displayName
                ?.split(" ")
                ?.toMutableList()
                ?.firstOrNull()
            _firstName.value = "Good Day, $firstName"
            getAllTasks()
        }
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            try {
                taskSource.fetchAllUnassignedTask().collect { allTodoList ->
                    if (allTodoList.size != 0) {
                        _taskListSize.value = allTodoList.filter { !it.isCompleted }.size
                        _completedTaskList.value = getCompletedTaskList(allTodoList)
                        _upcomingTaskList.value = getUpcomingTaskList(allTodoList)
                        _overdueTaskList.value = getOverDueTaskList(allTodoList)
                    }
                    getAllAssignedTasks()
                }
            } catch (e: Exception) {
                _loadingState.value = false
            }
        }
    }

    private fun getAllAssignedTasks() {
        viewModelScope.launch {
            try {
                taskSource.fetchOnlyAssignedTask().collect { assignedTask ->
                    assignedTaskList.value = assignedTask
                    if (assignedTask.size != 0) {
                        getAssignedTaskList(assignedTask)
                        _loadingState.value = false
                    } else getQuote()
                }
            } catch (e: Exception) {
                _loadingState.value = false
            }
        }
    }

    private fun getQuote() {
        val quote = preferences.getString(QUOTE, "")
        if (quote?.isNotBlank() == true) {
            _quoteText.value = quote
            _quoteAuthor.value = preferences.getString(QUOTE_AUTHOR, "")
            _loadingState.value = false
        } else {
            viewModelScope.launch {
                val todayQuote = quoteSource.fetchQuote()
                _quoteText.value = "${todayQuote.text}"
                _quoteAuthor.value = "- ${todayQuote.author}"
                _loadingState.value = false
            }
        }
    }

    private fun getCompletedTaskList(list: List<Todo>) = list.filter { it.isCompleted }
        .sortedWith(compareByDescending(nullsLast(), { it.todoDate }))

    private fun getUpcomingTaskList(list: List<Todo>) =
        list.filter { it.todoDate?.compareWithToday() == IS_AFTER && !it.isCompleted }
            .sortedWith(compareByDescending(nullsLast(), { it.todoDate }))


    private fun getOverDueTaskList(list: List<Todo>) =
        list.filter { it.todoDate?.compareWithToday() == IS_BEFORE && !it.isCompleted }
            .sortedWith(compareByDescending(nullsLast(), { it.todoDate }))

    private fun getAssignedTaskList(list: List<Todo>) =
        list.sortedWith(compareByDescending(nullsLast(), { it.todoDate }))

}