package com.line.saj.components.viewModel

import android.graphics.ColorSpace
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.line.saj.components.model.Memo
import com.line.saj.repository.MemoRepository

class MemoDetailViewModel(memoRepo: MemoRepository, id: Int): ViewModel(){
    var memo = memoRepo.getMemo(id)
    var modifyBtnClickConverter = MutableLiveData<Unit>()
    var backBtnClickConverter = MutableLiveData<Unit>()


    fun onClickModify(){
        modifyBtnClickConverter.value = Unit
    }

    fun onClickBack(){
        backBtnClickConverter.value = Unit
    }
}