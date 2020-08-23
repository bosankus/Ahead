package tech.androidplay.sonali.todo.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import tech.androidplay.sonali.todo.R

class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth = FirebaseAuth.getInstance()

        // Setting screen UI elements
        setScreenUI()

    }

    private fun setScreenUI() {
        // enable white status bar with black icons
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (!hasFocus) {
            return
        }
        checkAuth()
        super.onWindowFocusChanged(hasFocus)
    }

    private fun checkAuth() {
        // TODO: Uncomment during release
        val timer = object : CountDownTimer(1000, 500) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (firebaseAuth.currentUser != null) {
                    goToMainActivity()
                } else goToLoginActivity()
            }
        }
        timer.start()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        overridePendingTransition(R.anim.fade_out_animation, R.anim.fade_in_animation)
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        overridePendingTransition(R.anim.fade_out_animation, R.anim.fade_in_animation)
    }
}
