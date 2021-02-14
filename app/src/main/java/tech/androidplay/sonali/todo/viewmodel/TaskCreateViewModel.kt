package tech.androidplay.sonali.todo.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.data.repository.TaskRepository
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 31/Dec/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
class TaskCreateViewModel @ViewModelInject constructor(private val taskSource: TaskRepository) :
    ViewModel() {

    private val _currentUser = taskSource.userDetails
    val currentUser get() = _currentUser

    fun createTask(
        todoBody: String,
        todoDesc: String,
        todoDate: String?,
        assignee: String?,
        uri: Uri?
    ): LiveData<ResultData<String>> {

        val taskMap = hashMapOf(
            "creator" to _currentUser?.uid,
            "todoBody" to todoBody,
            "todoDesc" to todoDesc,
            "todoDate" to todoDate,
            "todoCreationTimeStamp" to UIHelper.getCurrentTimestamp(),
            "isCompleted" to false,
            "assignee" to arrayListOf(assignee)
        )

        return liveData {
            emit(ResultData.Loading)
            emit(taskSource.createTask(taskMap, assignee, uri))
        }
    }

    fun checkAssigneeAvailability(email: String) = liveData {
        emit(ResultData.Loading)
        emit(taskSource.checkAssigneeAvailability(email))
    }
}