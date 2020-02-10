package com.line.saj.components.view.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.line.saj.R
import com.line.saj.base.BaseActivity
import com.line.saj.components.viewModel.MemoListViewModel
import com.line.saj.dao.AppDatabase
import com.line.saj.databinding.ActivityMainBinding
import com.line.saj.factory.MemoViewModelFactory
import com.line.saj.network.ConnectivityHelper
import com.line.saj.repository.MemoRepository

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (!ConnectivityHelper.isConnectedToNetwork(this)) showNetworkErrorAlert()

        subscribeUi(binding)
    }


    private fun subscribeUi(binding: ActivityMainBinding) {

        val memoRepo = MemoRepository.getInstance(AppDatabase.getInstance(this).memoDao())
        val factory = MemoViewModelFactory(memoRepo)
        val vm: MemoListViewModel =
            ViewModelProviders.of(this, factory).get(MemoListViewModel::class.java)
        val adapter = initAdapter(binding)


        binding.vm = vm
        binding.lifecycleOwner = this
    }



    // ================================================
    //
    //  Adapter
    //
    // ================================================
    private fun initAdapter(binding: ActivityMainBinding){

    }






    // ================================================
    //
    //  Alert
    //
    // ================================================

    private fun showNetworkErrorAlert() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("ERROR").setMessage("인터넷 연결을 확인해주세요.")
        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
            finish()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}
