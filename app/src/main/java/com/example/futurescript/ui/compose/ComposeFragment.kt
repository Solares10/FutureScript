package com.example.futurescript.ui.compose

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.futurescript.R
import com.example.futurescript.viewmodel.LetterViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ComposeFragment : Fragment() {

    private val letterViewModel: LetterViewModel by activityViewModels()

    private lateinit var messageField: EditText
    private lateinit var dateField: EditText
    private lateinit var sendButton: MaterialButton

    private var selectedDateMillis: Long = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_compose, container, false)

        messageField = view.findViewById(R.id.messageField)
        dateField = view.findViewById(R.id.dateField)
        sendButton = view.findViewById(R.id.sendBtn)

        setupDatePicker()
        setupSendButton()

        return view
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()

        dateField.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDateMillis = calendar.timeInMillis

                    val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        .format(calendar.time)
                    dateField.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }
    }

    private fun setupSendButton() {
        sendButton.setOnClickListener {
            val message = messageField.text.toString().trim()

            if (message.isNotEmpty()) {
                // Save letter to database via ViewModel
                letterViewModel.insert(
                    message = message,
                    deliverAtEpochSec = selectedDateMillis
                )

                // Navigate to Sent Confirmation fragment
                findNavController().navigate(R.id.action_composeFragment_to_sentConfirmationFragment)
            } else {
                messageField.error = "Please write a message before sending!"
            }
        }
    }
}
