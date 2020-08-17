package com.example.selfevident.patterndatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.selfevident.casedatabase.Case

@Dao
interface PatternDao {

    @Query("SELECT * from pattern_table p inner join cross_table c ON p.id = c.pid WHERE cid = :id ORDER BY relationship ASC")
    fun getPatternsByCase(id: Int): List<Pattern>

    @Query("SELECT * from pattern_table WHERE id = :id ORDER BY relationship ASC")
    fun getPatternById(id: Int): List<Pattern>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(pattern: Pattern)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(cross: Cross)

    @Delete
    fun deletePattern(pattern: Pattern)

    @Delete
    fun deleteCross(cross: Cross)

    @Query("DELETE FROM pattern_table")
    fun deletePatterns()

    @Query("DELETE FROM cross_table")
    fun deleteCrosses()

}