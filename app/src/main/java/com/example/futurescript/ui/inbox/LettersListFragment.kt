package com.example.futurescript.ui.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
    private val letterViewModel: LetterViewModel by viewModels()

    private val adapter = LettersAdapter()

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
            findNavController().navigate(R.id.composeFragment)
        }

        // Observe letters
        viewLifecycleOwner.lifecycleScope.launch {
            letterViewModel.allLetters.collectLatest { list ->
                adapter.submitList(list)
                // TODO: will wait for frontend to finish with screens
                // b.emptyView.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() { _b = null; super.onDestroyView() }
}

private val df = DateTimeFormatter.ofPattern("MMM d, yyyy").withZone(ZoneId.systemDefault())

class LettersAdapter : ListAdapter<Letter, LettersAdapter.VH>(DIFF) {
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
        h.b.messagePreview.text = l.message.take(80)
        h.b.date.text = df.format(Instant.ofEpochSecond(l.deliverAtEpochSec))
        h.b.status.text = if (l.delivered) "Delivered" else "Scheduled"
    }
}
