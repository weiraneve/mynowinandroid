package com.weiran.mynowinandroid.repository

import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.source.datasource.DataSource
import com.weiran.mynowinandroid.data.source.sp.SpStorage
import com.weiran.mynowinandroid.repository.interfaces.TopicRepository
import javax.inject.Inject

class TopicRepositoryImpl @Inject constructor(
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