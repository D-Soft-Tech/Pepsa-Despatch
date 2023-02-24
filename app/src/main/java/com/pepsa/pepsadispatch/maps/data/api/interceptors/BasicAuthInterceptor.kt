package com.pepsa.pepsadispatch.maps.data.api.interceptors

import com.pepsa.pepsadispatch.shared.utils.AppConstants.AUTHORIZATION
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class BasicAuthInterceptor(username: String, password: String) : Interceptor {
    private val credentials: String

    init {
        credentials = Credentials.basic(username, password)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val authenticationRequest: Request =
            request.newBuilder().header(AUTHORIZATION, credentials).build()
        return chain.proceed(authenticationRequest)
    }
}
