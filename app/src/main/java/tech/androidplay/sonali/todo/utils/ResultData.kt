package tech.androidplay.sonali.todo.utils

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 13/Sep/2020
 * Email: ankush@androidplay.in
 */
sealed class ResultData<out T> {
    object Loading : ResultData<Nothing>()
    data class Success<out T>(val data: T? = null) : ResultData<T>()
    data class Failed(val message: String? = null) : ResultData<Nothing>()
}