package com.weiran.mynowinandroid.data.model

data class NewsItem(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val isMarked: Boolean = false,
    val topics: List<TopicItem> = listOf()
)