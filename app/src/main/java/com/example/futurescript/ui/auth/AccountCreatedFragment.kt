package com.example.futurescript.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.futurescript.R
import com.example.futurescript.databinding.FragmentAccountCreatedBinding

class AccountCreatedFragment : Fragment() {
    private var _b: FragmentAccountCreatedBinding? = null

    private val b get() = _b!!

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentAccountCreatedBinding.inflate(i, c, false)
        return b.root
    }
    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)

        b.loginPageBtn.setOnClickListener {
            // Navigate back to LoginFragment
            findNavController().navigate(R.id.action_accountCreatedFragment_to_loginFragment)
        }
    }
    override fun onDestroyView() { _b = null; super.onDestroyView() }
}
