package com.example.futurescript.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.futurescript.R
import com.example.futurescript.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private var _b: FragmentSignupBinding? = null
    private val b get() = _b!!
    override fun onCreateView(
        i: LayoutInflater,
        c: ViewGroup?,
        s: Bundle?)
    : View {
        _b = FragmentSignupBinding.inflate(i, c, false); return b.root
    }
    override fun onViewCreated(v: View, s: Bundle?) {
        b.signupBtn.setOnClickListener {
           findNavController().navigate(R.id.action_signupFragment_to_accountCreatedFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _b = null; super.onDestroyView() }
}
