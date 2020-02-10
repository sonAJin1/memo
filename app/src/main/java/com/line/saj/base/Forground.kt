package com.line.saj.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler

class Foreground : Application.ActivityLifecycleCallbacks {

    private val CHECK_DELAY: Long = 3000
    private val handler = Handler()
    private var check: Runnable? = null


    interface Listener {
        fun foreground(activity: Activity?)
        fun background(activity: Activity?)
    }


    companion object {
        private var instance: Foreground? = null

        fun init(application: Application) {
            if (instance == null) {
                instance = Foreground()
                application.registerActivityLifecycleCallbacks(instance)
            }

        }

        fun addListener(listener: Listener) {
            instance?.addListener(listener)
        }


        fun get(application: Application): Foreground? {
            if (instance == null)
                init(application)
            return instance
        }

        fun get(ctx: Context): Foreground? {
            if (instance == null) {
                val appCtx = ctx.applicationContext
                if (appCtx is Application) {
                    init(appCtx)
                }
                throw IllegalStateException(
                    "Foreground is not initialised and " + "cannot obtain the Application object"
                )
            }
            return instance
        }

        fun get(): Foreground? {
            if (instance == null) {
                throw IllegalStateException(
                    "Foreground is not initialised and " + "cannot obtain the Application object"
                )
            }
            return instance
        }

    }

    val listeners: ArrayList<Listener> = ArrayList()
    var numStarted = 0

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun isForeground(): Boolean {
        return (numStarted > 0)
    }

    fun isBackground(): Boolean {
        return (numStarted == 0)
    }

    private fun notifyForeground(activity: Activity?) {
        listeners.forEach { listener ->
            listener.foreground(activity)
        }
    }

    private fun notifyBackground(activity: Activity?) {
        listeners.forEach { listener ->
            listener.background(activity)
        }
    }

    override fun onActivityPaused(activity: Activity?) {}

    override fun onActivityResumed(activity: Activity?) {}

    override fun onActivityStarted(activity: Activity?) {

        if (check != null)
            handler.removeCallbacks(check)

        //  Notify before increment
        if (numStarted == 0) {
            notifyForeground(activity)
        }

        numStarted++

    }

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityStopped(activity: Activity?) {
        numStarted--
        //  Notify after decrement


        if (check != null)
            handler.removeCallbacks(check)

        check = Runnable {
            if (numStarted == 0) {
                notifyBackground(activity)
            } else {
            }
        }

        handler.postDelayed(check, CHECK_DELAY)
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}

}