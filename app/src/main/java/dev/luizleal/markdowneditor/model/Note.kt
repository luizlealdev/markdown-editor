package dev.luizleal.markdowneditor.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.luizleal.markdowneditor.constants.DatabaseConstants
import kotlinx.parcelize.Parcelize

@Entity(tableName = DatabaseConstants.NOTE_TABLE)
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val text: String,
    @ColumnInfo(name = "last_update_day") val lastUpdateDay: Int,
    @ColumnInfo(name = "last_update_month") val lastUpdateMonth: Int
) : Parcelable