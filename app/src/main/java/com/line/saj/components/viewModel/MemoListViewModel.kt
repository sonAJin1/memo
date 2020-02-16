package com.line.saj.components.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.line.saj.repository.MemoRepository

class MemoListViewModel(memoRepository: MemoRepository) : ViewModel() {
    var memo = memoRepository.getAllMemos()
    var addMemoConverter = MutableLiveData<Unit>()

    fun onClickAddMemo() {
        addMemoConverter.value = Unit
    }

}