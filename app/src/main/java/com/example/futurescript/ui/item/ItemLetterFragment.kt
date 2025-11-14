package com.example.futurescript.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.futurescript.databinding.ItemLetterBinding
import com.example.futurescript.viewmodel.LetterViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ItemLetterFragment : Fragment() {

    private var _binding: ItemLetterBinding? = null
    private val binding get() = _binding!!

    private val letterViewModel: LetterViewModel by activityViewModels()

    // Temporary variable for letter ID until Safe Args is fixed
    private var letterId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemLetterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Retrieve letterId manually from arguments
        letterId = arguments?.getLong("letterId") ?: -1L
        letterViewModel.selectLetter(letterId)

        setupClickListeners()
        observeLetter()
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.deleteButton.setOnClickListener {
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

                        binding.dateSent.text =
                            formatter.format(Instant.ofEpochMilli(it.createdAtEpochSec))
                        binding.dateDeliver.text =
                            formatter.format(Instant.ofEpochMilli(it.deliverAtEpochSec))
                        binding.letterContent.text = it.message
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
        letterViewModel.clearSelectedLetter()
        _binding = null
    }
}
