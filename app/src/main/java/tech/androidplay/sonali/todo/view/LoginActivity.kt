package tech.androidplay.sonali.todo.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.AuthViewModel
import tech.androidplay.sonali.todo.utils.Constants
import tech.androidplay.sonali.todo.utils.Helper.showToast

class LoginActivity : AppCompatActivity(), Constants {

    private lateinit var authViewModel: AuthViewModel

    // Firebase
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentUser = firebaseAuth.currentUser

    // Animation
    private lateinit var animFadeIn: Animation
    private lateinit var animFadeOut: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // enable white status bar with black icons
        setScreenUI()

        // initiating viewodel
        initAuthViewModel()

        // turning listeners on
        clickListeners()

    }


    private fun setScreenUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
    }

    private fun initAuthViewModel() {
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    private fun clickListeners() {
        btnSignUpEmailPassword.setOnClickListener { signUpUser() }

        btnloginEmailPassword.setOnClickListener { loginUser() }

        btnGoogleSignIn.setOnClickListener {
            showToast(this, "Not yet implemented")
        }

        tvSignUpOption.setOnClickListener { setSignUpUI() }

        tvLoginOption.setOnClickListener { setLoginUi() }
    }

    private fun setSignUpUI() {
        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation)
        animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_animation)

        btnloginEmailPassword.startAnimation(animFadeIn)
        btnloginEmailPassword.visibility = View.INVISIBLE
        tvSignUpOption.startAnimation(animFadeIn)
        tvSignUpOption.visibility = View.INVISIBLE

        btnSignUpEmailPassword.startAnimation(animFadeOut)
        btnSignUpEmailPassword.visibility = View.VISIBLE
        tvLoginOption.startAnimation(animFadeOut)
        tvLoginOption.visibility = View.VISIBLE
    }

    private fun setLoginUi() {
        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation)
        animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_animation)

        btnSignUpEmailPassword.startAnimation(animFadeIn)
        btnSignUpEmailPassword.visibility = View.INVISIBLE
        tvLoginOption.startAnimation(animFadeIn)
        tvLoginOption.visibility = View.INVISIBLE

        btnloginEmailPassword.startAnimation(animFadeOut)
        btnloginEmailPassword.visibility = View.VISIBLE
        tvSignUpOption.startAnimation(animFadeOut)
        tvSignUpOption.visibility = View.VISIBLE
    }

    private fun signUpUser() {
        if (validateInput()) {
            animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation)
            btnSignUpEmailPassword.startAnimation(animFadeIn)
            btnSignUpEmailPassword.visibility = View.INVISIBLE
            lottieAuthLoading.visibility = View.VISIBLE
            authViewModel.createAccountWithEmailPassword(
                loginInputEmailTxt.text.toString(),
                loginInputEmailTxt.text.toString()
            )
            authViewModel.createAccountMutableLiveData.observe(
                this,
                Observer {
                    if (it == 1) {
                        // Account successfully created
                        goToMainActivity()
                    } else if (it == 0) showToast(this, "Something went wrong. Please retry.")
                })
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        authViewModel.sendPasswordResetEmail(
            loginInputEmailTxt.text.toString()
        )
        authViewModel.passwordResetLiveData.observe(
            this,
            Observer {
                if (it.isNotEmpty()) {
                    showToast(this, "Password reset email is sent to $it")
                } else showToast(this, "Invalid email id")
            }
        )
    }

    private fun loginUser() {
        if (validateInput()) {
            animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation)
            btnloginEmailPassword.startAnimation(animFadeIn)
            btnloginEmailPassword.visibility = View.INVISIBLE
            lottieAuthLoading.visibility = View.VISIBLE
            authViewModel.loginWithEmailPassword(
                loginInputEmailTxt.text.toString(),
                loginInputEmailTxt.text.toString()
            )
            authViewModel.loginLiveData.observe(
                this,
                Observer {
                    if (it == 1) {
                        // Successfully Logged in
                        goToMainActivity()
                    } else if (it == 0) showToast(this, "Something went wrong. Please retry.")
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


    // validate user input in text fields
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

}
