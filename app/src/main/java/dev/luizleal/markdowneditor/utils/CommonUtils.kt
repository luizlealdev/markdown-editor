package dev.luizleal.markdowneditor.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import dev.luizleal.markdowneditor.model.Note
import dev.luizleal.markdowneditor.ui.viewmodel.NoteViewModel
import java.io.BufferedReader

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

        fun deleteNote(viewModel: NoteViewModel, note: Note) {
            viewModel.deleteNote(note)
        }

        fun readRawFile(context: Context, resId: Int): String {
            return context.resources.openRawResource(resId).bufferedReader().use(BufferedReader::readText)
        }

//        fun saveNote(viewModel: NoteViewModel, note: Note) {
//            viewModel.insertNote(note)
//        }
//
//        fun updateNote(viewModel: NoteViewModel, note: Note) {
//            viewModel.updateNote(note)
//        }
    }
}