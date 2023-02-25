package com.pepsa.pepsadispatch.shared.utils.rxJava

import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

object RxJavaUtils {
    fun Disposable.disposeWith(compositeDisposable: CompositeDisposable) {
        compositeDisposable.add(this)
    }

    fun <T> handleBackgroundObservableTask(
        ioScheduler: Scheduler,
        mainThreadScheduler: Scheduler,
        compositeDisposable: CompositeDisposable,
        logTag: String = "",
    ): ObservableTransformer<T, T> =
        ObservableTransformer { observable ->
            observable.subscribeOn(ioScheduler)
                .observeOn(mainThreadScheduler)
                .doOnError { throwable ->
                    Timber.d("${logTag}ERROR====>%s", throwable.localizedMessage)
                }
                .doOnSubscribe {
                    it.disposeWith(compositeDisposable)
                    Timber.d("${logTag}STARTED====>%s", "$logTag Started")
                }
                .doOnComplete {
                    Timber.d("${logTag}COMPLETED====>%s", "$logTag Started")
                }
        }

    fun <T> handleBackgroundSingleTask(
        ioScheduler: Scheduler,
        mainThreadScheduler: Scheduler,
        compositeDisposable: CompositeDisposable,
        logTag: String = "",
    ): SingleTransformer<T, T> =
        SingleTransformer { single ->
            single.subscribeOn(ioScheduler)
                .observeOn(mainThreadScheduler)
                .doOnError { throwable ->
                    Timber.d("${logTag}ERROR====>%s", throwable.localizedMessage)
                }
                .doOnSubscribe {
                    it.disposeWith(compositeDisposable)
                    Timber.d("${logTag}STARTED====>%s", "$logTag Started")
                }
        }
}
