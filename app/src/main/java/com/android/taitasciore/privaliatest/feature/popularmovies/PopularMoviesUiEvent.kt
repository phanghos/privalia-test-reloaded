package com.android.taitasciore.privaliatest.feature.popularmovies

import com.android.taitasciore.privaliatest.presentation.base.UiEvent

sealed class PopularMoviesUiEvent : UiEvent {

    class Initial : PopularMoviesUiEvent()

    class LoadFirst : PopularMoviesUiEvent()

    class LoadNext : PopularMoviesUiEvent()

    class Retry : PopularMoviesUiEvent()

    data class RetrySearch(val query: String) : PopularMoviesUiEvent()

    data class Search(val query: String) : PopularMoviesUiEvent()

    data class LoadNextSearch(val query: String) : PopularMoviesUiEvent()
}