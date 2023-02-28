package com.pepsa.pepsadispatch.orders.data.workers

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Data
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.pepsa.pepsadispatch.BuildConfig
import com.pepsa.pepsadispatch.maps.data.api.GetRouteDirectionApi
import com.pepsa.pepsadispatch.maps.data.models.GetMapRouteMode
import com.pepsa.pepsadispatch.orders.data.models.OrderEntity
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.STRING_GET_ROUTE_ORDER_WORKER_INPUT_DATA_TAG
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.STRING_GET_ROUTE_ORDER_WORKER_OUTPUT_DATA_TAG
import com.pepsa.pepsadispatch.orders.utils.manualNetworkOperations.GetRetrofit.providesGetRouteDirectionApi
import io.reactivex.Single
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class GetOrderRouteWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val gson: Gson = Gson(),
    private val getRouteApi: GetRouteDirectionApi = providesGetRouteDirectionApi(),
) : RxWorker(context, workerParameters) {
    /*
    private val ioScheduler: Scheduler = Schedulers.io()
    private val mainThreadSchedulers: Scheduler = AndroidSchedulers.mainThread()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val gson: Gson = Gson()
    private val getRouteApi: GetRouteDirectionApi = providesGetRouteDirectionApi()
    * */
    override fun createWork(): Single<Result> {
        val inputData = inputData.getString(STRING_GET_ROUTE_ORDER_WORKER_INPUT_DATA_TAG)
        val incomingOrderEntity = gson.fromJson(inputData, OrderEntity::class.java)
        val origin = LatLng(incomingOrderEntity.pickUpLatitude, incomingOrderEntity.pickUpLongitude)
        val destination = LatLng(
            incomingOrderEntity.destinationLatitude,
            incomingOrderEntity.destinationLongitude,
        )
        val originString = "${origin.latitude},${origin.longitude}"
        val destinationString = "${destination.latitude},${destination.longitude}"

        return getRouteApi.getRouteDirection2(
            originString,
            destinationString,
            false,
            GetMapRouteMode.MODE_DRIVING.mode,
            BuildConfig.PLACE_API_KEY,
        ).flatMap {
            if (it.isSuccessful) {
                val outputValue = gson.toJson(it.body())
                val outputData = Data.Builder()
                    .putString(STRING_GET_ROUTE_ORDER_WORKER_OUTPUT_DATA_TAG, outputValue)
                    .build()
                Single.just(Result.success(outputData))
            } else {
                val outputData = Data.Builder()
                    .putString(STRING_GET_ROUTE_ORDER_WORKER_OUTPUT_DATA_TAG, "")
                    .build()
                Single.just(Result.failure(outputData))
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun getBackgroundExecutor(): Executor =
        Executors.newSingleThreadExecutor()
}
