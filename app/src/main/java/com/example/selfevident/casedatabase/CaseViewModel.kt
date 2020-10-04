package com.example.selfevident.casedatabase

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class CaseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CaseRepository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<Case>>

    init {
        val wordsDao = CaseRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = CaseRepository(wordsDao)
        allWords = repository.allWords
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    suspend fun insert(aCase: Case): Long {
        return repository.insert(aCase)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(pattern: Pattern) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(pattern)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(cross: Cross) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(cross)
    }


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun delete(pattern: Pattern) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(pattern)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun delete(cross: Cross) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(cross)
    }

    fun updatePattern(old: String, new: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.updatePattern(old, new)
    }

    suspend fun getAllCases(): List<Case> {
        return repository.getAllCases()
    }

    suspend fun getCaseById(id: Int): List<Case> {
        return repository.getCaseById(id)
    }


    suspend fun getPatternById(id: String): List<Pattern> {
        return repository.getPatternById(id)
    }

    suspend fun getPatternsByCase(cid: Int): List<Pattern> {
        return repository.getPatternsByCase(cid)
    }

    suspend fun getAllPatterns(): List<Pattern> {
        return repository.getAllPatterns()
    }

    fun getCrossesByPattern(pid: String): List<Cross>{
        return repository.getCrossesByPattern(pid)
    }
}