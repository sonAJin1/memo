package com.line.saj.components.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.line.saj.R
import com.line.saj.base.BaseActivity
import com.line.saj.components.model.Memo
import com.line.saj.components.view.adapter.DetailImageAdapter
import com.line.saj.components.viewModel.MemoDetailViewModel
import com.line.saj.dao.AppDatabase
import com.line.saj.databinding.ActivityDetailBinding
import com.line.saj.factory.MemoDetailViewModelFactory
import com.line.saj.repository.MemoRepository

class DetailActivity : BaseActivity() {

    //TODO: 몇장 중에 몇장째 스크롤 중인지 이미지 띄울 것

    private lateinit var binding: ActivityDetailBinding
    val modifyRequestCode = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val intent = intent
        val memoId = intent.getIntExtra(MainActivity().INTENT_MEMO,0)

        subscribeUi(memoId)
    }

    private fun subscribeUi(memoId: Int) {

        val memoRepo = MemoRepository.getInstance(AppDatabase.getInstance(this).memoDao())
        val factory = MemoDetailViewModelFactory(memoRepo, memoId)
        val vm: MemoDetailViewModel =
            ViewModelProviders.of(this, factory).get(MemoDetailViewModel::class.java)


        vm.memo.observe(this, Observer {
            initAdapter(imageList = vm.memo.value?.image)
        })

        vm.modifyBtnClickConverter.observe(this, Observer {
            val intent = Intent(this, EditMemoActivity::class.java)
            intent.putExtra("MODIFY_MEMO", vm.memo.value)
            startActivityForResult(intent,modifyRequestCode)
        })

        vm.backBtnClickConverter.observe(this, Observer {
            finish()
        })

        binding.vm = vm
        binding.lifecycleOwner = this
    }

    private fun initAdapter(imageList: List<String>?){
        if(imageList==null) return

        val adapter = DetailImageAdapter()
        adapter.addAll(imageList)
        binding.rcPhoto.adapter = adapter
    }

}
