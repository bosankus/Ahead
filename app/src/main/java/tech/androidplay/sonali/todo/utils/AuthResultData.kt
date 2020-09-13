package tech.androidplay.sonali.todo.utils

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 13/Sep/2020
 * Email: ankush@androidplay.in
 */
sealed class AuthResultData<out T> {
    object Loading : AuthResultData<Nothing>()
    data class Success<out T>(val data: T? = null) : AuthResultData<T>()
    data class Failed(val message: String? = null) : AuthResultData<Nothing>()
    data class Exception(val message: String? = null) : AuthResultData<Nothing>()
}