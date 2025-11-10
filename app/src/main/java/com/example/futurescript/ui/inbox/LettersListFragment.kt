package com.example.futurescript.ui.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.futurescript.R
import com.example.futurescript.data.database.AppDatabase
import com.example.futurescript.data.database.entities.Letter
import com.example.futurescript.data.repository.LetterRepository
import com.example.futurescript.databinding.FragmentLettersListBinding
import com.example.futurescript.databinding.ItemLetterBinding
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

class LettersListFragment : Fragment() {
    private var _b: FragmentLettersListBinding? = null
    private val b get() = _b!!
    private lateinit var repo: LetterRepository
    private val adapter = LettersAdapter()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentLettersListBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = LetterRepository(AppDatabase.get(requireContext()).letterDao())
        b.lettersRecycler.layoutManager = LinearLayoutManager(requireContext())
        b.lettersRecycler.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        b.lettersRecycler.adapter = adapter

        b.fab.setOnClickListener {
            findNavController().navigate(R.id.composeFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repo.lettersFlow().collectLatest { list -> adapter.submitList(list) }
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
        val L = getItem(pos)
        h.b.messagePreview.text = L.message.take(80)
        h.b.date.text = df.format(Instant.ofEpochSecond(L.deliverAtEpochSec))
        h.b.status.text = if (L.delivered) "Delivered" else "Scheduled"
    }
}
