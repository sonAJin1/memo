package com.line.saj.utils

import android.widget.ImageView
import android.widget.SeekBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.line.saj.R

class DataBindingAdapter {
    companion object {

        @BindingAdapter("bind_image")
        @JvmStatic
        fun bindImage(view: ImageView, url: String?) {
            Glide.with(view.context)
                .load(url)
                .placeholder(R.drawable.ic_photo_black_24dp)
                .error(R.drawable.ic_report_problem_black_24dp)
                .into(view)
        }

    }
}