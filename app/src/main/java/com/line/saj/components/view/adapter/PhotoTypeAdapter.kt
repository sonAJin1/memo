package com.line.saj.components.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.line.saj.components.model.Memo
import com.line.saj.databinding.ItemMemoBinding
import com.line.saj.databinding.ItemPhotoBinding
import com.line.saj.databinding.ItemPhotoTypeBinding
import com.line.saj.utils.RecyclerArrayAdapter

class PhotoTypeAdapter() : RecyclerArrayAdapter<String, RecyclerView.ViewHolder>() {

    private var listener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            ItemPhotoTypeBinding.inflate(
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
                listener!!.onClickItem()
            }
        }
    }


    class ItemViewHolder(val binding: ItemPhotoTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mType: String) {
            binding.apply {
                type = mType
                executePendingBindings()
            }
        }
    }

    fun setListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onClickItem()
    }
}