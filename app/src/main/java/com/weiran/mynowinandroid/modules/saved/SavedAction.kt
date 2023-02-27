package com.weiran.mynowinandroid.modules.saved

sealed class SavedAction {
    class MarkNews(val newsId: String) : SavedAction()
}