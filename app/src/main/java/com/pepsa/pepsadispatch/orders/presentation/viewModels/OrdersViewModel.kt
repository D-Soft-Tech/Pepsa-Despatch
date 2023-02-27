package com.pepsa.pepsadispatch.orders.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pepsa.pepsadispatch.orders.domain.models.OrderDomain
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
    private val _incomingOrder: MutableLiveData<OrderDomain?> = MutableLiveData(null)
    val incomingOrder: LiveData<OrderDomain?> get() = _incomingOrder

    fun resetIncomingOrderToNull() {
        _incomingOrder.postValue(null)
    }

    fun setIncomingOrder(pendingOrder: OrderDomain) {
        _incomingOrder.postValue(pendingOrder)
    }

    fun acceptOrRejectOrder(acceptance: Boolean) {
        _incomingOrder.postValue(_incomingOrder.value!!.copy(accepted = acceptance))
    }
}
