package com.android.taitasciore.privaliatest.dagger

import android.app.Application
import com.android.taitasciore.privaliatest.BaseApplication
import com.android.taitasciore.privaliatest.feature.popularmovies.view.PopularMoviesFragment
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class])
interface AppComponent : AndroidInjector<BaseApplication> {

    fun inject(popularMoviesFragment: PopularMoviesFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(app: Application): Builder
        fun build(): AppComponent
    }
}