package com.line.saj.components.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.line.saj.components.model.Memo
import com.line.saj.repository.MemoRepository

class MemoAddViewModel() : ViewModel() {
    var closeConverter = MutableLiveData<Unit>()


    fun onClickClose(){
        closeConverter.value = Unit
    }
}