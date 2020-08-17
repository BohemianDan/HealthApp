package com.example.selfevident.patterndatabase

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.selfevident.casedatabase.Case
import com.example.selfevident.casedatabase.CaseDao

class PatternRepository(private val patternDao: PatternDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(pattern: Pattern) {
        patternDao.insert(pattern)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(cross: Cross) {
        patternDao.insert(cross)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun get(id: Int): List<Pattern>{
        return patternDao.getPatternById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAll(cid: Int): List<Pattern> {
        return patternDao.getPatternsByCase(cid)
    }
}