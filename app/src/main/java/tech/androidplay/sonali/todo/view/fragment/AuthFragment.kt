package tech.androidplay.sonali.todo.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.FragmentAuthBinding
import tech.androidplay.sonali.todo.utils.AuthManager
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.viewLifecycleLazy
import tech.androidplay.sonali.todo.viewmodel.AuthViewModel
import javax.inject.Inject

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

    @Inject
    lateinit var authManager: AuthManager

    private val viewModel: AuthViewModel by viewModels()

    private val authLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            handleAuthResult(result)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finish()
        }

        binding.btnSignUp.setOnClickListener {
            if (!authManager.isUserLoggedIn) authManager.initiateAuthentication(authLauncher)
            else showSnack(requireView(), "Already signedIn")
        }
    }

    @SuppressLint("RestrictedApi")
    private fun handleAuthResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (response?.isSuccessful == true && result.resultCode == Activity.RESULT_OK) {
            authManager.userDetails?.let { viewModel.saveUserData(it) }
            this.requireActivity().findNavController(R.id.navHostFragment)
                .navigate(R.id.action_global_taskFragment)
        } else response?.error?.let {
            showSnack(
                requireView(), getString(
                    R.string.prompt_failed_to_login, it.message.toString()
                )
            )
        }
    }
}
