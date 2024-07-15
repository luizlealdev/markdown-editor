package dev.luizleal.markdowneditor.utils

import java.text.DateFormatSymbols

class CalendarUtils {
    companion object {
        fun getMouthName(index: Int): String {
            return DateFormatSymbols().months[index]
        }
    }
}