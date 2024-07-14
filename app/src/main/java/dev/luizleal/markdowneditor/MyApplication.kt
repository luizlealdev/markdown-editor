package dev.luizleal.markdowneditor

import android.app.Application
import dev.luizleal.markdowneditor.database.NoteDatabase
import dev.luizleal.markdowneditor.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {
    //val applicationScope = CoroutineScope(SupervisorJob())
    val database: NoteDatabase by lazy { NoteDatabase.getDatabase(this) }
    val repository: NoteRepository by lazy { NoteRepository(database.noteDao()) }
}