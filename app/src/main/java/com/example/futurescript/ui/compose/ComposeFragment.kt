package com.example.futurescript.ui.compose

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.futurescript.R
import com.example.futurescript.viewmodel.LetterViewModel
import com.example.futurescript.databinding.FragmentComposeBinding
import com.example.futurescript.util.localDateToEpochSeconds
import com.example.futurescript.util.showDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class ComposeFragment : Fragment() {

    private var _binding: FragmentComposeBinding? = null
    private val binding get() = _binding!!
    private val letterViewModel: LetterViewModel by viewModels()
    private var selectedDate: LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComposeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Open date picker
        binding.dateField.setOnClickListener {
            showDatePicker(requireContext()) { date ->
                selectedDate = date
                binding.dateField.setText(date.toString())
            }
        }

        // Send letter
        binding.sendBtn.setOnClickListener {
            val messageText = binding.messageField.text.toString().trim()
            val date = selectedDate ?: return@setOnClickListener
            val epoch = localDateToEpochSeconds(date)

            if (messageText.isNotBlank()) {
                letterViewModel.insert(messageText, epoch)
                findNavController().navigate(R.id.sentConfirmationFragment)
            } else {
                Toast.makeText(context, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
