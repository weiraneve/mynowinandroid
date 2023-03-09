package com.weiran.mynowinandroid.pages.saved

sealed class SavedAction {
    class MarkNews(val newsId: String) : SavedAction()
}