package com.techapp.james.weatherrxjava.model.weatherAPI

class Weather(var name: String,
              var main: Main
) {
    class Main(var temp: String,
               var pressure: String,
               var humidity: String,
               var temp_min: String,
               var temp_max: String)
}