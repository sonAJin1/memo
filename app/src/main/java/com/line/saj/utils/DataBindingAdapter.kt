package com.line.saj.utils

import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.line.saj.R
import org.w3c.dom.Text

class DataBindingAdapter {
    companion object {

        @BindingAdapter("bind_image")
        @JvmStatic
        fun bindImage(view: ImageView, url: String?) {
            if (url == null) return

            Glide.with(view.context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_photo_black_24dp)
                .error(R.drawable.ic_report_problem_black_24dp)
                .into(view)
        }

        @BindingAdapter("convert_text")
        @JvmStatic
        fun convertIntToString(view: TextView, value: Int) {
             view.text = value.toString()
        }

        @BindingAdapter("month_text")
        @JvmStatic
        fun convertMonthTOText(view: TextView, value: Int){
            view.text = when(value){
                1 -> "JAN"
                2 -> "FEB"
                3 -> "MAR"
                4 -> "APR"
                5 -> "MAY"
                6 -> "JUN"
                7 -> "JUL"
                8 -> "AUG"
                9 -> "SEP"
                10 -> "OCT"
                11 -> "NOV"
                12 -> "DEC"
                else -> "JAN"
            }
        }

    }
}