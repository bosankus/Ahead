package tech.androidplay.sonali.todo.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.data.repository.AuthRepository
import tech.androidplay.sonali.todo.utils.ResultData
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:51 AM
 */

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class AuthViewModel @ViewModelInject constructor(private val dataSource: AuthRepository) :
    ViewModel() {

    fun createAccount(fName: String, lName: String, email: String, password: String):
            LiveData<ResultData<FirebaseUser>> {
        return liveData {
            emit(ResultData.Loading)
            if (email.isEmpty() || password.isEmpty())
                emit(ResultData.Failed("Please check your input"))
            else if (password.isNotEmpty() && password.length < 6)
                emit(ResultData.Failed("Password can't be less than 6 characters"))
            else if (fName.isEmpty()) emit(ResultData.Failed("Your first name can't be empty"))
            else if (lName.isEmpty()) emit(ResultData.Failed("Your last name can't be empty"))
            else emit(dataSource.createAccount(email, password, fName, lName))
        }
    }

    fun loginUser(email: String, password: String): LiveData<ResultData<FirebaseUser>> {
        return liveData {
            emit(ResultData.Loading)
            if (email.isEmpty() || password.isEmpty() || password.length < 6) {
                emit(ResultData.Failed("Please check your input"))
            } else emit(dataSource.logInUser(email, password))
        }
    }

    fun resetPassword(email: String): LiveData<ResultData<String>> {
        return liveData {
            emit(ResultData.Loading)
            if (email.isEmpty()) {
                emit(ResultData.Failed("Please provide your email ID"))
            } else emit(dataSource.resetPassword(email))
        }
    }

    /*@dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(dataSource: AuthRepository): AuthViewModel
    }

    companion object {
        fun provideFactory(assistedFactory: AssistedFactory, dataSource: AuthRepository):
                ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(dataSource) as T
            }

        }
    }*/
}