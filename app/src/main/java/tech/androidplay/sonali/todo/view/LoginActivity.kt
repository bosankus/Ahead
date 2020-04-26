package tech.androidplay.sonali.todo.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.User
import tech.androidplay.sonali.todo.data.viewmodel.AuthViewModel
import tech.androidplay.sonali.todo.utils.Constants
import tech.androidplay.sonali.todo.utils.Helper

class LoginActivity : AppCompatActivity(), Constants {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var helper: Helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // enable white status bar with black icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        firebaseAuth = FirebaseAuth.getInstance()
        helper = Helper()
        initAuthViewModel()
        clickListeners()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        updateUI(currentUser)
    }

    private fun clickListeners() {

        loginBtnEmailPassword.setOnClickListener {
            if (validateInput()) {
                authViewModel.createAccountWithEmailPassword(
                    loginInputEmailTxt.text.toString(),
                    loginInputEmailTxt.text.toString()
                )
                authViewModel.authenticatedUserLiveData.observe(
                    this,
                    Observer { authenticatedUser ->
                        if (authenticatedUser.isCreated) {
                            helper.logErrorMessage("Done")
                        }
                    })
            }
        }
    }

    private fun initAuthViewModel() {
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    private fun createNewUser(authenticatedUser: User) {
        authViewModel.createUser(authenticatedUser)
        authViewModel.createdUserLiveData.observe(this, Observer {
            if (it.isCreated) {
                helper.logErrorMessage(authenticatedUser.userId)
            }
        })
    }

    private fun validateInput(): Boolean {
        var valid = true

        if (TextUtils.isEmpty(loginInputEmailTxt.text.toString())) {
            loginInputEmailTxt.error = "Required"
            valid = false
        } else loginEmailTxtLayout.error = null

        if (TextUtils.isEmpty(loginInputPasswordTxt.text.toString())) {
            loginInputPasswordTxt.error = "Required"
            valid = false
        } else loginInputPasswordTxt.error = null

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            loginTxt.text = user.email
            loginBtnEmailPassword.visibility = View.GONE
        } else {
            loginBtnEmailPassword.visibility = View.VISIBLE
        }
    }

}
