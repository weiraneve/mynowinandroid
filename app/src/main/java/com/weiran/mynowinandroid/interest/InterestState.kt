package com.weiran.mynowinandroid.interest

import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.foryou.FeedUIState

data class InterestState(
    val topicItems: List<TopicItem> = listOf(),
    val feedUIState: FeedUIState = FeedUIState.Loading
)