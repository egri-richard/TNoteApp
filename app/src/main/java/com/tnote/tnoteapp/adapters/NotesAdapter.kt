package com.tnote.tnoteapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.databinding.ItemNoteBinding
import com.tnote.tnoteapp.models.Note

class NotesAdapter: RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    /*fun submitList(list: List<Note>?) {
        differ.submitList(list?.let { ArrayList(it) })
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = differ.currentList[position]
        holder.binding.apply {
            tvNoteTitle.text = note.title
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(note) }
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.let { it(note) }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Note) -> Unit)? = null

    fun setOnItemClickListener(listener: (Note) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemLongClickListener: ((Note) -> Unit)? = null

    fun setOnItemLongClickListener(listener: (Note) -> Unit) {
        onItemLongClickListener = listener
    }
}