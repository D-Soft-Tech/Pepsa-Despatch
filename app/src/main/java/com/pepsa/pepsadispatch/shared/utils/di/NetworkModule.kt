package com.pepsa.pepsadispatch.shared.utils.di

import com.pepsa.pepsadispatch.maps.data.api.GetRouteDirectionApi
import com.pepsa.pepsadispatch.maps.data.api.apiImp.GetRouteDirectionApiImpl
import com.pepsa.pepsadispatch.maps.domain.interactors.MapsApiRepository
import com.pepsa.pepsadispatch.shared.utils.AppConstants.TIME_OUT_10
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun providesBaseUrl(): String = "https://maps.googleapis.com/"

    @Singleton
    @Provides
    fun providesLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun providesOKHTTPClient(
        loggingInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(TIME_OUT_10, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT_10, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT_10, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(loggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String,
    ): Retrofit =
        Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Singleton
    @Provides
    fun providesGetRouteDirectionApi(retrofit: Retrofit): GetRouteDirectionApi =
        retrofit.create(GetRouteDirectionApi::class.java)

    @Singleton
    @Provides
    fun providesMapsApiRepository(
        getRouteDirectionApiImpl: GetRouteDirectionApiImpl,
    ): MapsApiRepository = getRouteDirectionApiImpl
}
