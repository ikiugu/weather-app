package com.ikiugu.weather.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Alfred Ikiugu on 11/06/2021
 */

fun Long.toDate(): String {
    val formatter = SimpleDateFormat("EEEE", Locale.US)
    val expiry = Date(this * 1000)
    return formatter.format(expiry)
}