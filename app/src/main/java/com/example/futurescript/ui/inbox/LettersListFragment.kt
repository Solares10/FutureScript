package com.example.futurescript.ui.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
<<<<<<< HEAD
=======
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
>>>>>>> origin/main
import androidx.navigation.fragment.findNavController
import com.example.futurescript.R
<<<<<<< HEAD
import com.google.android.material.floatingactionbutton.FloatingActionButton
=======
import com.example.futurescript.data.database.entities.Letter
import com.example.futurescript.databinding.FragmentLettersListBinding
import com.example.futurescript.databinding.ItemLetterBinding
import com.example.futurescript.viewmodel.LetterViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import android.view.LayoutInflater as LI
import android.view.ViewGroup as VG
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
>>>>>>> origin/main

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
<<<<<<< HEAD
=======

        // Observe letters
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                letterViewModel.letters.collectLatest { list ->
                    adapter.safeSubmitList(list)
                    if (list.isEmpty()) {
                        b.emptyStateGroup.visibility = View.VISIBLE
                        b.lettersRecycler.visibility = View.GONE
                    }
                    else {
                        b.emptyStateGroup.visibility = View.GONE
                        b.lettersRecycler.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}

private val df = DateTimeFormatter.ofPattern("MMM d, yyyy").withZone(ZoneId.systemDefault())

class LettersAdapter(
    private val onLetterClicked: (Letter) -> Unit
) : ListAdapter<Letter, LettersAdapter.VH>(DIFF) {

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        super.onAttachedToRecyclerView(rv)
        recyclerView = rv
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        super.onDetachedFromRecyclerView(rv)
        recyclerView = null
    }

    fun safeSubmitList(list: List<Letter>?) {
        if (recyclerView?.isAttachedToWindow == true) {
            submitList(list)
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Letter>() {
            override fun areItemsTheSame(o: Letter, n: Letter) = o.id == n.id
            override fun areContentsTheSame(o: Letter, n: Letter) = o == n
        }
    }
    class VH(val b: ItemLetterBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(p: VG, v: Int) =
        VH(ItemLetterBinding.inflate(LI.from(p.context), p, false))

    override fun onBindViewHolder(h: VH, pos: Int) {
        val l = getItem(pos)

        // Set an OnClickListener for the entire item view
        h.itemView.setOnClickListener {
            onLetterClicked(l)
        }

        h.b.messagePreview.text = l.message.take(80)
        h.b.date.text = df.format(Instant.ofEpochSecond(l.deliverAtEpochSec))
        h.b.status.text = if (l.delivered) "Delivered" else "Scheduled"
>>>>>>> origin/main
    }
}
