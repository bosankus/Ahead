package tech.androidplay.sonali.todo.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import tech.androidplay.sonali.todo.R

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Sep/2020
 * Email: ankush@androidplay.in
 */
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        checkAuth()
    }

    private fun checkAuth() {
        if (firebaseAuth.currentUser != null) {
            goToTaskFragment()
        } else goToAuthFragment()
    }

    private fun goToTaskFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_taskFragment)
    }

    private fun goToAuthFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_authFragment)
    }
}