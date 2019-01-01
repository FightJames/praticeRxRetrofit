package com.techapp.james.weatherrxjava.model.weatherAPI

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GetWheather {
    @GET("weather")
    fun getCurrentWheather(@Query("q") place: String, @Query("appid") key:String): Single<Weather>
}