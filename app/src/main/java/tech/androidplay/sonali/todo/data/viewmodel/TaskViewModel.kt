package tech.androidplay.sonali.todo.data.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.firestore.DocumentSnapshot
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.repository.TaskRepository
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 1:39 AM
 */
class TaskViewModel @ViewModelInject constructor(private val taskRepository: TaskRepository) :
    ViewModel() {

    var completeTaskLiveData: MutableLiveData<Boolean> = MutableLiveData()


    val todo = Todo()
    var taskStatus: Boolean = todo.isCompleted

    fun createTask(todoName: String, todoDesc: String): LiveData<ResultData<Boolean>> {
        return liveData {
            emit(ResultData.Loading)
            emit(taskRepository.create(todoName, todoDesc))
        }
    }

    fun fetchTask(): LiveData<ResultData<MutableList<Todo>>> {
        return liveData {
            emit(ResultData.Loading)
            emit(taskRepository.fetchTasks())
        }
    }


    fun completeTask(taskId: String, status: Boolean){
        /*taskStatus = status
        completeTaskLiveData = taskRepository.completeTask(taskId, status)
        return completeTaskLiveData*/
    }

    fun uploadImage(uri: Uri) {
        taskRepository.uploadImage(uri)
    }

}