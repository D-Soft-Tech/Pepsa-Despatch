package com.pepsa.pepsadispatch.orders.utils.manualNetworkOperations

import com.pepsa.pepsadispatch.maps.data.api.GetRouteDirectionApi
import com.pepsa.pepsadispatch.maps.utils.MapsConstants
import com.pepsa.pepsadispatch.shared.utils.AppConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GetRetrofit {
    private fun providesBaseUrl(): String = MapsConstants.MAP_GET_ROUTE_BASE_URL

    private fun providesLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private fun providesOKHTTPClient(): OkHttpClient {
        val loggingInterceptor: Interceptor = providesLoggingInterceptor()
        return OkHttpClient().newBuilder()
            .connectTimeout(AppConstants.TIME_OUT_10, TimeUnit.SECONDS)
            .readTimeout(AppConstants.TIME_OUT_10, TimeUnit.SECONDS)
            .writeTimeout(AppConstants.TIME_OUT_10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private fun providesRetrofit(): Retrofit {
        val okHttpClient: OkHttpClient = providesOKHTTPClient()
        val baseUrl: String = providesBaseUrl()

        return Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun providesGetRouteDirectionApi(): GetRouteDirectionApi {
        val retrofit = providesRetrofit()
        return retrofit.create(GetRouteDirectionApi::class.java)
    }
}
