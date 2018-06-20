package com.android.taitasciore.privaliatest.data.net

import com.android.taitasciore.privaliatest.data.entity.MovieListResponseEntity
import io.reactivex.Observable
import javax.inject.Inject

open class TmdbService @Inject constructor(private val tmdbApi: TmdbApi) : TmdbApi {

    override fun getPopularMovies(page: Int): Observable<MovieListResponseEntity> {
        return tmdbApi.getPopularMovies(page)
    }

    override fun searchMovies(query: String, page: Int): Observable<MovieListResponseEntity> {
        return tmdbApi.searchMovies(query, page)
    }
}