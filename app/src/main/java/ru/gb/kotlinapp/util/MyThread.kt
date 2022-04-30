package ru.gb.kotlinapp.util

import android.os.Handler
import android.os.Looper

class MyThread : Thread() {
    var handler: Handler? = null
    override fun run() {
        Looper.prepare()
        handler = Handler(Looper.myLooper()!!)
        Looper.loop()
    }
}