package com.weiran.mynowinandroid.pages.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private fun changeToDefaultTheme() =
        _homeState.update { it.copy(settingThemeState = SettingThemeState.DefaultThemeState) }

    private fun changeToAndroidTheme() =
        _homeState.update { it.copy(settingThemeState = SettingThemeState.AndroidThemeState) }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SettingDefaultThemeChange -> changeToDefaultTheme()
            is HomeAction.SettingAndroidThemeChange -> changeToAndroidTheme()
        }
    }
}