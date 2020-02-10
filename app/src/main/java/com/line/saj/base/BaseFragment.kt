package com.line.saj.base

import androidx.fragment.app.Fragment
import com.line.saj.App
import com.line.saj.components.view.activity.MainActivity

open class BaseFragment : Fragment() {

    fun showProgress() {
        App.getInstance().showProgress(activity!!)
    }

    fun hideProgress() {
        App.getInstance().hideProgress()
    }

    fun getMainAcitivity(): MainActivity {
        return activity as MainActivity
    }

}