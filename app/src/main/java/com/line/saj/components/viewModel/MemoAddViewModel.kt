package com.line.saj.components.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MemoAddViewModel() : ViewModel() {
    var closeConverter = MutableLiveData<Unit>()
    var addPhotoConverter = MutableLiveData<Unit>()
    var addMemoConverter = MutableLiveData<Unit>()


    fun onClickClose(){
        closeConverter.value = Unit
    }

    fun onClickAddPhoto(){
        addPhotoConverter.value = Unit
    }

    fun onClickAddMemo(){
        addMemoConverter.value = Unit
    }

}