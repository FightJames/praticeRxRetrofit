package com.techapp.james.weatherrxjava.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import com.techapp.james.weatherrxjava.R
import com.techapp.james.weatherrxjava.model.fetchLocate.FetchLocateIntentService
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import com.techapp.james.weatherrxjava.model.weatherAPI.RetrofitManager
import com.techapp.james.weatherrxjava.model.weatherAPI.Weather
import io.reactivex.disposables.Disposable


class MainActivity : AppCompatActivity() {

    private val locateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action.equals(LOCATE_RECEIVER)) {
                    var address = it.getParcelableExtra<Address>(LOCATE_RECEIVER)
                    Timber.d(address.toString())

                    var observer = object : SingleObserver<Weather> {
                        override fun onSuccess(t: Weather) {
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onError(e: Throwable) {
                            try {
                                e
                            } catch (ex: Exception) {
                                e.printStackTrace()
                            }
                            Timber.d("onObserver Error")
                        }
                    }

                    var retrofitManager = RetrofitManager.getInstance(this@MainActivity)
                    //it will get error here.
                    val ob = retrofitManager.getWeather(address.adminArea)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError { onError ->

                            }
                            .doOnSuccess { resp ->
                                //                    Log.d(TAG, resp.toString())
                                Timber.d(resp.name)

                                Timber.d(resp.main.temp_max)

                                Timber.d(resp.main.temp)

                                Timber.d(resp.main.temp_min)

                                Timber.d(resp.main.humidity)

                                Timber.d(resp.main.pressure)
                            }
                    ob.subscribe(observer)

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.activity_main)
        registerReceiver()
        if (applyRight()) {
            getLastLocation()
        }

//        var retrofitManager = RetrofitManager.getInstance(this)
//
//        val ob = retrofitManager.getWeather("Taipei")
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSuccess { resp ->
//                    //                    Log.d(TAG, resp.toString())
//                    Timber.d(resp.name)
//
//                    Timber.d(resp.main.temp_max)
//
//                    Timber.d(resp.main.temp)
//
//                    Timber.d(resp.main.temp_min)
//
//                    Timber.d(resp.main.humidity)
//
//                    Timber.d(resp.main.pressure)
//                }
//        ob.subscribe({}, { throwable -> Timber.d("hello") })


        var furtureTarget = Glide.with(this).load(R.drawable.weather_app_sunny).submit(root.measuredWidth, root.measuredHeight)

        Observable.create(object : ObservableOnSubscribe<Drawable> {
            override fun subscribe(emitter: ObservableEmitter<Drawable>) {
                var drawable = furtureTarget.get()
                emitter.onNext(drawable)
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { repc ->
                    root.background = repc
                }.subscribe()
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            Timber.d("PASS")
            if (location == null) {
                // request current location
                var mLocationMgr: LocationManager
                var mLocationPro: String
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mLocationMgr = this.getSystemService(LocationManager::class.java)
                } else {
                    mLocationMgr = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                }
                mLocationPro = LocationManager.GPS_PROVIDER


                mLocationMgr.requestLocationUpdates(mLocationPro, 0L, 0f, object : LocationListener {
                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle?) {
                        Timber.d("PASS Status")
                    }

                    override fun onProviderEnabled(provider: String) {
                        Timber.d("PASS ProviderEnable")
                    }

                    override fun onProviderDisabled(provider: String) {
                        Timber.d("PASS ProviderDisabled $provider")
//                        val intent = Intent("android.location.GPS_ENABLED_CHANGE")
//                        intent.putExtra("enabled", true)
//                        sendBroadcast(intent)

//                        startActivity(context, new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        
                    }

                    override fun onLocationChanged(location: Location?) {
                        Timber.d("PASS LocationChanged")
                        if (location == null) {
                            Toast.makeText(this@MainActivity, "Can't fetch location", Toast.LENGTH_LONG).show()
                        } else {
                            var i = Intent(this@MainActivity, FetchLocateIntentService::class.java)
                            i.putExtra(REQUEST_SERVICE, location)
                            startService(i)
                        }
                    }
                })

            } else {
                Timber.d("PASS1")
                var i = Intent(this, FetchLocateIntentService::class.java)
                i.putExtra(REQUEST_SERVICE, location)
                startService(i)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var judge = true
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            for (i in 0..(grantResults.size - 1)) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    judge = false
                }
            }
        }
        if (!judge) {
            finish()
        } else {
            getLastLocation()
        }
    }

    fun applyRight(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)  // add right which you need
            for (e: String in permission) {
                if (this.checkSelfPermission(e) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permission,
                            REQUEST_CODE_ASK_PERMISSIONS)
                    return false
                }
            }
        }
        return true
    }

    fun registerReceiver() {
        var iFilter = IntentFilter()
        iFilter.addAction(LOCATE_RECEIVER)
        registerReceiver(locateReceiver, iFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(locateReceiver)
    }

    companion object {
        val REQUEST_CODE_ASK_PERMISSIONS = 0
        val REQUEST_SERVICE = "request_service"
        val LOCATE_RECEIVER = "locate_receiver"
    }
}
