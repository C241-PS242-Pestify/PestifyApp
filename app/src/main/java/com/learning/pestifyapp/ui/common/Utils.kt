package com.learning.pestifyapp.ui.common

fun truncateText(text: String, maxLength: Int): String {
    return if (text.length <= maxLength) text else text.substring(0, maxLength) + "..."
}