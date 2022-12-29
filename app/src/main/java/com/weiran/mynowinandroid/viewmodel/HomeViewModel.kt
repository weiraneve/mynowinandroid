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
    data class CurrentTabChangedAction(val currentTabState: TabState) : HomeAction()
}

data class HomeState(
    val currentTab: HomeTabs = HomeTabs.FOR_PAGE,
    val tabStates: List<TabState> = listOf()
)

data class TabState(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val isSelected: Boolean
)

class HomeViewModel : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        viewModelScope.launch {
            _homeState.update {
                it.copy(
                    tabStates = listOf(
                        TabState(HomeTabs.FOR_PAGE.title, HomeTabs.FOR_PAGE.selectIcon, true),
                        TabState(HomeTabs.SAVED_PAGE.title, HomeTabs.SAVED_PAGE.icon, false),
                        TabState(HomeTabs.INTEREST_PAGE.title, HomeTabs.INTEREST_PAGE.icon, false),
                    )
                )
            }
        }
    }

    private fun findHomeTab(title: Int) =
        HomeTabs.values().find { it.title == title } ?: HomeTabs.FOR_PAGE

    private fun currentTabChanged(currentTabState: TabState) {
        val tabStates = _homeState.value.tabStates.map {
            val homeTab = findHomeTab(it.title)
            if (currentTabState == it) {
                it.copy(
                    icon = homeTab.selectIcon,
                    isSelected = true
                )
            } else {
                it.copy(
                    icon = homeTab.icon,
                    isSelected = false
                )
            }
        }
        _homeState.update {
            it.copy(
                tabStates = tabStates,
                currentTab = findHomeTab(currentTabState.title)
            )
        }
    }

    fun dispatchAction(action: HomeAction) {
        when (action) {
            is HomeAction.CurrentTabChangedAction -> currentTabChanged(action.currentTabState)
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