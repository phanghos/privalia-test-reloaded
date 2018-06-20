package com.android.taitasciore.privaliatest.domain.model

data class MovieListResponse(
		val page: Int? = null,
		val totalPages: Int? = null,
		val results: List<Movie>? = null,
		val totalResults: Int? = null
)
