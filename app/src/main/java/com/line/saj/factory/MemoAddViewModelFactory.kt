package com.line.saj.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.line.saj.components.viewModel.MemoAddViewModel

class MemoAddViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemoAddViewModel() as T
    }
}