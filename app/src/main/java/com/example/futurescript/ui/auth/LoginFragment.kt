package com.example.futurescript.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.futurescript.databinding.FragmentLoginBinding
import com.example.futurescript.R
import com.example.futurescript.viewmodel.AuthViewModel
import com.example.futurescript.data.auth.AuthState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

// LoginFragment.kt
@AndroidEntryPoint
class LoginFragment : Fragment() {

    // View Binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // ViewModel (Hilt handles injection)
    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        // Handle Login Button click
        binding.loginButton.setOnClickListener {
            // TODO: Add your login logic here
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.login(email,password)
            }
            else {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Sign Up Text click
        binding.signUpText.setOnClickListener {
            // Optional navigation (requires navigation graph)
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            authViewModel.authState.collectLatest { state ->
                when (state) {
                    is AuthState.Loading -> showLoading(true)

                    is AuthState.Authenticated -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), "Welcome ${state.user.displayName}!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }

                    is AuthState.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }

                    is AuthState.Unauthenticated -> showLoading(false)
                }
            }
        }

        return view
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loginButton.isEnabled = !isLoading
        binding.emailEditText.isEnabled = !isLoading
        binding.passwordEditText.isEnabled = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
