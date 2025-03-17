package com.pawatr.coinmarketcap.utils

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.ImageView
import android.widget.TextView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import java.text.DecimalFormat

fun ImageView.setImageUrl(url: String){
    val imageLoader = ImageLoader.Builder(this.context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    this.load(url, imageLoader)
}

fun TextView.setSpanColor(
    fullText: String,
    targetText: String,
    color: Int,
    boldTarget: Boolean = false
) {
    val spannableString = SpannableString(fullText)

    val startIndex = fullText.indexOf(targetText)
    if (startIndex != -1) {
        val endIndex = startIndex + targetText.length

        spannableString.setSpan(
            ForegroundColorSpan(color),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        if (boldTarget) {
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    this.text = spannableString
}

fun String.toCurrencyDecimalLongFormat(): String {
    if (this == "-" || this == "." || this == "0" || this == ".00" || this.isEmpty()) {
        return "0.00000"
    }
    val number = this.toDouble()
    val formatter = DecimalFormat("#,##0.00000")
    return formatter.format(number)
}

fun String.toCurrencyDecimalFormat(): String {
    if (this == "-" || this == "." || this == "0" || this == ".00" || this.isEmpty()) {
        return "0.00"
    }
    val number = this.toDouble()
    val formatter = DecimalFormat("#,##0.00")
    return formatter.format(number)
}

fun String.toDisplayMarketCap(): String {
    return try {
        val mkc = this.toDouble()
        when {
            mkc >= 1_000_000_000_000.0 -> "${(mkc / 1_000_000_000_000.0).toString().toCurrencyDecimalFormat()} Trillion"
            mkc >= 1_000_000_000.0 -> "${(mkc / 1_000_000_000.0).toString().toCurrencyDecimalFormat()} Billion"
            mkc >= 1_000_000.0 -> "${(mkc / 1_000_000.0).toString().toCurrencyDecimalFormat()} Million"
            else -> mkc.toString().toCurrencyDecimalFormat()
        }
    } catch (e: Exception) {
        "0.00"
    }
}

fun String.toColor(): Int {
    return try {
        Color.parseColor(this)
    } catch (e: Exception) {
        Color.parseColor("#000000")
    }
}
