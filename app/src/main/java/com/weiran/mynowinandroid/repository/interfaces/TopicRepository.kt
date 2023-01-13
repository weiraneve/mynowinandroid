package com.weiran.mynowinandroid.repository.interfaces

import com.weiran.mynowinandroid.data.model.TopicItem

interface TopicRepository {

    var topicItems: List<TopicItem>

    suspend fun loadTopicItems(): List<TopicItem>

    fun checkTopicItemIsSelected(): Boolean

    fun checkTopicsSectionShown(): Boolean

    fun updateTopicsSectionShown(flag: Boolean)

    suspend fun updateTopicSelected(topicId: String)
}