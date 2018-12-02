package com.techapp.james.todolistrxjava

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "James"
    }



    var observer = object : Observer<String> {
        override fun onSubscribe(d: Disposable) {
            Log.d(TAG, "onSubscribe ${d.isDisposed.toString()}")
        }

        override fun onNext(t: String) {
            Log.d(TAG, "onNext $t")
        }

        override fun onError(e: Throwable) {

            Log.d(TAG, "onError ")
        }

        override fun onComplete() {
            Log.d(TAG, "onComplete ")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var test = RetrofitManager.getInstance(this)
        val ob = test.getWeather("Taipei")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { resp ->
                    Log.d(TAG, resp.toString())
                }
        ob.subscribe()

//        cancel
//        ob.dispose()

        Flowable.just("Hello world").subscribe(System.out::println)

        var ob1 = Observable.just("hello", "world")
        var ob2 = Observable.just(1, 2, 3, 4, 5)
        Observable.combineLatest(
                ob1,
                ob2,
                BiFunction<String, Int, String> { t1, t2 -> t1 + t2 }
        ).doOnNext {
            it
            Log.d(TAG, "DoOnNext")
        }.subscribe(observer)


        Observable.create(ObservableOnSubscribe<Weather>() { emitter ->

        })


        var k = ArrayList<String>()
        var observable = Observable.create(ObservableOnSubscribe<Int>() { emitter ->
            emitter.setDisposable(object : Disposable {
                override fun dispose() {


                }

                override fun isDisposed(): Boolean {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
            if (!emitter.isDisposed) {
                emitter.onNext(1)
                emitter.onNext(2)
            }
//            emitter.onError(Exception())
//            emitter.onComplete()

        })
                .doOnNext { Log.d(TAG, "Int" + it) }
                .map { it.toString() }
                .doOnNext { Log.d(TAG, it) }
                .subscribe()

//        observable.subscribe()
//        ob    servable
//        observable.doOnComplete { }



    }
}
