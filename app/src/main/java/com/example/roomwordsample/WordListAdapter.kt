package com.example.roomwordsample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class WordListAdapter internal constructor(
    context: Context, onClick: (id: Int) -> Unit
) : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cases = emptyList<Case>() // Cached copy of words
    private val onClick = onClick

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: Button = itemView.findViewById(R.id.recyclerButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = cases[position]
        holder.wordItemView.text =
            current.datetime.toString() + ": " + current.summary
        holder.wordItemView.setOnClickListener {
            onClick(current.id)
        }
    }


    internal fun setWords(cases: List<Case>) {
        this.cases = cases
        notifyDataSetChanged()
    }

    override fun getItemCount() = cases.size
}