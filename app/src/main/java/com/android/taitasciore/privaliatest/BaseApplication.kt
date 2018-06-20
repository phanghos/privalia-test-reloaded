package com.android.taitasciore.privaliatest

import android.app.Application
import com.android.taitasciore.privaliatest.dagger.AppComponent
import com.android.taitasciore.privaliatest.dagger.DaggerAppComponent

/**
 * Base application class
 * It injects Dagger AppComponent
 */
class BaseApplication : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
                .app(this)
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}