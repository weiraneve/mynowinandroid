package com.weiran.mynowinandroid.ui.pages.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _homePageState = MutableStateFlow(HomePageState())
    val homePageState = _homePageState.asStateFlow()

    private fun changeToDefaultTheme() =
        _homePageState.update { it.copy(settingThemeState = SettingThemeState.DefaultThemeState) }

    private fun changeToAndroidTheme() =
        _homePageState.update { it.copy(settingThemeState = SettingThemeState.AndroidThemeState) }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.SettingDefaultThemeChange -> changeToDefaultTheme()
            is HomeAction.SettingAndroidThemeChange -> changeToAndroidTheme()
        }
    }
}