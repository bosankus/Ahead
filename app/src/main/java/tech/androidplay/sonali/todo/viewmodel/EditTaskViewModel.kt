package tech.androidplay.sonali.todo.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.repository.TaskRepository
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 31/Dec/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
class EditTaskViewModel @ViewModelInject constructor(private val taskSource: TaskRepository) :
    ViewModel() {

    private var _userId = MutableLiveData<String>()
    val userId get() = _userId

    fun getTaskByTaskId(taskId: String) = liveData {
        emit(ResultData.Loading)
        emit(taskSource.fetchTaskByTaskId(taskId))
    }

    fun updateTask(taskId: String, todoBody: String?, todoDesc: String?, todoDate: String?) {
        val map: Map<String, Any?> = mapOf(
            "todoBody" to todoBody,
            "todoDesc" to todoDesc,
            "todoDate" to todoDate,
            "updatedOn" to getCurrentTimestamp()
        )
        viewModelScope.launch { taskSource.updateTask(taskId, map) }
    }

    fun uploadImage(uri: Uri?, taskId: String) = uri?.let {
        liveData {
            emit(ResultData.Loading)
            emit(taskSource.uploadImage(it, null, taskId))
        }
    }

    fun deleteTask(docId: String?, hasImage: Boolean) = docId?.let {
        liveData {
            emit(ResultData.Loading)
            emit(taskSource.deleteTask(it, hasImage))
        }
    }
}