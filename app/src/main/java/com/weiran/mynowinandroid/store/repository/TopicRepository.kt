package com.weiran.mynowinandroid.store.repository

import com.weiran.mynowinandroid.store.data.model.Topic
import com.weiran.mynowinandroid.store.data.source.datasource.DataSource
import com.weiran.mynowinandroid.store.data.source.sp.SpStorage

interface TopicRepository {

    suspend fun getTopics(): List<Topic>

    fun readTopicsSectionShown(key: String): Boolean

    fun updateTopicsSectionShown(key: String, flag: Boolean)

    suspend fun updateTopicSelected(topicId: String)
}

class TopicRepositoryImpl constructor(
    private val spStorage: SpStorage,
    private val dataSource: DataSource
) : TopicRepository {

    override suspend fun getTopics(): List<Topic> = dataSource.getTopics()

    override fun readTopicsSectionShown(key: String): Boolean = spStorage.readFlag(key)

    override fun updateTopicsSectionShown(key: String, flag: Boolean) =
        spStorage.writeFlag(key, flag)

    override suspend fun updateTopicSelected(topicId: String) =
        dataSource.updateTopicSelected(topicId)
}