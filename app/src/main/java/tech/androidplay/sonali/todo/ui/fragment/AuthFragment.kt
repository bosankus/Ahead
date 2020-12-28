package tech.androidplay.sonali.todo.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.AuthViewModel
import tech.androidplay.sonali.todo.utils.Extensions.hideKeyboard
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.UIHelper.viewAnimation

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Sep/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val authViewModel: AuthViewModel by viewModels()
    private var networkFlag: Boolean = false
    private val animFadeIn by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_animation)
    }
    private val animFadeOut by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out_animation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finishAffinity()
        }
        btnSignUpEmailPassword.setOnClickListener {
            requireActivity().hideKeyboard()
            val userEmail = loginInputEmailTxt.text.toString()
            val userPassword = loginInputPasswordTxt.text.toString()
            signUpUser(userEmail, userPassword)
        }

        btnloginEmailPassword.setOnClickListener {
            requireActivity().hideKeyboard()
            val userEmail = loginInputEmailTxt.text.toString()
            val userPassword = loginInputPasswordTxt.text.toString()
            login(userEmail, userPassword)
        }

        tvForgotPassword.setOnClickListener {
            requireActivity().hideKeyboard()
            val userEmail = loginInputEmailTxt.text.toString()
            resetPassword(userEmail)
        }

        tvSignUpOption.setOnClickListener { setSignUpUI() }

        tvLoginOption.setOnClickListener { setLoginUi() }
    }

    private fun signUpUser(userEmail: String, userPassword: String) {
        if (validateInput(userEmail, userPassword)) {
            authViewModel.createAccount(userEmail, userPassword).observe(
                viewLifecycleOwner,
                {
                    it?.let {
                        when (it) {
                            is ResultData.Loading -> showLoading(btnSignUpEmailPassword)
                            is ResultData.Success -> goToMainActivity()
                            is ResultData.Failed -> hideLoading(
                                btnSignUpEmailPassword,
                                it.message.toString()
                            )
                        }
                    }
                })
        } else showSnack(requireView(), "Please recheck your inputs")
    }

    private fun login(userEmail: String, userPassword: String) {
        if (validateInput(userEmail, userPassword)) {
            networkFlag = true
            authViewModel.loginUser(userEmail, userPassword).observe(
                viewLifecycleOwner,
                {
                    it?.let {
                        when (it) {
                            is ResultData.Loading -> showLoading(btnloginEmailPassword)
                            is ResultData.Success -> goToMainActivity()
                            is ResultData.Failed -> hideLoading(
                                btnloginEmailPassword,
                                it.message.toString()
                            )
                        }
                    }
                    networkFlag = false
                }
            )
        } else showSnack(requireView(), "Please recheck your inputs")
    }

    private fun resetPassword(userEmail: String) {
        if (userEmail.isNotEmpty()) {
            authViewModel.resetPassword(userEmail)
            showSnack(
                requireView(), "You will receive password " +
                        "reset mail if you are registered with us"
            )
        } else loginInputEmailTxt.error = "Please put your login email"
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

    private fun goToMainActivity() {
        findNavController().navigate(R.id.action_authFragment_to_taskFragment)
    }

    private fun showLoading(button: Button) {
        viewAnimation(button, animFadeIn, false)
        viewAnimation(lottieAuthLoading, null, true)
    }

    private fun hideLoading(button: Button, message: String = "") {
        showSnack(requireView(), message)
        viewAnimation(button, animFadeOut, true)
        viewAnimation(lottieAuthLoading, null, false)
    }

    private fun setSignUpUI() {
        if (networkFlag) {
            showToast(requireContext(), "Have Patience")
        } else {
            viewAnimation(btnloginEmailPassword, animFadeIn, false)
            viewAnimation(tvSignUpOption, animFadeIn, false)
            viewAnimation(btnSignUpEmailPassword, animFadeOut, true)
            viewAnimation(tvLoginOption, animFadeOut, true)
        }
    }

    private fun setLoginUi() {
        if (networkFlag) {
            showToast(requireContext(), "Have Patience")
        } else {
            viewAnimation(btnSignUpEmailPassword, animFadeIn, false)
            viewAnimation(tvLoginOption, animFadeIn, false)
            viewAnimation(btnloginEmailPassword, animFadeOut, true)
            viewAnimation(tvSignUpOption, animFadeOut, true)
        }
    }
}