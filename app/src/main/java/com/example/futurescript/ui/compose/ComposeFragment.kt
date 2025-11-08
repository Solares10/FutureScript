package com.example.futurescript.ui.compose

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.futurescript.data.database.AppDatabase
import com.example.futurescript.data.database.entities.Letter
import com.example.futurescript.data.repository.LetterRepository
import com.example.futurescript.databinding.FragmentComposeBinding
import com.example.futurescript.util.localDateToEpochSeconds
import com.example.futurescript.util.showDatePicker
import com.example.futurescript.workers.scheduleDelivery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class ComposeFragment : Fragment() {

    private var _binding: FragmentComposeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: LetterRepository
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

        repository = LetterRepository(AppDatabase.get(requireContext()).letterDao())

        // Open date picker
        binding.dateField.setOnClickListener {
            showDatePicker(requireContext()) { date ->
                selectedDate = date
                binding.dateField.setText(date.toString())
            }
        }

        // Send letter
        binding.sendBtn.setOnClickListener {
            val titleText = binding.titleField.text.toString().trim()
            val messageText = binding.messageField.text.toString().trim()
            val date = selectedDate ?: return@setOnClickListener
            val epoch = localDateToEpochSeconds(date)

            if (titleText.isEmpty() || messageText.isEmpty()) {
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val id = repository.insert(
                    Letter(
                        title = titleText,
                        message = messageText,
                        deliverAtEpochSec = epoch
                    )
                )
                scheduleDelivery(requireContext(), id, epoch, messageText)
            }

            // Uncomment when navigation works:
            // findNavController().navigate(ComposeFragmentDirections.actionComposeToSentConfirm())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
