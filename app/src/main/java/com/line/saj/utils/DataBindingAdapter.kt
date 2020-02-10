package com.line.saj.utils

import android.widget.ImageView
import android.widget.SeekBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class DataBindingAdapter {
    companion object {

        @BindingAdapter("bind_image")
        @JvmStatic
        fun bindImage(view: ImageView, url: String?) {
            Glide.with(view.context)
                .load(url)
                .into(view)
        }

    }
}