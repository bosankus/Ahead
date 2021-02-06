package tech.androidplay.sonali.todo.ui.fragment

import android.os.Bundle
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
import tech.androidplay.sonali.todo.databinding.FragmentAuthBinding
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.hideKeyboard
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.UIHelper.viewAnimation
import tech.androidplay.sonali.todo.utils.viewLifecycleLazy
import tech.androidplay.sonali.todo.viewmodel.AuthViewModel

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Sep/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewLifecycleLazy { FragmentAuthBinding.bind(requireView()) }
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
        binding.btnSignUpEmailPassword.setOnClickListener {
            requireActivity().hideKeyboard()
            val userEmail = binding.loginInputEmailTxt.text.toString()
            val userPassword = binding.loginInputPasswordTxt.text.toString()
            val fName = binding.userInputFirstName.text.toString()
            val lName = binding.userInputLastName.text.toString()
            createAccount(userEmail, userPassword, fName, lName)
        }

        binding.btnloginEmailPassword.setOnClickListener {
            requireActivity().hideKeyboard()
            val userEmail = binding.loginInputEmailTxt.text.toString()
            val userPassword = binding.loginInputPasswordTxt.text.toString()
            login(userEmail, userPassword)
        }

        binding.tvForgotPassword.setOnClickListener {
            requireActivity().hideKeyboard()
            val userEmail = binding.loginInputEmailTxt.text.toString()
            resetPassword(userEmail)
        }

        binding.tvSignUpOption.setOnClickListener { setSignUpUI() }

        binding.tvLoginOption.setOnClickListener { setLoginUi() }
    }

    private fun createAccount(
        userEmail: String,
        userPassword: String,
        fName: String,
        lName: String
    ) {
        authViewModel.createAccount(userEmail, userPassword, fName, lName).observe(
            viewLifecycleOwner,
            {
                it?.let {
                    when (it) {
                        is ResultData.Loading -> showLoading(btnSignUpEmailPassword)
                        is ResultData.Success -> goToMainActivity()
                        is ResultData.Failed -> hideLoading(
                            btnSignUpEmailPassword, it.message.toString()
                        )
                    }
                }
            })
    }

    private fun login(userEmail: String, userPassword: String) {
        networkFlag = true
        authViewModel.loginUser(userEmail, userPassword).observe(viewLifecycleOwner, {
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
        })
    }

    private fun resetPassword(userEmail: String) {
        authViewModel.resetPassword(userEmail).observe(viewLifecycleOwner, {
            it?.let {
                when (it) {
                    is ResultData.Loading -> showSnack(requireView(), "Please wait")
                    is ResultData.Success -> showSnack(requireView(), it.data.toString())
                    is ResultData.Failed -> showSnack(requireView(), it.message.toString())
                }
            }
        })
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
            viewAnimation(authInputContainer, animFadeOut, true)
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
            viewAnimation(authInputContainer, animFadeIn, false)
            viewAnimation(btnSignUpEmailPassword, animFadeIn, false)
            viewAnimation(tvLoginOption, animFadeIn, false)
            viewAnimation(btnloginEmailPassword, animFadeOut, true)
            viewAnimation(tvSignUpOption, animFadeOut, true)
        }
    }
}