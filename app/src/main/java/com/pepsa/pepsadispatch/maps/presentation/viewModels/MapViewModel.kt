package com.pepsa.pepsadispatch.maps.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.pepsa.pepsadispatch.maps.data.models.enums.AppDestinations
import com.pepsa.pepsadispatch.maps.data.models.enums.AppDestinations.HOME
import com.pepsa.pepsadispatch.maps.domain.interactors.MapsApiRepository
import com.pepsa.pepsadispatch.maps.utils.MapUtils
import com.pepsa.pepsadispatch.maps.utils.MapsConstants.TAG_GET_DISTANCE_ERROR
import com.pepsa.pepsadispatch.maps.utils.MapsConstants.TAG_GET_ROUTE_ERROR
import com.pepsa.pepsadispatch.shared.utils.AppUtils.disposeWith
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
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
    // Route
    private val _routePolylineOptions: MutableLiveData<PolylineOptions> = MutableLiveData()
    val routePolylineOptions: LiveData<PolylineOptions> get() = _routePolylineOptions

    // Distance and Time
    private val _routeDistanceAndTime: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    val routeDistanceAndTime: LiveData<Pair<Int, Int>> get() = _routeDistanceAndTime

    private val _appCurrentDestination: MutableLiveData<AppDestinations> = MutableLiveData(HOME)
    val appCurrentDestination: LiveData<AppDestinations> get() = _appCurrentDestination

    fun getRoute(origin: LatLng, destination: LatLng) {
        getRouteBetweenTwoPoints(origin, destination)
            .subscribe { routeData, error ->
                routeData?.let {
                    _routePolylineOptions.postValue(
                        mapUtils.convertRouteFromLatLngToLineOption(it.first),
                    )
                }
                error?.let {
                    Timber.d("$TAG_GET_ROUTE_ERROR%s", it.localizedMessage)
                }
            }.disposeWith(compositeDisposable)
    }

    private fun getRouteBetweenTwoPoints(
        origin: LatLng,
        destination: LatLng,
    ): Single<Pair<ArrayList<List<LatLng>>, Pair<Int, Int>>> {
        val originString = "${origin.latitude},${origin.longitude}"
        val destinationString = "${destination.latitude},${destination.longitude}"
        return mapsApiRepository.getRoutePath(
            originString,
            destinationString,
        ).subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
    }

    fun getDistanceAndDurationBtwTwoLocations(origin: LatLng, destination: LatLng) {
        getRouteBetweenTwoPoints(origin, destination)
            .subscribe { routeData, error ->
                routeData?.let {
                    _routeDistanceAndTime.postValue(it.second!!)
                }
                error?.let {
                    Timber.d("$TAG_GET_DISTANCE_ERROR%s", it.localizedMessage)
                }
            }.disposeWith(compositeDisposable)
    }

    fun setAppDestination(appDestinations: AppDestinations) {
        _appCurrentDestination.postValue(appDestinations)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
