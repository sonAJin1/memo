package com.line.saj.components.viewModel

import android.graphics.ColorSpace
import androidx.lifecycle.ViewModel
import com.line.saj.components.model.Memo

class MemoDetailViewModel(memo: Memo): ViewModel(){
    var memo = memo
}