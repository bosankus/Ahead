package tech.androidplay.sonali.todo.data.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.data.firebase.DataRepository
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
class TaskViewModel @ViewModelInject constructor(
    private val firebaseSource: FirebaseRepository,
    private val dataSource: DataRepository,
    private val messaging: FirebaseMessaging
) :
    AndroidViewModel(Application()) {

    private var _userId = firebaseSource.userDetails?.uid
    val userId get() = _userId

    private var _loadingState = MutableLiveData<Boolean>()
    val loadingState get() = _loadingState

    private var _taskListSize = MutableLiveData<Int>()
    val taskListSize get() = _taskListSize

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
        getQuote()
        getAllTasks()
        updateDeviceToken()
    }

    private fun getQuote() {
        _loadingState.value = true
        viewModelScope.launch {
            val todayQuote = dataSource.fetchQuote()
            _quoteText.value = todayQuote.text
            _quoteAuthor.value = "- ${todayQuote.author}"
        }
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            try {
                firebaseSource.fetchTaskRealtime().collect { allTodoList ->
                    // set value for all incomplete task list size
                    _taskListSize.value = allTodoList.size
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
        viewModelScope.launch { firebaseSource.updateTask(taskId, map) }
    }


    fun logoutUser() = viewModelScope.launch {
        firebaseSource.signOut()
    }

    private fun updateDeviceToken() {
        viewModelScope.launch {
            val token = messaging.token.await()
            firebaseSource.sendTokenToSever(token)
        }
    }
}

