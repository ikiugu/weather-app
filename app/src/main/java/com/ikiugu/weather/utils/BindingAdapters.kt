package com.ikiugu.weather.utils

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Alfred Ikiugu on 10/06/2021
 */

@BindingAdapter("hideViewIfNull")
fun hideViewIfNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.VISIBLE else View.GONE
}

@BindingAdapter("showViewIfNull")
fun showViewIfNull(view: View, it: Any?) {
    view.visibility = if (it == null) View.VISIBLE else View.GONE
}

@BindingAdapter("showCorrectImage")
fun showCorrectImage(imageView: ImageView, drawable: Int) {
    imageView.setImageResource(drawable)
}

@BindingAdapter("showCorrectText")
fun showCorrectText(textView: TextView, weather: Any?) {
    if (weather != null) {
        textView.text = weather.toString()
    }
}

@BindingAdapter("setBackgroundColor")
fun setBackgroundColor(view: LinearLayout, color: Int) {
    view.setBackgroundColor(color)
}

@BindingAdapter("setRecyclerViewBackground")
fun setRecyclerViewBackground(view: RecyclerView, color: Int) {
    view.setBackgroundColor(color)
}