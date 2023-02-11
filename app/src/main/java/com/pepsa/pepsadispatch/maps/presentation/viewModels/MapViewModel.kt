package com.pepsa.pepsadispatch.maps.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.pepsa.pepsadispatch.maps.domain.interactors.MapsApiRepository
import com.pepsa.pepsadispatch.maps.utils.MapUtils
import com.pepsa.pepsadispatch.maps.utils.MapsConstants.TAG_GET_ROUTE_ERROR
import com.pepsa.pepsadispatch.shared.utils.AppUtils.disposeWith
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapsApiRepository: MapsApiRepository,
    private val mapUtils: MapUtils,
    private val gson: Gson,
    private val compositeDisposable: CompositeDisposable,
    @Named("io-scheduler") private val ioScheduler: Scheduler,
    @Named("main-thread-scheduler") private val mainThreadScheduler: Scheduler,
) : ViewModel() {
    private val _routePolylineOptions: MutableLiveData<PolylineOptions> = MutableLiveData()
    val routePolylineOptions: LiveData<PolylineOptions> get() = _routePolylineOptions

    fun getRoute(origin: LatLng, destination: LatLng) {
        val originString = "${origin.latitude},${origin.longitude}"
        val destinationString = "${destination.latitude},${destination.longitude}"
        mapsApiRepository.getRoutePath(
            originString,
            destinationString,
        ).subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe { routeData, error ->
                routeData?.let {
                    _routePolylineOptions.postValue(
                        mapUtils.convertRouteFromLatLngToLineOption(it),
                    )
                }
                error?.let {
                    Timber.d("$TAG_GET_ROUTE_ERROR%s", it.localizedMessage)
                }
            }.disposeWith(compositeDisposable)
    }
}
