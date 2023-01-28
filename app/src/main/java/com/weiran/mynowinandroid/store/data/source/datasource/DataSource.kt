package com.weiran.mynowinandroid.store.data.source.datasource

import com.weiran.mynowinandroid.store.data.model.News
import com.weiran.mynowinandroid.store.data.model.Topic

interface DataSource {

    suspend fun getTopics(): List<Topic>

    suspend fun saveTopics(topics: List<Topic>)

    suspend fun updateTopicSelected(topicId: String)

    suspend fun getNews(): List<News>

    suspend fun saveNews(newsList: List<News>)

    suspend  fun updateIsMarkedById(newsId: String): Long

}