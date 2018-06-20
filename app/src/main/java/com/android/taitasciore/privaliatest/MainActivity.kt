package com.android.taitasciore.privaliatest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.taitasciore.privaliatest.feature.popularmovies.view.PopularMoviesFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            launchPopularMoviesFragment()
        }
    }

    private fun launchPopularMoviesFragment() {
        supportFragmentManager.beginTransaction().add(R.id.container, PopularMoviesFragment()).commit()
    }
}
