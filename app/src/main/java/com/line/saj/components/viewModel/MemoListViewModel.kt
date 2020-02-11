package com.line.saj.components.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.line.saj.components.model.Memo
import com.line.saj.repository.MemoRepository

class MemoListViewModel(memoRepository: MemoRepository) : ViewModel() {
    var initMemo = memoRepository.getAllMemos()
    val memo = MutableLiveData<List<Memo>>()

    private val mediator = MediatorLiveData<Unit>()

    init {
        mediator.addSource(initMemo) { memo.value = it }
    }

}