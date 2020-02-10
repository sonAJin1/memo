package com.line.saj.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.line.saj.components.viewModel.MemoListViewModel
import com.line.saj.repository.MemoRepository

class MemoViewModelFactory(val memoRepository: MemoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemoListViewModel(memoRepository) as T
    }
}