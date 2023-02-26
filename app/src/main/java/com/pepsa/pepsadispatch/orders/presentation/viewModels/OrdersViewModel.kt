package com.pepsa.pepsadispatch.orders.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pepsa.pepsadispatch.orders.data.models.OrderEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val gson: Gson,
    private val compositeDisposable: CompositeDisposable,
    @Named("io-scheduler") private val ioScheduler: Scheduler,
    @Named("main-thread-scheduler") private val mainThreadScheduler: Scheduler,
) : ViewModel() {
    private val _thereIsAPendingIncomingOrder: MutableLiveData<Boolean> = MutableLiveData(false)
    val thereIsAPendingIncomingOrder: LiveData<Boolean> get() = _thereIsAPendingIncomingOrder

    private val _incomingOrder: MutableLiveData<OrderEntity?> = MutableLiveData(null)
    val incomingOrder: LiveData<OrderEntity?> get() = _incomingOrder

    fun resetIncomingOrderToNull() {
        _incomingOrder.postValue(null)
    }

    fun setPendingIncomingOrderNotification(thereIsPendingNotification: Boolean) {
        _thereIsAPendingIncomingOrder.postValue(thereIsPendingNotification)
    }
}
