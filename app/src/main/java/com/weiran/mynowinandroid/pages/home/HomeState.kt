package com.weiran.mynowinandroid.pages.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class HomeState(
    val tabStates: List<TabState> = listOf(),
    val settingThemeState: SettingThemeState = SettingThemeState.DefaultThemeState
)

sealed class SettingThemeState {
    object DefaultThemeState : SettingThemeState()
    object AndroidThemeState : SettingThemeState()
}

data class TabState(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val isSelected: Boolean
)