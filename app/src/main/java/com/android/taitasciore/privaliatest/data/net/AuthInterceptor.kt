package com.android.taitasciore.privaliatest.data.net

import com.android.taitasciore.privaliatest.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * This class intercepts HTTP requests made with Retrofit
 * and appends api_key query parameter to every request
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain?.request()!!
        val url = request.url()
        val newUrl = url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()
        val newRequest = request.newBuilder()
                .url(newUrl)
                .build()
        return chain.proceed(newRequest)
    }
}