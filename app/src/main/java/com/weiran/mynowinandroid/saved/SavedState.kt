package com.weiran.mynowinandroid.saved

import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.foryou.FeedUIState

data class SavedState(
    val savedUIState: SavedUIState = SavedUIState.Empty,
    val markedNewsItems: List<NewsItem> = listOf(),
    val feedUIState: FeedUIState = FeedUIState.Loading
)

sealed class SavedUIState {
    object Empty : SavedUIState()
    object NonEmpty : SavedUIState()
}