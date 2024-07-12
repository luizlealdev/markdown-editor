package dev.luizleal.markdowneditor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.luizleal.markdowneditor.constants.DatabaseConstants
import dev.luizleal.markdowneditor.dao.NoteDao
import dev.luizleal.markdowneditor.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DatabaseConstants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }
}