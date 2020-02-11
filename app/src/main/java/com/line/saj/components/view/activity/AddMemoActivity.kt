package com.line.saj.components.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.line.saj.R
import com.line.saj.base.BaseActivity
import com.line.saj.components.viewModel.MemoAddViewModel
import com.line.saj.components.viewModel.MemoListViewModel
import com.line.saj.dao.AppDatabase
import com.line.saj.databinding.ActivityAddMemoBinding
import com.line.saj.databinding.ActivityMainBinding
import com.line.saj.factory.MemoAddViewModelFactory
import com.line.saj.factory.MemoViewModelFactory
import com.line.saj.repository.MemoRepository

class AddMemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityAddMemoBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_memo)

        subscribeUi(binding)
    }

    private fun subscribeUi(binding: ActivityAddMemoBinding) {

        val factory = MemoAddViewModelFactory()
        val vm: MemoAddViewModel =
            ViewModelProviders.of(this, factory).get(MemoAddViewModel::class.java)


        vm.closeConverter.observe(this, Observer {
            finish()
        })

        binding.vm = vm
        binding.lifecycleOwner = this
    }
}
