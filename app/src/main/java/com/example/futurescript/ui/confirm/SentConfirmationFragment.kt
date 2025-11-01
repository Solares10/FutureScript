package com.example.futurescript.ui.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.futurescript.R
import com.example.futurescript.databinding.FragmentSentConfirmationBinding

class SentConfirmationFragment : Fragment() {
    private var _b: FragmentSentConfirmationBinding? = null
    private val b get() = _b!!
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentSentConfirmationBinding.inflate(i, c, false); return b.root
    }
    override fun onViewCreated(v: View, s: Bundle?) {
        b.homeBtn.setOnClickListener { findNavController().navigate(R.id.lettersListFragment) }
    }
    override fun onDestroyView() { _b = null; super.onDestroyView() }
}
