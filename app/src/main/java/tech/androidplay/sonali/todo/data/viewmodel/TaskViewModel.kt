package tech.androidplay.sonali.todo.data.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.firebase.FirebaseRepository
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.Constants.IS_AFTER
import tech.androidplay.sonali.todo.utils.Constants.IS_BEFORE
import tech.androidplay.sonali.todo.utils.compareWithToday

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 1:39 AM
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TaskViewModel @ViewModelInject constructor(private val dataSource: FirebaseRepository) :
    ViewModel() {

    private var _loadingState = MutableLiveData<Boolean>()
    val loadingState get() = _loadingState

    private var _incompleteTaskListSize = MutableLiveData<Int>()
    val incompleteTaskListSize get() = _incompleteTaskListSize

    private var _completedTaskList = MutableLiveData<List<Todo>>()
    val completedTaskList get() = _completedTaskList

    private var _upcomingTaskList = MutableLiveData<List<Todo>>()
    val upcomingTaskList get() = _upcomingTaskList

    private var _overdueTaskList = MutableLiveData<List<Todo>>()
    val overdueTaskList get() = _overdueTaskList


    init {
        getAllTasks()

    }

    private fun getAllTasks() {
        _loadingState.value = true
        viewModelScope.launch {
            try {
                dataSource.fetchTaskRealtime().collect { allTodoList ->
                    // set value for all incomplete task list size
                    _incompleteTaskListSize.value = allTodoList.filter { !it.isCompleted }.size
                    // set value for all completed tasks
                    _completedTaskList.value =
                        allTodoList.filter { it.isCompleted }.sortedByDescending { it.todoDate }
                    // set value for all incomplete tasks for today
                    _upcomingTaskList.value =
                        allTodoList.filter { it.todoDate.compareWithToday() == IS_AFTER && !it.isCompleted }
                            .sortedByDescending { it.todoDate }
                    // set value for all incomplete tasks which are overdue
                    _overdueTaskList.value =
                        allTodoList.filter { it.todoDate.compareWithToday() == IS_BEFORE && !it.isCompleted }
                            .sortedByDescending { it.todoDate }
                    _loadingState.value = false
                }
            } catch (e: Exception) {
                _loadingState.value = false
            }
        }
    }

    fun changeTaskStatus(taskId: String, status: Boolean) {
        val map: Map<String, Boolean> = mapOf("isCompleted" to status)
        viewModelScope.launch { dataSource.updateTask(taskId, map) }
    }


    fun logoutUser() = viewModelScope.launch {
        dataSource.signOut()
    }

}

