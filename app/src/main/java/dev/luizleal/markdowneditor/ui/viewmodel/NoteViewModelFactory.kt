package dev.luizleal.markdowneditor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.luizleal.markdowneditor.repository.NoteRepository

class NoteViewModelFactory(private val repository: NoteRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}