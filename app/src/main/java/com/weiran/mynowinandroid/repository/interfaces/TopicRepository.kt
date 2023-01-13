package com.weiran.mynowinandroid.repository.interfaces

import com.weiran.mynowinandroid.data.model.TopicItem

interface TopicRepository {

    var topicItems: List<TopicItem>

    suspend fun loadTopicItems(): List<TopicItem>

    fun checkTopicItemIsSelected(): Boolean

    fun checkDoneShown(): Boolean

    fun updateDoneShown(flag: Boolean)

    suspend fun updateTopicSelected(topicId: String)
}