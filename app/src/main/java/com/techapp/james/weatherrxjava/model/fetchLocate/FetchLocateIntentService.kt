package com.techapp.james.weatherrxjava.model.fetchLocate

import android.app.IntentService
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import com.techapp.james.weatherrxjava.ui.main.MainActivity
import timber.log.Timber
import java.util.*


class FetchLocateIntentService : IntentService("FetchLocateIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        val geocoder = Geocoder(this, Locale.ENGLISH)
        var location = intent!!.getParcelableExtra<Location>(MainActivity.REQUEST_SERVICE)
        var address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        Timber.d("address ")
        for (i in address.indices) {
            Timber.d("address " + address[i].countryName)
            Timber.d("address " + address[i].adminArea)

            var i = Intent(MainActivity.LOCATE_RECEIVER)
            i.putExtra(MainActivity.LOCATE_RECEIVER, address[0])
            sendBroadcast(i)
        }

    }
}
