package com.tekskills.er_tekskills.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan


class TextViewHtmlString {
//    fun getFormattedText(text: String): SpannableString {
//        val spannable = SpannableString(text)
//        val startIndex = text.indexOf(":") + 2 // +2 to exclude ': '
//        val endIndex = text.length
//        spannable.setSpan(
//            ForegroundColorSpan(Color.rgb(235, 100, 38)), 0,
//            spannable.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        spannable.setSpan(
//            StyleSpan(Typeface.BOLD),
//            startIndex,
//            endIndex,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        return spannable
//    }

//    fun getFormattedText(text: String): SpannableString? {
//        val spannable = SpannableString(text)
//        val startIndex = text.indexOf(":") + 2 // +2 to exclude ': '
//        val endIndex = text.length
//
//        // Set the start index (": ") to be in red color
//        spannable.setSpan(
//            ForegroundColorSpan(ContextCompat.getColor(context, R.color.red_color)),
//            0,
//            startIndex,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        // Set the end index (rest of the text) to be in black color and bold
//        spannable.setSpan(
//            ForegroundColorSpan(ContextCompat.getColor(context, R.color.black_color)),
//            startIndex,
//            endIndex,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        spannable.setSpan(
//            StyleSpan(Typeface.BOLD),
//            startIndex,
//            endIndex,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        return spannable
//    }

    fun getFormattedText(text: String): SpannableString {
        val spannable = SpannableString(text)
        val endIndex = text.length

        // Apply bold style to the text from startIndex to endIndex
        spannable.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Apply red color to the text from startIndex to endIndex
//        spannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannable
    }


    fun getFormattedTexts(text: String): SpannableString {
        val spannable = SpannableString(text)
        val startIndex = text.indexOf(":") + 2 // +2 to exclude ': '

        // Set the start part (before the colon) to bold and red
        spannable.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannable.setSpan(ForegroundColorSpan(Color.RED), 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannable
    }
}