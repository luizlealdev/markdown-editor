package dev.luizleal.markdowneditor.utils

import java.util.regex.Pattern

class StringUtils {
    companion object {
        fun String.isAValidUrl(): Boolean {
            val pattern = Pattern.compile("^(ftp|http|https):\\/\\/[^ \"]+\$")
            val matcher = pattern.matcher(this)

            return matcher.matches()
        }
    }
}