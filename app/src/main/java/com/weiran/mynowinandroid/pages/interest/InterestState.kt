package com.weiran.mynowinandroid.pages.interest

import com.weiran.mynowinandroid.store.data.model.TopicItem
import com.weiran.mynowinandroid.pages.foryou.FeedUIState

data class InterestState(
    val topicItems: List<TopicItem> = listOf(),
    val feedUIState: FeedUIState = FeedUIState.Loading
)