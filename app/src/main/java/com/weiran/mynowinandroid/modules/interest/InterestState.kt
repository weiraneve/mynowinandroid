package com.weiran.mynowinandroid.modules.interest

import com.weiran.mynowinandroid.store.data.model.TopicItem
import com.weiran.mynowinandroid.modules.foryou.FeedUIState

data class InterestState(
    val topicItems: List<TopicItem> = listOf(),
    val feedUIState: FeedUIState = FeedUIState.Loading
)