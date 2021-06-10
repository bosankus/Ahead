package tech.androidplay.sonali.todo.utils

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.R

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 10/Feb/2021
 * Email: ankush@androidplay.in
 */

/** This class helps to authenticate user through [AuthUI.IdpConfig.EmailBuilder] signin */

class AuthManager(
    private val activity: Activity,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    val isUserLoggedIn: Boolean
        get() {
            return auth.currentUser != null
        }

    val userDetails: FirebaseUser?
        get() {
            return if (isUserLoggedIn) auth.currentUser else null
        }

    /*val userEmail: String?
        get() {
            return if (isUserLoggedIn) auth.currentUser?.email else null
        }

    val userId: String?
        get() {
            return if (isUserLoggedIn) auth.currentUser?.uid else null
        }*/

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    fun authUser() = activity.startActivityForResult(
        // intent
        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.AuthStyle)
            .build(),
        // request code
        RC_SIGN_IN
    )

    fun handleAuth(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        action: (isSuccessful: Boolean, exception: Exception?) -> Unit
    ) {
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) action.invoke(true, null)
            else response?.error?.let { action.invoke(false, it) }
        }

    }

    fun signOut(): LiveData<ResultData<Boolean>> = liveData { emit(startSignOut()) }

    private suspend fun startSignOut(): ResultData<Boolean> = try {
        AuthUI.getInstance().signOut(activity).await()
        ResultData.Success(true)
    } catch (e: Exception) {
        ResultData.Failed(e.message)
    }

    companion object {
        const val RC_SIGN_IN = 123
    }
}