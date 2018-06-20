package com.android.taitasciore.privaliatest.data.repository.impl

import com.android.taitasciore.privaliatest.data.entity.mapper.MovieMapper
import com.android.taitasciore.privaliatest.data.net.TmdbService
import com.android.taitasciore.privaliatest.data.repository.MovieRepository
import com.android.taitasciore.privaliatest.domain.model.Movie
import io.reactivex.Observable
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
        private val tmdbService: TmdbService,
        private val movieMapper: MovieMapper): MovieRepository {

    override fun getPopularMovies(page: Int): Observable<List<Movie>> {
        return tmdbService.getPopularMovies(page).map { movieMapper.map(it.results!!) }
    }

    override fun searchMovies(query: String, page: Int): Observable<List<Movie>> {
        return tmdbService.searchMovies(query, page).map { movieMapper.map(it.results!!) }
    }
}