package com.android.taitasciore.privaliatest.data.repository

import com.android.taitasciore.privaliatest.domain.model.Movie
import io.reactivex.Observable

interface MovieRepository {

    fun getPopularMovies(page: Int): Observable<List<Movie>>

    fun searchMovies(query: String, page: Int): Observable<List<Movie>>
}