package com.weiran.mynowinandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.weiran.mynowinandroid.data.source.LocalStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val localStorage: LocalStorage
) : ViewModel() {

}