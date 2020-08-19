package com.example.selfevident.casedatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "pattern_table")
@TypeConverters(Converters::class)
data class Pattern(
    @PrimaryKey @ColumnInfo(name = "id") val relationship: String
)