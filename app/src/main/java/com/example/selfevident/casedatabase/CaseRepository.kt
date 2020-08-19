package com.example.selfevident.casedatabase

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class CaseRepository(private val caseDao: CaseDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<Case>> = caseDao.getAlphabetizedCases()

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.

    //Cases:
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(aCase: Case) {
        caseDao.insert(aCase)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getCaseById(id: Int): List<Case> {
        return caseDao.getCaseById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAllCases(): List<Case> {
        return caseDao.getAllCases()
    }

    //Pattern and Cross:
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getPatternsByCase(cid: Int): List<Pattern> {
        return caseDao.getPatternsByCase(cid)
    }

    //Pattern and Cross:
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAllPatterns(): List<Pattern> {
        return caseDao.getAllPatterns()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getPatternById(id: String): List<Pattern> {
        return caseDao.getPatternById(id)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(pattern: Pattern) {
        return caseDao.insert(pattern)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(cross: Cross) {
        return caseDao.insert(cross)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updatePattern(old: String, new: String) {
        return caseDao.updatePattern(old, new)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(cross: Cross) {
        return caseDao.deleteCross(cross)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(pattern: Pattern) {
        return caseDao.deletePattern(pattern)
    }


}