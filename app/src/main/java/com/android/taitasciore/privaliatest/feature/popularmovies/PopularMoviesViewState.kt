package com.android.taitasciore.privaliatest.feature.popularmovies

import com.android.taitasciore.privaliatest.domain.model.Movie
import com.android.taitasciore.privaliatest.presentation.base.ViewState

data class PopularMoviesViewState(
        val loading: Boolean = false,
        val movies: List<Movie>? = null,
        val error: Throwable? = null,
        val showInitialError: Boolean? = false,
        val showSnackbarError: Boolean? = false,
        val search: Boolean = false
) : ViewState