package com.weiran.mynowinandroid.modules.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class HomeAction {
    object SettingDefaultThemeChange : HomeAction()
    object SettingAndroidThemeChange : HomeAction()
}

sealed class SettingTheme {
    object DefaultTheme : SettingTheme()
    object AndroidTheme : SettingTheme()
}

data class HomeState(
    val tabStates: List<TabState> = listOf(),
    val settingTheme: SettingTheme = SettingTheme.DefaultTheme
)

data class TabState(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val isSelected: Boolean
)

class HomeViewModel : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private fun changeToDefaultTheme() =
        _homeState.update { it.copy(settingTheme = SettingTheme.DefaultTheme) }

    private fun changeToAndroidTheme() =
        _homeState.update { it.copy(settingTheme = SettingTheme.AndroidTheme) }

    fun dispatchAction(action: HomeAction) {
        when (action) {
            is HomeAction.SettingDefaultThemeChange -> changeToDefaultTheme()
            is HomeAction.SettingAndroidThemeChange -> changeToAndroidTheme()
        }
    }
}