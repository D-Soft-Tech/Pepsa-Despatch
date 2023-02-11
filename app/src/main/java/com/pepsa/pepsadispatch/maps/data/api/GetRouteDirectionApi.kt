package com.pepsa.pepsadispatch.maps.data.api

import com.pepsa.pepsadispatch.maps.domain.models.MapData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GetRouteDirectionApi {
    @GET("maps/api/directions/json")
    fun getRouteDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("sensor") sensor: Boolean,
        @Query("mode") mode: String,
        @Query("key") key: String,
    ): Single<MapData>
}
