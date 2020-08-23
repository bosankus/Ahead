package tech.androidplay.sonali.todo.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.AuthViewModel
import tech.androidplay.sonali.todo.utils.CacheManager
import tech.androidplay.sonali.todo.utils.UIHelper.networkFlag
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.UIHelper.viewAnimation
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var cache: CacheManager

    private val authViewModel: AuthViewModel by viewModels()

    // Animation
    private val animFadeIn by lazy {
        AnimationUtils.loadAnimation(this, R.anim.fade_in_animation)
    }
    private val animFadeOut by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.fade_out_animation
        )
    }
    private val userEmail by lazy { loginInputEmailTxt.text.toString() }
    private val userPassword by lazy { loginInputPasswordTxt.text.toString() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // enable white status bar with black icons
        setScreenUI()

        // turning listeners on
        clickListeners()

    }

    private fun clickListeners() {
        btnSignUpEmailPassword.setOnClickListener { signUpUser() }

        btnloginEmailPassword.setOnClickListener { loginUser() }

        tvForgotPassword.setOnClickListener { sendPasswordResetEmail() }

        tvSignUpOption.setOnClickListener { setSignUpUI() }

        tvLoginOption.setOnClickListener { setLoginUi() }
    }

    private fun signUpUser() {
        if (validateInput()) {
            viewAnimation(btnSignUpEmailPassword, animFadeIn, false)
            viewAnimation(lottieAuthLoading, null, true)
            authViewModel.createAccountWithEmailPassword(userEmail, userPassword)
            authViewModel.createAccountLiveData.observe(
                this,
                Observer {
                    when (it) {
                        1 -> goToMainActivity()
                        0 -> {
                            showToast(this, "User already signed up")
                            viewAnimation(btnSignUpEmailPassword, animFadeOut, true)
                            viewAnimation(lottieAuthLoading, null, false)
                        }
                    }
                })
        }
    }

    private fun sendPasswordResetEmail() {
        if (validateInput()) {
            networkFlag = true
            authViewModel.sendPasswordResetEmail(userEmail)
            authViewModel.passwordResetLiveData.observe(
                this,
                Observer {
                    if (it == 1) {
                        networkFlag = false
                        showToast(
                            this,
                            "Password reset email is sent to ${loginInputEmailTxt.text.toString()}"
                        )
                    } else {
                        networkFlag = false
                        showToast(this, "Invalid email id")
                    }
                }
            )
        } else showToast(this, "Email is Empty")
    }

    private fun loginUser() {
        if (validateInput()) {
            networkFlag = true
            viewAnimation(btnloginEmailPassword, animFadeIn, false)
            viewAnimation(lottieAuthLoading, null, true)
            authViewModel.loginWithEmailPassword(userEmail, userPassword)
            authViewModel.loginLiveData.observe(
                this,
                Observer {
                    if (it == 1) {
                        // Successfully Logged in
                        networkFlag = false
                        goToMainActivity()
                    } else if (it == 0) {
                        networkFlag = false
                        showToast(this, "Something went wrong. Please retry.")
                        viewAnimation(btnloginEmailPassword, animFadeOut, true)
                        viewAnimation(lottieAuthLoading, null, false)
                    }
                })
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        overridePendingTransition(
            R.anim.fade_out_animation,
            R.anim.fade_in_animation
        )
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
        }

        if (loginInputPasswordTxt.text.toString().length < 6) {
            loginInputPasswordTxt.error = "Minimum 6 characters"
            valid = false
        } else loginInputPasswordTxt.error = null

        return valid
    }

    private fun setScreenUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
    }

    private fun setSignUpUI() {
        if (networkFlag) {
            showToast(this, "Have Patience")
        } else {
            viewAnimation(btnloginEmailPassword, animFadeIn, false)
            viewAnimation(tvSignUpOption, animFadeIn, false)
            viewAnimation(btnSignUpEmailPassword, animFadeOut, true)
            viewAnimation(tvLoginOption, animFadeOut, true)
        }
    }

    private fun setLoginUi() {
        if (networkFlag) {
            showToast(this, "Have Patience")
        } else {
            viewAnimation(btnSignUpEmailPassword, animFadeIn, false)
            viewAnimation(tvLoginOption, animFadeIn, false)
            viewAnimation(btnloginEmailPassword, animFadeOut, true)
            viewAnimation(tvSignUpOption, animFadeOut, true)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onStop() {
        super.onStop()
        cache.clearCache(this)
    }
}
