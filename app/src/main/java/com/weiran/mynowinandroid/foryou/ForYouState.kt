package com.weiran.mynowinandroid.foryou

import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.TopicItem

data class ForYouState(
    val topicItems: List<TopicItem> = listOf(),
    val doneShownState: Boolean = false,
    val topicsSectionUIState: TopicsSectionUiState = TopicsSectionUiState.Shown,
    val newsItems: List<NewsItem> = listOf(),
    val feedUIState: FeedUIState = FeedUIState.Loading,
    val markedNewsItems: List<NewsItem> = listOf(),
)

sealed class TopicsSectionUiState {
    object Shown : TopicsSectionUiState()
    object NotShown : TopicsSectionUiState()
}

sealed class FeedUIState {
    object Loading : FeedUIState()
    object Success : FeedUIState()
}