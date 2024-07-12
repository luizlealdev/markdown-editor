package dev.luizleal.markdowneditor.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.luizleal.markdowneditor.model.Note
import dev.luizleal.markdowneditor.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository): ViewModel() {
    val allNotes: LiveData<List<Note>> = repository.allNotes

    fun insertNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }
}