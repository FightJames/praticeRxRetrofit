package com.techapp.james.todolistrxjava

import com.google.gson.annotations.SerializedName

class Weather(var name: String,
              var main: Main
) {
    class Main(var temp: String,
               var pressure: String,
               var humidity: String,
               var temp_min: String,
               var temp_max: String)
}