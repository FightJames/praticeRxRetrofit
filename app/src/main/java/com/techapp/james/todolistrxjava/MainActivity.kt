package com.techapp.james.todolistrxjava

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.WindowManager
import com.bumptech.glide.Glide
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "James"
    }
//    var observer = object : Observer<String> {
//        override fun onSubscribe(d: Disposable) {
//            Log.d(TAG, "onSubscribe ${d.isDisposed.toString()}")
//        }
//
//        override fun onNext(t: String) {
//            Log.d(TAG, "onNext $t")
//        }
//
//        override fun onError(e: Throwable) {
//
//            Log.d(TAG, "onError ")
//        }
//
//        override fun onComplete() {
//            Log.d(TAG, "onComplete ")
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.activity_main)

        var retrofitManager = RetrofitManager.getInstance(this)
        val ob = retrofitManager.getWeather("Taipei")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { resp ->
                    //                    Log.d(TAG, resp.toString())
                    Timber.d(resp.name)

                    Timber.d(resp.main.temp_max)

                    Timber.d(resp.main.temp)

                    Timber.d(resp.main.temp_min)

                    Timber.d(resp.main.humidity)

                    Timber.d(resp.main.pressure)
                }
        ob.subscribe()

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

        Single.create(object : SingleOnSubscribe<Drawable> {
            override fun subscribe(emitter: SingleEmitter<Drawable>) {
                var drawable = furtureTarget.get()
                emitter.onSuccess(drawable)
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { repc ->
                    root.background = repc
                }.subscribe()
    }
}
