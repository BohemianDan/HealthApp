package com.example.selfevident

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "pattern_table")
data class Pattern(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "emotion") val emotion: String,
    @ColumnInfo(name = "condition") val condition: String
)