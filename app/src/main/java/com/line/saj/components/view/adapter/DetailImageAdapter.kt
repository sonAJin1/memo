package com.line.saj.components.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.line.saj.components.model.Memo
import com.line.saj.databinding.ItemDetailImageBinding
import com.line.saj.databinding.ItemImageTypeBinding
import com.line.saj.databinding.ItemMemoBinding
import com.line.saj.utils.RecyclerArrayAdapter

class DetailImageAdapter(): RecyclerArrayAdapter<String, RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            ItemDetailImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val image = getItem(position)
            holder.bind(image)
        }
    }

    class ItemViewHolder(val binding: ItemDetailImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mImage: String?) {
            binding.apply {
                image = mImage
                executePendingBindings()
            }
        }
    }

}