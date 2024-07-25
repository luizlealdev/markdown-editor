package dev.luizleal.markdowneditor.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import dev.luizleal.markdowneditor.R
import dev.luizleal.markdowneditor.model.Note
import dev.luizleal.markdowneditor.ui.viewmodel.NoteViewModel
import java.io.BufferedReader
import java.util.regex.Pattern

class CommonUtils {
    companion object {
        fun shareText(activity: Activity, title: String, subject: String, body: String) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, body)

            activity.startActivity(Intent.createChooser(shareIntent, title))
        }

        fun copyText(activity: Activity, text: String) {
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", text)

            clipboard.setPrimaryClip(clip)
        }

        fun readRawFile(context: Context, resId: Int): String {
            return context.resources.openRawResource(resId).bufferedReader()
                .use(BufferedReader::readText)
        }

        fun getSyntaxHighLightPattern(context: Context): Map<Pattern, Int> {
            val syntaxPattern = mutableMapOf<Pattern, Int>()

            val syntaxRegexAndColor = mutableMapOf(
                "^(#{1,6})\\s+.*$" to R.color.markdownHeadingColor,
                "(\\*\\*|__)(.*?)\\1" to R.color.markdownBoldColor,
                "(\\*|_)(.*?)\\1" to R.color.markdownItalicColor,
                "\\[.*?\\]\\(.*?\\)" to R.color.markdownLinkColor,
                "```.*?```" to R.color.markdownCodeBlockColor,
                "`.*?`" to R.color.markdownCodeBlockColor,
                "^\\s*[-+*]\\s+.*$" to R.color.markdownListColor,
                "^\\s*\\d+\\.\\s+.*$" to R.color.markdownListColor,
                "^>\\s+.*$" to R.color.markdownQuoteColor

            )
            for ((regex, resId) in syntaxRegexAndColor) {
                syntaxPattern[Pattern.compile(regex, Pattern.MULTILINE)] = ContextCompat.getColor(
                    context,
                    resId
                )
            }

            return syntaxPattern
        }

        fun saveNote(viewModel: NoteViewModel, note: Note) {
            viewModel.insertNote(note)
        }

        fun updateNote(viewModel: NoteViewModel, note: Note) {
            viewModel.updateNote(note)
        }

        fun deleteNote(viewModel: NoteViewModel, note: Note) {
            viewModel.deleteNote(note)
        }

        fun searchNote(
            viewModel: NoteViewModel,
            owner: LifecycleOwner,
            query: String,
            callback: (List<Note>) -> Unit
        ) {
            val formatedQuery = "%$query%"

            viewModel.searchNote(formatedQuery).observe(owner) { items ->
                callback(items)
            }
        }
    }
}