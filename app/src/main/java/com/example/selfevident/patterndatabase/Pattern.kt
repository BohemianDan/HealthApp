package com.example.selfevident.patterndatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.selfevident.Converters
import java.sql.Date

@Entity(tableName = "pattern_table")
@TypeConverters(Converters::class)
data class Pattern(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "relationship") val relationship: String
)