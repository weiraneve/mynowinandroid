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

    var allNews = emptyList<News>()
    private val topicIdNewsItemsMap = mutableMapOf<String, List<NewsItem>>()

    init {
        allNews = localStorage.getNewsFromAssets()
    }

    suspend fun getMarkedNewsIds() = withContext(ioDispatcher) { localStorage.getMarkedNewsIds() }

    fun getMarkedNewsByIds(markedNewsIds: List<String>, topicItems: List<TopicItem>) =
        allNews.filter { markedNewsIds.contains(it.id) }.map { getNewsItemByNews(it, topicItems) }

    private fun getNewsItemByNews(news: News, topicItems: List<TopicItem>) = NewsItem(
        id = news.id,
        title = news.title,
        content = news.content,
        url = news.url,
        headerImageUrl = news.headerImageUrl,
        topics = getTopicItemsById(news.topics, topicItems)
    )

    private fun getTopicItemsById(topicIds: List<String>, topicItems: List<TopicItem>) =
        topicIds.map { topicId ->
            TopicItem(
                id = topicId,
                name = topicItems.find { topicItem -> topicItem.id == topicId }?.name ?: ""
            )
        }

    private fun getNewsItemsByNewsTopicId(id: String, topicItems: List<TopicItem>) =
        allNews.filter { it.topics.contains(id) }.map {
            getNewsItemByNews(it, topicItems)
        }

    fun getNewItemsAndSaveInCacheMap(
        selectedTopicIds: List<String>,
        topicItems: List<TopicItem>,
        markedNewsItems: List<NewsItem>
    ): List<NewsItem> {
        val resultNewsItems = mutableListOf<NewsItem>()
        selectedTopicIds.forEach {
            if (!topicIdNewsItemsMap.containsKey(it)) {
                topicIdNewsItemsMap[it] = getNewsItemsByNewsTopicId(it, topicItems)
            }
            topicIdNewsItemsMap[it]?.let { newsItems -> resultNewsItems.addAll(newsItems) }
        }
        return updateNewsItemsForMarked(resultNewsItems, markedNewsItems)
    }

    private fun updateNewsItemsForMarked(
        newsItems: List<NewsItem>,
        markedNewsItems: List<NewsItem>
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