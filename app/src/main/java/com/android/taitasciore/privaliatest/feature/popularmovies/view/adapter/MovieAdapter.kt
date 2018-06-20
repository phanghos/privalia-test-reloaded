package com.android.taitasciore.privaliatest.feature.popularmovies.view.adapter

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.taitasciore.privaliatest.R
import com.android.taitasciore.privaliatest.domain.model.Movie
import com.android.taitasciore.privaliatest.utils.parseSimpleDate
import com.facebook.drawee.view.SimpleDraweeView
import java.util.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    companion object {
        const val BASE_URL_IMG = "https://image.tmdb.org/t/p/w500"
    }

    private val movies = mutableListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        var releaseDate: Calendar? = null

        if (!movie.releaseDate!!.isEmpty()) {
            releaseDate = movie.releaseDate!!.parseSimpleDate()
        }

        var releaseYearStr = if (releaseDate == null) "" else "(${releaseDate!!.get(Calendar.YEAR)})"

        var posterPath = movie.posterPath
        if (posterPath == null || posterPath.isEmpty()) {
            holder?.draweeViewPoster.visibility = View.GONE
        } else {
            holder?.draweeViewPoster.visibility = View.VISIBLE
        }

        holder?.apply {
            title.text = movie.title!!
            releaseYear.text = releaseYearStr
            overview.text = movie.overview
            draweeViewPoster.setImageURI(Uri.parse(BASE_URL_IMG + posterPath))
        }
    }

    fun setList(movies: List<Movie>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.text_view_movie_title)

        val releaseYear: TextView = view.findViewById(R.id.text_view_movie_relese_year)

        val overview: TextView = view.findViewById(R.id.text_view_movie_overview)

        val draweeViewPoster: SimpleDraweeView = view.findViewById(R.id.drawee_view_poster)
    }
}