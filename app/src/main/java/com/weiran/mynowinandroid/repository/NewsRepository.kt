package com.weiran.mynowinandroid.repository

import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.datasource.DataSource
import com.weiran.mynowinandroid.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val dataSource: DataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    var newsItems = emptyList<NewsItem>()

    suspend fun loadNewsItems() = withContext(ioDispatcher) {
        newsItems = dataSource.getNews().map { getNewsItemByNews(it) }
        newsItems
    }

    fun getMarkedNewsItems() = newsItems.filter { it.isMarked }

    fun changeNewsItemsById(newsId: String) {
        dataSource.updateIsMarkedById(newsId)
    }

    private fun getNewsItemByNews(news: News) = NewsItem(
        id = news.id,
        title = news.title,
        content = news.content,
        isMarked = news.isMarked,
        url = news.url,
        headerImageUrl = news.headerImageUrl,
        topicItems = getTopicItemsByTopics(news.topics)
    )

    private fun getTopicItemsByTopics(topics: List<Topic>): List<TopicItem> =
        topics.map { TopicItem(id = it.id, name = it.name) }

}