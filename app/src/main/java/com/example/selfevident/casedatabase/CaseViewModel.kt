package com.example.selfevident.casedatabase

import android.app.Application
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
    fun insert(aCase: Case) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(aCase)
    }

    suspend fun getAll(): List<Case>{
        return repository.getAll()
    }

    suspend fun get(id: Int): List<Case>{
        return repository.get(id)
    }
}