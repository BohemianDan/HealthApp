package com.example.selfevident.casedatabase

import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * The Room Magic is in this file, where you map a Java method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
interface CaseDao {

    //Case

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from case_table ORDER BY datetime ASC")
    fun getAlphabetizedCases(): LiveData<List<Case>>

    @Query("SELECT * from case_table ORDER BY datetime ASC")
    fun getAllCases(): List<Case>

    @Query("SELECT * from case_table WHERE id = :id ORDER BY datetime ASC")
    fun getCaseById(id: Int): List<Case>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(aCase: Case)

    @Query("DELETE FROM case_table")
    fun deleteAll()

    //Pattern and Cross:
    @Query("SELECT * from pattern_table p inner join cross_table c ON p.id = c.pid WHERE cid = :cid ORDER BY id ASC")
    fun getPatternsByCase(cid: Int): List<Pattern>

    @Query("SELECT * from pattern_table WHERE id = :id ORDER BY id ASC")
    fun getPatternById(id: String): List<Pattern>

    @Query("SELECT * from pattern_table ORDER BY id ASC")
    fun getAllPatterns(): List<Pattern>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pattern: Pattern)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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