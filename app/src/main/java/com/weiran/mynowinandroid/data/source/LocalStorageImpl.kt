package com.weiran.mynowinandroid.data.source

import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.source.room.AppDatabase
import com.weiran.mynowinandroid.data.source.room.model.TopicEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalStorageImpl @Inject constructor(private val appDatabase: AppDatabase) : LocalStorage {

    override suspend fun getTopics(): List<Topic> {
        val topics: MutableList<Topic> = mutableListOf()
        withContext(Dispatchers.Default) {
            if (appDatabase.topicDao().getAll().isEmpty()) {
                saveTopics(fakeTopics)
            } else {
                appDatabase.topicDao().getAll().forEach {
                    topics.add(
                        Topic(
                            name = it.name,
                            id = it.id.toString(),
                            selected = it.selected,
                        )
                    )
                }
            }
        }
        return topics
    }

    override suspend fun saveTopics(topics: List<Topic>) {
        withContext(Dispatchers.Default) {
            topics.forEach { topic ->
                val topicEntity = TopicEntity().apply {
                    id = topic.id.toLong()
                    name = topic.name
                    selected = topic.selected
                }
                appDatabase.topicDao().insert(topicEntity)
            }
        }
    }

}