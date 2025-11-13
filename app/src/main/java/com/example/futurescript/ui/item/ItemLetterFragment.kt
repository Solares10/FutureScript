package com.example.futurescript.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.futurescript.viewmodel.LetterViewModel
import com.example.futurescript.databinding.ItemLetterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ItemLetterFragment: Fragment() {

    private var _b: ItemLetterBinding? = null
    private val b get() = _b!!

    // Hilt will provide the ViewModel instance.
    private val letterViewModel: LetterViewModel by viewModels()

    // safe-args delegate to get the letterId passed from the previous fragment.
    private val args: ItemLetterFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = ItemLetterBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        letterViewModel.selectLetter(args.letterId)

        setupClickListeners()
        observeLetter()
    }

    private fun setupClickListeners() {
        b.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        b.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

    }

    private fun observeLetter() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                letterViewModel.selectedLetter.collect { letter ->
                    letter?.let {
                        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
                            .withZone(ZoneId.systemDefault())

                        b.dateSent.text =
                            formatter.format(Instant.ofEpochMilli(it.createdAtEpochSec))
                        b.dateDeliver.text =
                            formatter.format(Instant.ofEpochMilli(it.deliverAtEpochSec))
                        b.letterContent.text = it.message

                    }
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Letter")
            .setMessage("Are you sure you want to delete this letter?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ ->
                letterViewModel.selectedLetter.value?.let { letterToDelete ->
                    letterViewModel.delete(letterToDelete)
                }
                findNavController().popBackStack()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }



}