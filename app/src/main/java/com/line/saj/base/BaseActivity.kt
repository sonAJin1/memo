package com.line.saj.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.line.saj.App

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun showProgress() {
        App.getInstance().showProgress(this)
    }

    fun hideProgress() {
        App.getInstance().hideProgress()
    }

    fun showToast(msg: String){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
    }


}