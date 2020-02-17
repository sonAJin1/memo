package com.line.saj.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.line.saj.components.viewModel.MemoEditViewModel

class MemoEditViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemoEditViewModel() as T
    }
}