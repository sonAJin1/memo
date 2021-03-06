package com.line.saj


import android.app.Activity
import android.app.Application
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import java.util.concurrent.CopyOnWriteArrayList

class App : Application() {

    private var dialog: AppCompatDialog? = null

    companion object {

        private var instance: App? = null

        fun getInstance(): App {
            if (instance == null) {
                instance = App()
            }

            return instance!!
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) {
        }

    }

    fun showProgress(activity: Activity?) {


        try {
            if (activity == null || activity.isFinishing) return


            if (dialog != null && dialog!!.isShowing) return


            dialog = AppCompatDialog(activity)
            dialog!!.setCancelable(false)
            dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.setContentView(R.layout.progress_loading)
            dialog!!.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun hideProgress() {

        try {
            if (dialog != null && dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        } catch (e: IllegalArgumentException) {

        }
    }


}