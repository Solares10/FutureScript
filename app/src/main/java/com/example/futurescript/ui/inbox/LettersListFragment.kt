package com.example.futurescript.ui.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.futurescript.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LettersListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_letters_list, container, false)

        // Reference to your Floating Action Button
     val addButton = view.findViewById<FloatingActionButton>(R.id.addButton)


        // Navigate to ComposeFragment when clicked
        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_lettersListFragment_to_composeFragment)
        }

        return view
    }
}
