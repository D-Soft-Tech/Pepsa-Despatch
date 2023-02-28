package com.pepsa.pepsadispatch.shared.utils.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesGson(): Gson = Gson()

    @Singleton
    @Provides
    fun providesCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Singleton
    @Provides
    @Named("io-scheduler")
    fun providesIoScheduler(): Scheduler = Schedulers.io()

    @Singleton
    @Provides
    @Named("main-thread-scheduler")
    fun providesMainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
