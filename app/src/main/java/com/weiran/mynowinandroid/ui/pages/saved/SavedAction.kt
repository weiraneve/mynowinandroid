package com.weiran.mynowinandroid.ui.pages.saved

sealed class SavedAction {
    class MarkNews(val newsId: String) : SavedAction()
}