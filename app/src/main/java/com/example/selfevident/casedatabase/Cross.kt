package com.example.selfevident.casedatabase

import android.app.Notification
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.TypeConverters

@Entity(
    tableName = "cross_table", primaryKeys = ["cid", "pid"], foreignKeys = [
        ForeignKey(
            entity = Case::class,
            parentColumns = ["id"],
            childColumns = ["cid"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Pattern::class,
            parentColumns = ["id"],
            childColumns = ["pid"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(Converters::class)
data class Cross(
    @ColumnInfo(name = "cid") val cid: Int,
    @ColumnInfo(name = "pid") val pid: String
)