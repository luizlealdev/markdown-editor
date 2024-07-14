package dev.luizleal.markdowneditor.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.luizleal.markdowneditor.constants.DatabaseConstants

@Entity(tableName = DatabaseConstants.NOTE_TABLE)
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val text: String,
    @ColumnInfo(name = "last_update_day") val lastUpdateDay: Int,
    @ColumnInfo(name = "last_update_month") val lastUpdateMonth: Int
)