package com.android.taitasciore.privaliatest.feature.popularmovies.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.android.taitasciore.privaliatest.feature.popularmovies.PopularMoviesActionProcessor
import com.android.taitasciore.privaliatest.feature.popularmovies.PopularMoviesStateReducer
import javax.inject.Inject

/**
 * Factory class for PopularMoviesViewModel
 */
class PopularMoviesViewModelFactory @Inject constructor(
        private val popularMoviesActionProcessor: PopularMoviesActionProcessor,
        private val popularMoviesStateReducer: PopularMoviesStateReducer) : ViewModelProvider.Factory {

    /**
     * This method creates an instance of PopularMoviesViewModel
     * and it's necessary because the view model has a non-empty constructor
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PopularMoviesViewModel(popularMoviesActionProcessor, popularMoviesStateReducer) as T
    }
}