package com.line.saj.components.view.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.line.saj.R
import com.line.saj.base.BaseActivity
import com.line.saj.components.model.Memo
import com.line.saj.components.view.adapter.MemoAdapter
import com.line.saj.components.viewModel.MemoListViewModel
import com.line.saj.dao.AppDatabase
import com.line.saj.databinding.ActivityMainBinding
import com.line.saj.factory.MemoViewModelFactory
import com.line.saj.network.ConnectivityHelper
import com.line.saj.repository.MemoRepository


/**
 *
 * TODO: !!사용한 부분 .?등을 사용하거나 최대한 사용을 지양할 것
 *
 *
 *
 */
class MainActivity : BaseActivity() {

    private var memoRepo: MemoRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (!ConnectivityHelper.isConnectedToNetwork(this)) showNetworkErrorAlert()

        subscribeUi(binding)
    }


    private fun subscribeUi(binding: ActivityMainBinding) {

        memoRepo = MemoRepository.getInstance(AppDatabase.getInstance(this).memoDao())
        val factory = MemoViewModelFactory(memoRepo!!)
        val vm: MemoListViewModel =
            ViewModelProviders.of(this, factory).get(MemoListViewModel::class.java)

        initAdapter(binding)

        vm.memo.observe(this, Observer {
            addAdapter(binding,it)
        })

        vm.addMemoConverter.observe(this, Observer {
            val intent = Intent(this, AddMemoActivity::class.java)
            startActivity(intent)
        })


        binding.vm = vm
        binding.lifecycleOwner = this
    }



    // ================================================
    //
    //  Adapter
    //
    // ================================================

    private fun initAdapter(binding: ActivityMainBinding){
        val adapter = MemoAdapter()

        adapter.setListener(object : MemoAdapter.OnClickListener {
            override fun onClickItem(id: Int) {
                //TODO: memo 상세 페이지로 이동
            }

            override fun onClickDeleteItem(id: Int) {
                deleteMemo(id)
            }

        })

       // memoRepo!!.addMemos(Memo(0,"메모 제목 예제입니다","메모 내용 예제입니다",""))

        binding.rcMemo.adapter = adapter
    }

    private fun addAdapter(binding: ActivityMainBinding, memo: List<Memo>){
        val adapter = binding.rcMemo.adapter as MemoAdapter
        adapter.removeAll()
        adapter.addAll(memo)
    }




    // ================================================
    //
    //  Repo Data
    //
    // ================================================

    private fun deleteMemo(memoId: Int){
        val r = Runnable {
            memoRepo!!.deleteMemo(memoId)
        }

        val thread = Thread(r)
        thread.start()
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
