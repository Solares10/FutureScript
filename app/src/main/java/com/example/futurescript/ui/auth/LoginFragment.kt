package com.example.futurescript.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.futurescript.databinding.FragmentLoginBinding
import com.example.futurescript.R   // ✅ Add this line

class LoginFragment : Fragment() {

    // View Binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        // Handle Login Button click
        binding.loginButton.setOnClickListener {
            // TODO: Add your login logic here
        }

        // Handle Sign Up Text click
        binding.signUpText.setOnClickListener {
            // Optional navigation (requires navigation graph)
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
