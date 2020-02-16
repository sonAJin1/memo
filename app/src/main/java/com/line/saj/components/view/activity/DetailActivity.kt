package com.line.saj.components.view.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.line.saj.R
import com.line.saj.base.BaseActivity
import com.line.saj.components.model.Memo
import com.line.saj.components.view.adapter.DetailImageAdapter
import com.line.saj.components.viewModel.MemoDetailViewModel
import com.line.saj.databinding.ActivityDetailBinding
import com.line.saj.factory.MemoDetailViewModelFactory

class DetailActivity : BaseActivity() {

    //TODO: 몇장 중에 몇장째 스크롤 중인지 이미지 띄울 것

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val intent = intent
        val memoData = intent.getParcelableExtra<Memo>(MainActivity().INTENT_MEMO)

        if(memoData==null) {
            showToast(getString(R.string.missing_memo_detail))
            return
        }

        subscribeUi(memoData)
    }

    private fun subscribeUi(memoData: Memo) {
        val factory = MemoDetailViewModelFactory(memoData)
        val vm: MemoDetailViewModel =
            ViewModelProviders.of(this, factory).get(MemoDetailViewModel::class.java)

        initAdapter(imageList = memoData.image)

        binding.vm = vm
        binding.lifecycleOwner = this
    }

    private fun initAdapter(imageList: List<String>){
        val adapter = DetailImageAdapter()
        adapter.addAll(imageList)
        binding.rcPhoto.adapter = adapter
    }

}
