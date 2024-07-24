package dev.luizleal.markdowneditor.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.luizleal.markdowneditor.constants.DatabaseConstants
import dev.luizleal.markdowneditor.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM ${DatabaseConstants.NOTE_TABLE} ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM ${DatabaseConstants.NOTE_TABLE} WHERE text LIKE :query")
    fun searchNote(query: String): LiveData<List<Note>>
}