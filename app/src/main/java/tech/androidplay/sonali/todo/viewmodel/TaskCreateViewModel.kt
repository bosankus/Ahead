package tech.androidplay.sonali.todo.viewmodel

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.BR
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.Constants.LOW_PRIORITY
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp
import tech.androidplay.sonali.todo.utils.UIHelper.isEmailValid
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 31/Dec/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
@HiltViewModel
class TaskCreateViewModel @Inject constructor(
    private val taskSource: TodoRepository
) : ViewModel(), Observable {

    private val registry = PropertyChangeRegistry()

    private val _currentUser = MutableLiveData(taskSource.userDetails)
    val currentUser: LiveData<FirebaseUser?> get() = _currentUser

    private var _emailUnderCheckForAvailability = MutableLiveData<String>()
    val emailUnderCheckForAvailability: LiveData<String> get() = _emailUnderCheckForAvailability

    private var _emailAvailabilityStatus = MutableLiveData<ResultData<String>>(ResultData.DoNothing)
    val emailAvailabilityStatus: LiveData<ResultData<String>> get() = _emailAvailabilityStatus

    private val _taskCreationStatus = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val taskCreationStatus: LiveData<ResultData<*>> get() = _taskCreationStatus

    val taskPriority = MutableLiveData(LOW_PRIORITY)

    @get: Bindable
    var todo = Todo()
        set(value) {
            if (value != field) field = value
            registry.notifyChange(this, BR.todo)
        }


    @get: Bindable
    val todoAssigneeEmailWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            s.let { if (it.isEmailValid()) checkEmailAvailability(it.toString()) }
        }
    }


    private fun checkEmailAvailability(email: String) {
        viewModelScope.launch {
            val response = taskSource.isUserAvailable(email)
            _emailAvailabilityStatus.postValue(response)
            _emailUnderCheckForAvailability.postValue(email)
            if (response is ResultData.Success) {
                todo.assigneeList = arrayListOf(response.data).toList()
            } else todo.assigneeList = null
        }
    }


    fun createTaskModified(item: Todo?) {
        viewModelScope.launch {
            _taskCreationStatus.postValue(ResultData.Loading)
            item?.let {
                it.creator = _currentUser.value?.uid.toString()
                it.priority = taskPriority.value
                it.todoCreationTimeStamp = getCurrentTimestamp()
                when {
                    it.todoBody.isEmpty() ->
                        _taskCreationStatus.postValue(ResultData.Failed("Body can't be empty"))
                    it.todoDate?.isEmpty() == true -> _taskCreationStatus.postValue(
                        ResultData.Failed("Task date can't be empty")
                    )
                    it.taskImage?.isEmpty() == true -> {
                        val response = taskSource.createTasks(it)
                        _taskCreationStatus.postValue(response)
                    }
                    else -> createTaskWithImage(it)
                }
            }
        }
    }


    private fun createTaskWithImage(item: Todo?) {

    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.add(callback)
    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.remove(callback)
    }
}