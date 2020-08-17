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
    val allWords: LiveData<List<Case>> = caseDao.getAlphabetizedWords()

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(aCase: Case) {
        caseDao.insert(aCase)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun get(id: Int): List<Case>{
        return caseDao.getCaseById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAll(): List<Case> {
        return caseDao.getAllWords()
    }
}