package com.ikiugu.weather.utils

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Created by Alfred Ikiugu on 10/06/2021
 */

@BindingAdapter("hideTextViewIfNull")
fun hideTextViewIfNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.VISIBLE else View.GONE
}