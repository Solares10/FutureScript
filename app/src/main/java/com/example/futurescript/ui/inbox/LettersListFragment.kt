package com.example.futurescript.ui.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.futurescript.R
import com.example.futurescript.data.database.entities.Letter
import com.example.futurescript.databinding.FragmentLettersListBinding
import com.example.futurescript.databinding.ItemLetterBinding
import com.example.futurescript.viewmodel.AuthViewModel
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

@AndroidEntryPoint
class LettersListFragment : Fragment() {
    private var _b: FragmentLettersListBinding? = null
    private val b get() = _b!!

    // ViewModel for authentication and database access
    // private val authViewModel: AuthViewModel by viewModels()         // Does not have logout button
    private val letterViewModel: LetterViewModel by activityViewModels()

    private var lastClickTime: Long = 0

    private fun onSafeClick(action: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= 500) {
            lastClickTime = currentTime
            action()
        }
    }

    private val adapter = LettersAdapter { clickedLetter ->
        onSafeClick {
            val action =
                LettersListFragmentDirections.actionLettersListFragmentToItemLetterFragment(
                    clickedLetter.id
                )
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentLettersListBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.lettersRecycler.layoutManager = LinearLayoutManager(requireContext())
        b.lettersRecycler.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        b.lettersRecycler.adapter = adapter

        // Floating action button -> compose screen
        b.addButton.setOnClickListener {
            onSafeClick {
                findNavController().navigate(R.id.action_lettersListFragment_to_composeFragment)
            }
        }

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
        _b = null;
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
    }
}
