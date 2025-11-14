package com.example.futurescript.ui.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.futurescript.R
import com.google.android.material.button.MaterialButton

class SentConfirmationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sent_confirmation, container, false)

        val backButton = view.findViewById<MaterialButton>(R.id.backToLettersButton)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_sentConfirmationFragment_to_lettersListFragment)
        }

        return view
    }
}
