package com.tnote.tnoteapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tnote.tnoteapp.databinding.ItemTtelementBinding
import com.tnote.tnoteapp.models.TTElement

class TTElementsAdapter: RecyclerView.Adapter<TTElementsAdapter.TTElementViewHolder>() {

    inner class TTElementViewHolder(val binding: ItemTtelementBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<TTElement>() {
        override fun areItemsTheSame(oldItem: TTElement, newItem: TTElement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TTElement, newItem: TTElement): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TTElementViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTtelementBinding.inflate(layoutInflater, parent,  false)
        return TTElementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TTElementViewHolder, position: Int) {
        val ttElement = differ.currentList[position]

        holder.binding.apply {
            tvLessonDay.text = "Day: ${ttElement.day}"
            tvLessonTitle.text = ttElement.title
            tvLessonStart.text = ttElement.start
            tvLessonEnd.text = ttElement.end
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(ttElement) }
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.let { it(ttElement) }

            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((TTElement) -> Unit)? = null

    fun setOnItemClickListener(listener: (TTElement) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemLongClickListener: ((TTElement) -> Unit)? = null

    fun setOnItemLongClickListener(listener: (TTElement) -> Unit) {
        onItemLongClickListener = listener
    }
}