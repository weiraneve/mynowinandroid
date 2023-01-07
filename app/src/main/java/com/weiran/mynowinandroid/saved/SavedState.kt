package com.weiran.mynowinandroid.saved

import com.weiran.mynowinandroid.data.model.NewsItem

data class SavedState(
    val savedUIState: SavedUIState = SavedUIState.Empty,
    val newsItems: List<NewsItem> = listOf(),
    val markedNewsItems: List<NewsItem> = listOf()
)

sealed class SavedUIState {
    object Empty : SavedUIState()
    object NonEmpty : SavedUIState()
}