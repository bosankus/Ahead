package tech.androidplay.sonali.todo.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.data.repository.FirebaseRepository
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 31/Dec/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
class TaskCreateViewModel @ViewModelInject constructor(
    firebaseAuth: FirebaseAuth,
    private val dataSource: FirebaseRepository
) : ViewModel() {

    private val currentUser = firebaseAuth.currentUser

    fun createTask(
        todoBody: String,
        todoDesc: String,
        todoDate: String?,
        assignee: String?,
        uri: Uri?
    ): LiveData<ResultData<String>> {

        val taskMap = hashMapOf(
            "creator" to currentUser?.uid,
            "todoBody" to todoBody,
            "todoDesc" to todoDesc,
            "todoDate" to todoDate,
            "todoCreationTimeStamp" to UIHelper.getCurrentTimestamp(),
            "isCompleted" to false,
            "assignee" to arrayListOf(assignee)
        )

        return liveData {
            emit(ResultData.Loading)
            emit(dataSource.createTask(taskMap, assignee, uri))
        }
    }

    fun checkAssigneeAvailability(email: String) = liveData {
        emit(ResultData.Loading)
        emit(dataSource.checkAssigneeAvailability(email))
    }
}