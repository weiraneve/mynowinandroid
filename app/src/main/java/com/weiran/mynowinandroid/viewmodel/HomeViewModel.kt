package com.weiran.mynowinandroid.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class HomeAction {
    data class CurrentTabChangedAction(val currentTab: HomeTabs) : HomeAction()
}

data class HomeState(
    val currentTab: HomeTabs = HomeTabs.FOR_PAGE,
)

class HomeViewModel : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private fun currentTabChanged(currentTab: HomeTabs) {
        viewModelScope.launch {
            _homeState.update {
                it.copy(
                    currentTab = currentTab
                )
            }
        }
    }

    fun dispatchAction(action: HomeAction) {
        when (action) {
            is HomeAction.CurrentTabChangedAction -> currentTabChanged(action.currentTab)
        }
    }

}

enum class HomeTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectIcon: Int
) {
    FOR_PAGE(R.string.for_page, R.drawable.ic_upcoming_border, R.drawable.ic_upcoming),
    SAVED_PAGE(R.string.saved, R.drawable.ic_bookmarks_border, R.drawable.ic_bookmarks),
    INTEREST_PAGE(R.string.interests, R.drawable.ic_menu_book_border, R.drawable.ic_menu_book),
}