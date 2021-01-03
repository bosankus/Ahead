package tech.androidplay.sonali.todo.data.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.firebase.FirebaseRepository
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 31/Dec/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
class EditTaskViewModel @ViewModelInject constructor(private val dataSource: FirebaseRepository) :
    ViewModel() {

    fun updateTask(taskId: String, todoBody: String?, todoDesc: String?, todoDate: String?) {
        val map: Map<String, Any?> = mapOf(
            "todoBody" to todoBody,
            "todoDesc" to todoDesc,
            "todoDate" to todoDate,
            "updatedOn" to getCurrentTimestamp()
        )
        viewModelScope.launch { dataSource.updateTask(taskId, map) }
    }

    fun uploadImage(uri: Uri?, taskId: String) = uri?.let {
        liveData {
            emit(ResultData.Loading)
            emit(dataSource.uploadImage(it, taskId))
        }
    }

    fun deleteTask(docId: String?, hasImage: Boolean) = docId?.let {
        liveData {
            emit(ResultData.Loading)
            emit(dataSource.deleteTask(it, hasImage))
        }
    }
}