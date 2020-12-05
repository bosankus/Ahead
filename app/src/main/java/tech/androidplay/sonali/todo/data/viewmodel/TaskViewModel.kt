package tech.androidplay.sonali.todo.data.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.firebase.FirebaseRepository
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 1:39 AM
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TaskViewModel @ViewModelInject constructor(
    firebaseAuth: FirebaseAuth,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val currentUser = firebaseAuth.currentUser

    fun createTask(
        todoBody: String,
        todoDesc: String,
        todoDate: String?,
        uri: Uri?
    ): LiveData<ResultData<String>> {
        val taskMap = hashMapOf(
            "id" to currentUser?.uid,
            "todoBody" to todoBody,
            "todoDesc" to todoDesc,
            "todoDate" to todoDate,
            "todoCreationTimeStamp" to getCurrentTimestamp(),
            "isCompleted" to false
        )
        uri?.let {
            return liveData {
                emit(ResultData.Loading)
                emit(firebaseRepository.createTaskWithImage(taskMap, it))
            }
        } ?: return liveData {
            emit(ResultData.Loading)
            emit(firebaseRepository.createTaskWithoutImage(taskMap))
        }
    }

    fun fetchTasksRealtime(): MutableLiveData<ResultData<MutableList<Todo>>> {
        val response = MutableLiveData<ResultData<MutableList<Todo>>>()
        viewModelScope.launch {
            firebaseRepository.fetchTaskRealtime().collect {
                response.value = it
            }
        }
        return response
    }

    fun changeTaskStatus(taskId: String, status: Boolean) {
        val map: Map<String, Boolean> = mapOf("isCompleted" to status)
        viewModelScope.launch { firebaseRepository.updateTask(taskId, map) }
    }

    fun updateTask(
        taskId: String, todoBody: String? = "", todoDesc: String? = "", todoDate: String? = ""
    ) {
        val map: Map<String, Any?> = mapOf(
            "todoBody" to todoBody,
            "todoDesc" to todoDesc,
            "todoDate" to todoDate,
            "updatedOn" to getCurrentTimestamp()
        )
        viewModelScope.launch { firebaseRepository.updateTask(taskId, map) }
    }

    fun uploadImage(uri: Uri?, taskId: String) = uri?.let {
        logMessage("$it.")
        liveData {
            emit(ResultData.Loading)
            emit(firebaseRepository.uploadImage(uri, taskId))
        }
    }!!


    fun deleteTask(docId: String?) =
        docId?.let { viewModelScope.launch { firebaseRepository.deleteTask(docId) } }

    fun provideFeedback(topic: String, description: String): LiveData<ResultData<String>> {
        val hashMap = hashMapOf(
            "user" to currentUser?.email,
            "topic" to topic,
            "description" to description
        )
        return liveData {
            emit(ResultData.Loading)
            emit(firebaseRepository.provideFeedback(hashMap))
        }
    }
}

