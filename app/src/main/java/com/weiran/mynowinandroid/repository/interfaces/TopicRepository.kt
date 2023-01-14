package com.weiran.mynowinandroid.repository.interfaces

import com.weiran.mynowinandroid.data.model.Topic

interface TopicRepository {

    suspend fun getTopics(): List<Topic>

    fun readTopicsSectionShown(key: String): Boolean

    fun updateTopicsSectionShown(key: String, flag: Boolean)

    suspend fun updateTopicSelected(topicId: String)
}