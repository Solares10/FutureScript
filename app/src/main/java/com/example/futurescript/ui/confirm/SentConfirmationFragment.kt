package com.example.futurescript.ui.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.futurescript.R
import com.example.futurescript.databinding.FragmentSentConfirmationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SentConfirmationFragment : Fragment() {

    private var _b: FragmentSentConfirmationBinding? = null
    private val b get() = _b!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentSentConfirmationBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // When the user taps the "HOME PAGE" button, navigate back
        b.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
