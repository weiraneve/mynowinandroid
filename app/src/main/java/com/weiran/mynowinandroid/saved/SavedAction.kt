package com.weiran.mynowinandroid.saved

sealed class SavedAction {
    data class MarkNews(val newsId: String) : SavedAction()
}