package com.example.recyclerviewswipeanddelete

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewswipeanddelete.databinding.ItemCardBinding
import java.lang.ref.WeakReference

class CardAdapter() : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    lateinit var onDeleteClick: (() -> Unit)

    private var dif = AsyncListDiffer(this, ITEM_DIFF)

    inner class ViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        private val view = WeakReference(binding.root)
        fun bind() {
            val card = dif.currentList[adapterPosition]
            view.get()?.let {
                it.setOnClickListener {
                    if (view.get()?.scrollX != 0) {
                        view.get()?.scrollTo(0, 0)
                    }
                }
            }
            binding.apply {
                tvTitle.text = card.title
                tvDelete.setOnClickListener {
                    onDeleteClick.invoke()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = dif.currentList.size

    fun submitList(list: List<Card>) {
        dif.submitList(list)
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem.title == newItem.title
        }
    }

}