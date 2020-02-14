package com.line.saj.components.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.line.saj.components.model.Memo
import com.line.saj.databinding.ItemMemoBinding
import com.line.saj.utils.RecyclerArrayAdapter

class MemoAdapter() : RecyclerArrayAdapter<Memo, RecyclerView.ViewHolder>() {

    //TODO: header나 뭔가 Filter를 적용하여 memo 만들기 검색기능도 있으면 좋을 듯 (화해 앱 처럼)
    private var listener: OnClickListener? = null

    val TYPE_ITEM = 1
    val TYPE_FOOTER = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            ItemMemoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            var memo = getItem(position)
            holder.bind(memo!!)
            val binding = holder.binding



            binding.root.setOnClickListener {
                listener!!.onClickItem(memo.memoId)
            }

            binding.ivDelete.setOnClickListener{
                listener!!.onClickDeleteItem(memo.memoId)
            }
        }
    }


    class ItemViewHolder(val binding: ItemMemoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mMemo: Memo) {
            binding.apply {
                memo = mMemo
                executePendingBindings()
            }
        }
    }

    fun setListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onClickItem(id: Int)
        fun onClickDeleteItem(id: Int)
    }
}