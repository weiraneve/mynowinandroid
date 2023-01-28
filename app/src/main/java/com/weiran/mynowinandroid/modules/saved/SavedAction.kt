package com.weiran.mynowinandroid.modules.saved

sealed class SavedAction {
    data class MarkNews(val newsId: String) : SavedAction()
}