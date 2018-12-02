package com.techapp.james.todolistrxjava

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.techapp.james.todolistrxjava.gsonData.ConvertWeather
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {

    private var retrofit: Retrofit
    private var key: String

    constructor(context: Context) {
        retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(object :CallAdapter.Factory(){})
                .build()
        key = context.getString(R.string.wheatherKey)
    }

    companion object {
        private var instance: RetrofitManager? = null
        fun getInstance(context: Context): RetrofitManager {
            if (instance == null) {
                instance = RetrofitManager(context)
            }
            return instance!!
        }
    }

    fun getWeather(place: String) = retrofit.create(GetWheather::class.java).getCurrentWheather(place, key)
}