package com.weiran.mynowinandroid.repository

import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.LocalStorage
import com.weiran.mynowinandroid.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val localStorage: LocalStorage,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    var newsItems = emptyList<NewsItem>()
    private val topicIdNewsItemsMap = mutableMapOf<String, List<NewsItem>>()

    suspend fun loadNewsItems(): List<NewsItem> = withContext(ioDispatcher) {
        newsItems = localStorage.getNewsFromAssets().map {
            getNewsItemByNews(it)
        }
        newsItems
    }

    suspend fun getMarkedNewsIds() = withContext(ioDispatcher) { localStorage.getMarkedNewsIds() }

    fun getMarkedNewsByIds(markedNewsIds: List<String>) =
        newsItems.filter { markedNewsIds.contains(it.id) }

    private fun getNewsItemByNews(news: News) = NewsItem(
        id = news.id,
        title = news.title,
        content = news.content,
        url = news.url,
        headerImageUrl = news.headerImageUrl,
        topics = getTopicItemsById(news.topics)
    )

    private fun getTopicItemsById(topicIds: List<String>) =
        topicIds.map { topicId ->
            TopicItem(
                id = topicId,
                name = localStorage.getTopics().find { topic -> topic.id == topicId }?.name ?: ""
            )
        }

    private fun getNewsItemsByNewsTopicId(topicId: String) =
        newsItems.filter {
            val topicItem = it.topics.find { topicItem ->
                topicItem.id == topicId
            }
            topicItem != null
        }

    fun getNewItemsAndSaveInCacheMap(
        selectedTopicIds: List<String>, markedNewsItems: List<NewsItem>
    ): List<NewsItem> {
        val resultNewsItems = mutableListOf<NewsItem>()
        selectedTopicIds.forEach {
            if (!topicIdNewsItemsMap.containsKey(it)) {
                topicIdNewsItemsMap[it] = getNewsItemsByNewsTopicId(it)
            }
            topicIdNewsItemsMap[it]?.let { newsItems -> resultNewsItems.addAll(newsItems) }
        }
        return updateNewsItemsForMarked(resultNewsItems, markedNewsItems)
    }

    private fun updateNewsItemsForMarked(
        newsItems: List<NewsItem>, markedNewsItems: List<NewsItem>
    ): List<NewsItem> {
        val markedNewsItemIds = markedNewsItems.map { it.id }
        return newsItems.map {
            if (markedNewsItemIds.contains(it.id)) it.copy(isMarked = true) else it
        }
    }

    private suspend fun removeMarkedNewsId(markedNewsId: String) = withContext(ioDispatcher) {
        localStorage.removeMarkedNewsId(markedNewsId)
    }

    private suspend fun saveMarkedNewsId(markedNewsId: String) = withContext(ioDispatcher) {
        localStorage.saveMarkedNewsId(markedNewsId)
    }

    private suspend fun changeDBMarkedNews(newsItem: NewsItem) {
        withContext(ioDispatcher) {
            if (newsItem.isMarked) {
                removeMarkedNewsId(newsItem.id)
            } else {
                saveMarkedNewsId(newsItem.id)
            }
        }
    }

    suspend fun getMarkedNewsItemsById(newsId: String, newsItems: List<NewsItem>) = newsItems.map {
        if (it.id == newsId) {
            withContext(ioDispatcher) { changeDBMarkedNews(it) }
            it.copy(isMarked = !it.isMarked)
        } else {
            it
        }
    }

}