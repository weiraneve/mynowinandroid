package com.weiran.mynowinandroid.pages.home

sealed class HomeAction {
    object SettingDefaultThemeChange : HomeAction()
    object SettingAndroidThemeChange : HomeAction()
}
