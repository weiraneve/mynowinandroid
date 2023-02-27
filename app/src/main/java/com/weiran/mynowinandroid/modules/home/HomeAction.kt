package com.weiran.mynowinandroid.modules.home

sealed class HomeAction {
    object SettingDefaultThemeChange : HomeAction()
    object SettingAndroidThemeChange : HomeAction()
}
