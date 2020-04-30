package tech.androidplay.sonali.todo.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.fxn.OnBubbleClickListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import tech.androidplay.sonali.todo.R

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initiating firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance()

        // enable white status bar with black icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        // Setting default fragment
        supportFragmentManager.inTransaction {
            replace(R.id.container, HomeFragment())
        }

        // load bottom navigation bar animations
        val animation = AnimationUtils.loadAnimation(this, R.anim.btn_animation)
        mainBottomBar.startAnimation(animation)

        // turning listeners on
        clickListeners()

    }

    private fun clickListeners() {
        mainBottomBar.addBubbleListener(object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                when (id) {
                    R.id.main -> supportFragmentManager.inTransaction {
                        this.addToBackStack(null)
                        hide(HomeFragment())
                        add(R.id.container, HomeFragment(), "MAIN")
                    }
                    R.id.alarm -> supportFragmentManager.inTransaction {
                        this.addToBackStack(null)
                        hide(AlarmFragment())
                        add(R.id.container, AlarmFragment(), "ALARM_FRAGMENT")
                    }
                    R.id.event -> supportFragmentManager.inTransaction {
                        this.addToBackStack(null)
                        hide(EventFragment())
                        add(R.id.container, EventFragment(), "EVENT")
                    }
                    R.id.profile -> supportFragmentManager.inTransaction {
                        this.addToBackStack(null)
                        hide(ProfileFragment())
                        add(R.id.container, ProfileFragment(), "PROFILE")
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    // Fragment Manager Transaction function
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func().commit()
    }
}

