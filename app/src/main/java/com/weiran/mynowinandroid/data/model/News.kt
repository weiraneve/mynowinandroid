package com.weiran.mynowinandroid.data.model

data class News(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val isMarked: Boolean = false,
    val url: String = "",
    val headerImageUrl: String = "",
    val topics: List<Topic> = listOf()
)