package com.weiran.mynowinandroid.store.data.model

data class NewsLocal(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val url: String = "",
    val headerImageUrl: String = "",
    val topics: List<String> = listOf()
)