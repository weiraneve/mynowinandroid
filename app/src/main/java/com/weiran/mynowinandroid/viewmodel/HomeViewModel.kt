package com.weiran.mynowinandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.weiran.mynowinandroid.ui.home.HomeTabs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class HomeAction {
    data class PositionChangedAction(val position: HomeTabs) : HomeAction()
}

class HomeViewModel : ViewModel() {

    private val _position = MutableStateFlow(HomeTabs.FOR_PAGE)
    val position = _position.asStateFlow()

    private fun onPositionChanged(position: HomeTabs) {
        _position.value = position
    }

    fun dispatchAction(action: HomeAction) {
        when (action) {
            is HomeAction.PositionChangedAction -> onPositionChanged(action.position)
        }
    }

}