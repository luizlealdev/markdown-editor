package dev.luizleal.markdowneditor.repository

import androidx.lifecycle.LiveData
import dev.luizleal.markdowneditor.dao.NoteDao
import dev.luizleal.markdowneditor.model.Note

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
}