package com.example.futurescript.ui.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.futurescript.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LettersListFragment : Fragment() {

    private lateinit var addButton: FloatingActionButton
    private lateinit var backButton: Button
    private lateinit var bottomBar: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_letters_list, container, false)

        addButton = view.findViewById(R.id.addButton)
        backButton = view.findViewById(R.id.backButton)
        bottomBar = view.findViewById(R.id.bottomBar)

        bottomBar.visibility = View.GONE

        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_lettersListFragment_to_composeFragment)
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_lettersListFragment_to_sentLettersFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val letterCreated = arguments?.getBoolean("letterCreated") ?: false
        if (letterCreated) {
            bottomBar.visibility = View.VISIBLE
        }
    }
}
