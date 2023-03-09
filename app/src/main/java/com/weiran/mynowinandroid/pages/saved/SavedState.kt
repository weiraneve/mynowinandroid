package com.weiran.mynowinandroid.pages.saved

import com.weiran.mynowinandroid.store.data.model.NewsItem

data class SavedState(
    val savedUIState: SavedUIState = SavedUIState.Empty,
    val markedNewsItems: List<NewsItem> = listOf()
)

sealed class SavedUIState {
    object Empty : SavedUIState()
    object NonEmpty : SavedUIState()
}