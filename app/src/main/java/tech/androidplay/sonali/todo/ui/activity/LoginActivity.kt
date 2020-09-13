package tech.androidplay.sonali.todo.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.AuthViewModel
import tech.androidplay.sonali.todo.utils.AuthResultData
import tech.androidplay.sonali.todo.utils.CacheManager
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

    // for accident control
    private var networkFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // enable white status bar with black icons
        setScreenUI()

        // turning listeners on
        clickListeners()
    }

    private fun setScreenUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
    }

    private fun clickListeners() {
        btnSignUpEmailPassword.setOnClickListener { signUpUser() }

        btnloginEmailPassword.setOnClickListener { login() }

        tvForgotPassword.setOnClickListener { sendPasswordResetEmail() }

        tvSignUpOption.setOnClickListener { setSignUpUI() }

        tvLoginOption.setOnClickListener { setLoginUi() }
    }

    private fun signUpUser() {
        val userEmail = loginInputEmailTxt.text.toString()
        val userPassword = loginInputPasswordTxt.text.toString()
        if (validateInput(userEmail, userPassword)) {
            authViewModel.createAccount(userEmail, userPassword).observe(
                this,
                {
                    it?.let {
                        when (it) {
                            is AuthResultData.Loading -> showLoading()
                            is AuthResultData.Success -> goToMainActivity()
                            is AuthResultData.Failed -> hideLoading(it.message.toString())
                        }
                    }
                })
        } else showToast(this, "Please recheck your inputs")
    }

    private fun login() {
        val userEmail = loginInputEmailTxt.text.toString()
        val userPassword = loginInputPasswordTxt.text.toString()
        if (validateInput(userEmail, userPassword)) {
            networkFlag = true
            authViewModel.loginUser(userEmail, userPassword).observe(
                this,
                {
                    it?.let {
                        when (it) {
                            is AuthResultData.Loading -> showLoading()
                            is AuthResultData.Success -> goToMainActivity()
                            is AuthResultData.Failed -> hideLoading(it.message.toString())
                        }
                    }
                    networkFlag = false
                }
            )
        } else showToast(this, "Please recheck your inputs")
    }

    private fun sendPasswordResetEmail() {
        val userEmail = loginInputEmailTxt.text.toString()
        if (validateInput(email = userEmail)) {
            authViewModel.resetPassword(userEmail)
            showToast(
                this, "You will receive password " +
                        "reset mail if you are registered with us"
            )
        }
    }

    private fun showLoading() {
        viewAnimation(btnloginEmailPassword, animFadeIn, false)
        viewAnimation(lottieAuthLoading, null, true)
    }

    private fun hideLoading(message: String = "") {
        showToast(this, message)
        viewAnimation(btnloginEmailPassword, animFadeOut, true)
        viewAnimation(lottieAuthLoading, null, false)
    }

    private fun goToMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        overridePendingTransition(
            R.anim.fade_out_animation,
            R.anim.fade_in_animation
        )
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

    private fun validateInput(email: String = "", password: String = ""): Boolean {
        var valid = true

        if (TextUtils.isEmpty(email)) {
            loginInputEmailTxt.error = "Required"
            valid = false
        } else loginEmailTxtLayout.error = null

        if (TextUtils.isEmpty(password)) {
            loginInputPasswordTxt.error = "Required"
            valid = false
        }

        if (password.length < 6) {
            loginInputPasswordTxt.error = "Minimum 6 characters"
            valid = false
        } else loginInputPasswordTxt.error = null

        return valid
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        cache.clearCache(this)
    }
}
