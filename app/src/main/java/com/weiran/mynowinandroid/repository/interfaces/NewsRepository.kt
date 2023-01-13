package com.weiran.mynowinandroid.repository.interfaces

import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.saved.SavedUIState

interface NewsRepository {

    var newsItems: List<NewsItem>

    suspend fun loadNewsItems(): List<NewsItem>

    fun loadMarkedNewsItems(): List<NewsItem>

    fun updateSavedUIState(): SavedUIState

    suspend fun changeNewsItemsById(newsId: String)
    fun getNewsByChoiceTopics(topicItems: List<TopicItem>): List<NewsItem>
}