package com.example.selfevident.patterndatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.selfevident.Converters

@Entity(tableName = "cross_table", primaryKeys = ["cid","pid"])
@TypeConverters(Converters::class)
data class Cross(
    @ColumnInfo(name = "cid") val cid: Int,
    @ColumnInfo(name = "pid") val pid: Int
)