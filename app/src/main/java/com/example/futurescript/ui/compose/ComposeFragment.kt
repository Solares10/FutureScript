package com.example.futurescript.ui.compose

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.futurescript.R
import com.example.futurescript.utils.LetterNotificationReceiver
import com.example.futurescript.viewmodel.LetterViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ComposeFragment : Fragment() {

    private val letterViewModel: LetterViewModel by activityViewModels()
    private lateinit var messageField: EditText
    private lateinit var dateField: EditText
    private lateinit var timeField: EditText
    private lateinit var sendButton: MaterialButton
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_compose, container, false)

        messageField = view.findViewById(R.id.messageField)
        dateField = view.findViewById(R.id.dateField)
        timeField = view.findViewById(R.id.timeField)
        sendButton = view.findViewById(R.id.sendBtn)

        setupDatePicker()
        setupTimePicker()
        setupSendButton()

        return view
    }

    private fun setupDatePicker() {
        val currentDate = Calendar.getInstance()
        dateField.setOnClickListener {
            val picker = DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    calendar.set(Calendar.YEAR, y)
                    calendar.set(Calendar.MONTH, m)
                    calendar.set(Calendar.DAY_OF_MONTH, d)
                    dateField.setText(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time))
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
            )
            picker.datePicker.minDate = System.currentTimeMillis()
            picker.show()
        }
    }

    private fun setupTimePicker() {
        val currentTime = Calendar.getInstance()
        timeField.setOnClickListener {
            val picker = TimePickerDialog(
                requireContext(),
                { _, h, m ->
                    calendar.set(Calendar.HOUR_OF_DAY, h)
                    calendar.set(Calendar.MINUTE, m)
                    calendar.set(Calendar.SECOND, 0)
                    timeField.setText(SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time))
                },
                currentTime.get(Calendar.HOUR_OF_DAY),
                currentTime.get(Calendar.MINUTE),
                false
            )
            picker.show()
        }
    }

    private fun setupSendButton() {
        sendButton.setOnClickListener {
            val message = messageField.text.toString().trim()
            if (message.isEmpty()) {
                messageField.error = "Please write a message before sending!"
                return@setOnClickListener
            }
            if (dateField.text.isEmpty() || timeField.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please select both date and time!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                Toast.makeText(requireContext(), "Please choose a future time!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            letterViewModel.insert(message, calendar.timeInMillis)

            // 30-second test trigger
            scheduleNotification(requireContext(), System.currentTimeMillis() + 30_000, message)

            Toast.makeText(requireContext(), "Letter scheduled for delivery 💌", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_composeFragment_to_sentConfirmationFragment)
        }
    }

    private fun scheduleNotification(context: Context, triggerTime: Long, message: String) {
        val intent = Intent(context, LetterNotificationReceiver::class.java).apply {
            // 💌 Must match the action name in your AndroidManifest
            action = "com.example.futurescript.ACTION_SEND_LETTER"
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
            android.util.Log.d("FutureScript", "⏰ Alarm scheduled for ${(triggerTime - System.currentTimeMillis()) / 1000}s")
        } catch (e: SecurityException) {
            // Fallback if permission isn’t yet granted
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            android.util.Log.w("FutureScript", "⚠️ Falling back to inexact alarm: ${e.message}")
        }
    }

}

