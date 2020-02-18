package com.line.saj.components.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
 * TODO: 1. !!사용한 부분 .?등을 사용하거나 최대한 사용을 지양할 것
 *       2. 상단에 스크롤에 따라서 보이고 안보이는 검색바 만들 것
 *       4. url 입력해서 이미지 가져오는 dialog 만들 것
 *
 *
 * TODO: error memo model list read 하는 부분에서 오류 잡을 것
 *
 */

class MainActivity : BaseActivity() {

    private var memoRepo: MemoRepository? = null
    private lateinit var binding: ActivityMainBinding

    val INTENT_MEMO = "INTENT_MEMO"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (!ConnectivityHelper.isConnectedToNetwork(this)) showNetworkErrorAlert()

        subscribeUi()

    }


    private fun subscribeUi() {

        memoRepo = MemoRepository.getInstance(AppDatabase.getInstance(this).memoDao())
        val factory = MemoViewModelFactory(memoRepo!!)
        val vm: MemoListViewModel =
            ViewModelProviders.of(this, factory).get(MemoListViewModel::class.java)

        initAdapter()

        vm.memo.observe(this, Observer {
            addMemo(it)
        })

        vm.addMemoConverter.observe(this, Observer {
            val intent = Intent(this, EditMemoActivity::class.java)
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

    private fun initAdapter() {
        val adapter = MemoAdapter()

        adapter.setListener(object : MemoAdapter.OnClickListener {
            override fun onClickItem(id: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(INTENT_MEMO, id)
                startActivity(intent)
            }

            override fun onClickDeleteItem(id: Int) {
                showDeleteMemoAlert(id)
            }

        })

        binding.rcMemo.adapter = adapter
    }

    private fun addMemo(memo: List<Memo>) {
        val adapter = binding.rcMemo.adapter as MemoAdapter
        adapter.removeAll()
        adapter.addAll(memo)
    }


    // ================================================
    //
    //  Repo Data
    //
    // ================================================

    private fun deleteMemo(memoId: Int) {
        val r = Runnable {
            memoRepo!!.deleteMemo(memoId)
        }

        val thread = Thread(r)
        thread.start()
    }


    // ================================================
    //
    //  Set Spinner
    //
    // ================================================

    private fun setSpinner(yearItem: ArrayList<String>){
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            yearItem
        )

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.header.spYear.adapter = adapter

        binding.header.spYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectYear = parent!!.getItemAtPosition(position)
            }

        }

    }



    // ================================================
    //
    //  Alert
    //
    // ================================================

    private fun showNetworkErrorAlert() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("ERROR").setMessage("인터넷 연결을 확인해주세요.")
        builder.setPositiveButton("OK") { _, _ -> finish() }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }


    private fun showDeleteMemoAlert(memoId: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Alert").setMessage("메모를 삭제하시겠습니까?")
        builder.setPositiveButton("삭제") { _, _ -> deleteMemo(memoId) }
        builder.setNegativeButton("취소") { _, _ -> }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}
