package com.line.saj.components.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.line.saj.components.model.Memo
import com.line.saj.repository.MemoRepository
import okhttp3.internal.Util

class MemoListViewModel(memoRepository: MemoRepository) : ViewModel() {
    private val mediator = MediatorLiveData<Unit>()

    var memoRepoData = memoRepository.getAllMemos()
    val memo = MutableLiveData<List<Memo>>()

    var addMemoConverter = MutableLiveData<Unit>()



    init {
        mediator.addSource(memoRepoData) { memo.value = it }
    }


    fun onClickAddMemo(){
        addMemoConverter.value = Unit
    }

}