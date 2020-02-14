package com.line.saj.components.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.line.saj.databinding.ItemThumbnailBinding
import com.line.saj.utils.RecyclerArrayAdapter

class ThumbnailAdapter() : RecyclerArrayAdapter<String, RecyclerView.ViewHolder>() {

    private var listener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            ItemThumbnailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            var photo = getItem(position)
            holder.bind(photo!!)
            val binding = holder.binding

            binding.root.setOnClickListener {
                listener!!.onClickDeleteItem(position)
            }
        }
    }


    class ItemViewHolder(val binding: ItemThumbnailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mPhoto: String) {
            binding.apply {
                photo = mPhoto
                executePendingBindings()
            }
        }
    }

    fun setListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onClickDeleteItem(position: Int)
    }
}