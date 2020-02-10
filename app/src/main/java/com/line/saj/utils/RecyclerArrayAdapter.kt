package com.line.saj.utils

import java.util.*
import kotlin.collections.ArrayList


abstract class RecyclerArrayAdapter<M, VH : androidx.recyclerview.widget.RecyclerView.ViewHolder> :
    androidx.recyclerview.widget.RecyclerView.Adapter<VH>() {
    private val items = ArrayList<M>()

    val isEmpty: Boolean
        get() = items.size == 0

    private val lastPosition: Int
        get() = if (itemCount > 0) itemCount - 1 else 0

    init {
        setHasStableIds(true)
    }

    fun add(`object`: M) {
        items.add(`object`)
        notifyDataSetChanged()
    }

    fun add(index: Int, `object`: M) {
        items.add(index, `object`)
        notifyDataSetChanged()
    }

    fun addAll(collection: Collection<M>?) {
        if (collection != null) {
            items.addAll(collection)
            notifyDataSetChanged()
        }
    }

    fun addAll(vararg items: M) {
        addAll(Arrays.asList(*items))
    }

    fun append(vararg items: M) {
        val lastPos = lastPosition
        addAll(Arrays.asList(*items))
        notifyItemRangeInserted(lastPos, Arrays.asList(*items).size)
    }

    fun remove(`object`: M) {
        notifyItemRemoved(items.indexOf(`object`))
        items.remove(`object`)
    }

    fun remove(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeAll() {
        val lastCnt = itemCount
        notifyItemRangeRemoved(0, lastCnt)
        items.clear()
    }

    fun getItem(position: Int): M? {
        return if (null == items || items.size == 0 || items.size <= position) {
            null
        } else items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return items.size
    }

}