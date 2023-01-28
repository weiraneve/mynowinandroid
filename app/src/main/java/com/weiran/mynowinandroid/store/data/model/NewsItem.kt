package com.weiran.mynowinandroid.store.data.model

data class NewsItem(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val isMarked: Boolean = false,
    val url: String = "",
    val headerImageUrl: String = "",
    val topicItems: List<TopicItem> = listOf()
)