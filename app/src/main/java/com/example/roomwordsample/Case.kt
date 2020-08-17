package com.example.roomwordsample

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity(tableName = "case_table")
data class Case(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "emotion") val emotion: String,
    @ColumnInfo(name = "summary") val summary: String,
    @ColumnInfo(name = "story") val story: String = "",
    @ColumnInfo(name = "datetime") val datetime: String = "Unknown"
)