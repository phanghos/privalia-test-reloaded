package com.android.taitasciore.privaliatest.data.net

import com.android.taitasciore.privaliatest.data.entity.MovieListResponseEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    companion object {
        val BASE_URL = "https://api.themoviedb.org/3/"
    }

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Observable<MovieListResponseEntity>

    @GET("search/movie")
    fun searchMovies(@Query("query") query: String, @Query("page") page: Int): Observable<MovieListResponseEntity>
}