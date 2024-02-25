package tech.androidplay.sonali.todo.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.R
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 10/Feb/2021
 * Email: ankush@androidplay.in
 */

/** This class helps to authenticate user through [AuthUI.IdpConfig.EmailBuilder] signin */

class AuthManager @Inject constructor(private val auth: FirebaseAuth) {

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

    fun initiateAuthentication(authLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent: Intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.AuthStyle)
            .setTosAndPrivacyPolicyUrls(
                "https://example.com/terms.html",
                "https://example.com/privacy.html",
            )
            .setLogo(R.drawable.ic_intro_logo)
            .build()
        authLauncher.launch(signInIntent)
    }

    fun signOut(context: Context): LiveData<ResultData<Boolean>> =
        liveData { emit(startSignOut(context)) }

    private suspend fun startSignOut(context: Context): ResultData<Boolean> = try {
        AuthUI.getInstance().signOut(context).await()
        ResultData.Success(true)
    } catch (e: Exception) {
        ResultData.Failed(e.message)
    }
}
