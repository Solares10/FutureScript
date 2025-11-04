package com.example.futurescript.ui.compose

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.futurescript.data.database.AppDatabase
import com.example.futurescript.data.model.Letter
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
    private var _b: FragmentComposeBinding? = null
    private val b get() = _b!!
    private lateinit var repo: LetterRepository
    private var pickedDate: LocalDate? = null

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentComposeBinding.inflate(i, c, false); return b.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = LetterRepository(AppDatabase.get(requireContext()).letterDao())

        b.dateField.setOnClickListener {
            showDatePicker(requireContext()) { ld ->
                pickedDate = ld
                b.dateField.setText(ld.toString())
            }
        }

        b.sendBtn.setOnClickListener {
            val title = b.title.text.toString().trim()
            val msg = b.message.text.toString().trim()
            val ld = pickedDate ?: return@setOnClickListener
            val epoch = localDateToEpochSeconds(ld)
            CoroutineScope(Dispatchers.IO).launch {
                val id = repo.insert(Letter(title = title, message = msg, deliverAtEpochSec = epoch))
                scheduleDelivery(requireContext(), id, epoch, msg)
            }
            //findNavController().navigate(ComposeFragmentDirections.actionComposeToSentConfirm())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
