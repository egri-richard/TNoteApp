package com.tnote.tnoteapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tnote.tnoteapp.databinding.ItemTimetableBinding
import com.tnote.tnoteapp.models.Timetable

class TimetablesAdapter: RecyclerView.Adapter<TimetablesAdapter.TimetableViewHolder>() {

    inner class TimetableViewHolder(val binding: ItemTimetableBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<Timetable>() {
        override fun areItemsTheSame(oldItem: Timetable, newItem: Timetable): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Timetable, newItem: Timetable): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTimetableBinding.inflate(layoutInflater, parent, false)
        return TimetableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        val timetable = differ.currentList[position]

        holder.binding.apply {
            tvTimetableName.text = timetable.name
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(timetable) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Timetable) -> Unit)? = null

    fun setOnItemClickListener(listener: (Timetable) -> Unit) {
        onItemClickListener = listener
    }
}