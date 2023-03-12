package com.weiran.mynowinandroid.ui.pages.home

sealed class HomeAction {
    object SettingDefaultThemeChange : HomeAction()
    object SettingAndroidThemeChange : HomeAction()
}
