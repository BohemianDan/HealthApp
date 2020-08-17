package com.example.selfevident.casedatabase

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


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

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from case_table ORDER BY emotion ASC")
    fun getAlphabetizedWords(): LiveData<List<Case>>

    @Query("SELECT * from case_table ORDER BY emotion ASC")
    fun getAllWords(): List<Case>

    @Query("SELECT * from case_table WHERE id = :id ORDER BY emotion ASC")
    fun getCaseById(id: Int): List<Case>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(aCase: Case)

    @Query("DELETE FROM case_table")
    fun deleteAll()
}