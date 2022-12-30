package com.weiran.mynowinandroid.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.weiran.mynowinandroid.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class HomeAction {
    data class ChangeCurrentTabAction(val currentTabState: TabState) : HomeAction()
}

data class HomeState(
    val currentTab: HomeTab = HomeTab.FOR_PAGE,
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
        _homeState.update {
            it.copy(
                tabStates = listOf(
                    TabState(HomeTab.FOR_PAGE.title, HomeTab.FOR_PAGE.selectIcon, true),
                    TabState(HomeTab.SAVED_PAGE.title, HomeTab.SAVED_PAGE.icon, false),
                    TabState(HomeTab.INTEREST_PAGE.title, HomeTab.INTEREST_PAGE.icon, false),
                )
            )
        }
    }

    private fun findHomeTab(title: Int) =
        HomeTab.values().find { it.title == title } ?: HomeTab.FOR_PAGE

    private fun changeCurrentTab(currentTabState: TabState) {
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
            is HomeAction.ChangeCurrentTabAction -> changeCurrentTab(action.currentTabState)
        }
    }

}

enum class HomeTab(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectIcon: Int
) {
    FOR_PAGE(R.string.for_page, R.drawable.ic_upcoming_border, R.drawable.ic_upcoming),
    SAVED_PAGE(R.string.saved, R.drawable.ic_bookmarks_border, R.drawable.ic_bookmarks),
    INTEREST_PAGE(R.string.interests, R.drawable.ic_menu_book_border, R.drawable.ic_menu_book),
}