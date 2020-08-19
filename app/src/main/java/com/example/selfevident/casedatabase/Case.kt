package com.example.selfevident.casedatabase

import androidx.room.*
import java.sql.Date

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
@TypeConverters(Converters::class)
data class Case(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "emotion") val emotion: String,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "summary") val summary: String,
    @ColumnInfo(name = "story") val story: String = "",
    @ColumnInfo(name = "datetime") val datetime: Date? = null
)
