package com.techapp.james.todolistrxjava.gsonData

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.techapp.james.todolistrxjava.Weather
import java.lang.reflect.Type

class ConvertWeather : JsonDeserializer<Weather> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Weather? {
        var name: String = json!!.asJsonObject.get("name").asString
        var main = json!!.asJsonObject.get("main").asJsonObject
        var temp = main.get("temp").asString
        var pressure = main.get("pressure").asString
        var humidity = main.get("humidity").asString
        var temp_min = main.get("temp_min").asString
        var temp_max = main.get("temp_max").asString
//        return Weather(name, temp, pressure, humidity, temp_min, temp_max)
        return null
    }
}