package com.pepsa.pepsadispatch.orders.data.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.pepsa.pepsadispatch.BuildConfig
import com.pepsa.pepsadispatch.maps.data.api.GetRouteDirectionApi
import com.pepsa.pepsadispatch.maps.data.models.GetMapRouteMode
import com.pepsa.pepsadispatch.maps.domain.models.MapData
import com.pepsa.pepsadispatch.orders.data.models.OrderEntity
import com.pepsa.pepsadispatch.orders.domain.interactors.GetOrderRouteCallBack
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.STRING_GET_ROUTE_ORDER_WORKER_INPUT_TAG
import com.pepsa.pepsadispatch.orders.utils.manualNetworkOperations.GetRetrofit.providesGetRouteDirectionApi
import com.pepsa.pepsadispatch.shared.utils.AppUtils.disposeWith
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.CountDownLatch

class GetOrderRouteWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val getOrderRouteCallBack: GetOrderRouteCallBack,
    private val ioScheduler: Scheduler = Schedulers.io(),
    private val mainThreadSchedulers: Scheduler = AndroidSchedulers.mainThread(),
    private val compositeDisposable: CompositeDisposable = CompositeDisposable(),
    private val gson: Gson = Gson(),
    private val getRouteApi: GetRouteDirectionApi = providesGetRouteDirectionApi(),
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        val inputData = inputData.getString(STRING_GET_ROUTE_ORDER_WORKER_INPUT_TAG)
        val incomingOrderEntity = gson.fromJson(inputData, OrderEntity::class.java)
        val origin = LatLng(incomingOrderEntity.pickUpLatitude, incomingOrderEntity.pickUpLongitude)
        val destination = LatLng(
            incomingOrderEntity.destinationLatitude,
            incomingOrderEntity.destinationLongitude,
        )
        val originString = "${origin.latitude},${origin.longitude}"
        val destinationString = "${destination.latitude},${destination.longitude}"

        val latch = CountDownLatch(1)
        var result: MapData? = null
        getRouteApi.getRouteDirection(
            originString,
            destinationString,
            false,
            GetMapRouteMode.MODE_DRIVING.mode,
            BuildConfig.PLACE_API_KEY,
        ).subscribeOn(ioScheduler)
            .observeOn(mainThreadSchedulers)
            .subscribe { route, error ->
                route.let {
                    result = it
                    getOrderRouteCallBack.onGetRouteSuccess(it)
                    latch.countDown()
                }
                error.let {
                    result = null
                    it?.localizedMessage?.let { errorMessage ->
                        getOrderRouteCallBack.onGetRouteError(
                            errorMessage,
                        )
                    }
                    latch.countDown()
                }
            }.disposeWith(compositeDisposable)
        latch.await()
        return if (result != null) Result.success() else Result.failure()
    }
}
