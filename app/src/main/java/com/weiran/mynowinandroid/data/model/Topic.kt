package com.weiran.mynowinandroid.data.model

data class Topic(
    val id: String,
    val name: String = "",
    val imageUrl: String = "",
    val selected: Boolean = false
)