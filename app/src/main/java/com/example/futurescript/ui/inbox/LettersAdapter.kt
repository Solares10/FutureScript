package com.example.futurescript.ui.inbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.futurescript.R

class LettersAdapter(private val letters: List<Pair<String, String>>) :
    RecyclerView.Adapter<LettersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.letterTitle)
        val preview: TextView = itemView.findViewById(R.id.letterPreview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_letter_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val letter = letters[position]
        holder.title.text = letter.first
        holder.preview.text = letter.second
    }

    override fun getItemCount() = letters.size
}

