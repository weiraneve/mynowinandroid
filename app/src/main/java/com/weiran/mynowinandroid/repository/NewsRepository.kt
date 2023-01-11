package com.weiran.mynowinandroid.repository

import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.datasource.DataSource
import javax.inject.Inject

class NewsRepository @Inject constructor(private val dataSource: DataSource) {

    private var newsItems = emptyList<NewsItem>()

    suspend fun loadNewsItems(): List<NewsItem> {
        newsItems = dataSource.getNews().map { getNewsItemByNews(it) }
        return newsItems
    }

    fun getMarkedNewsItems() = newsItems.filter { it.isMarked }

    suspend fun changeNewsItemsById(newsId: String) {
        newsItems = newsItems.map { if (it.id == newsId) it.copy(isMarked = !it.isMarked) else it }
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

    fun getNewsByChoiceTopics(topicItems: List<TopicItem>): List<NewsItem> {
        val selectedTopicIds = topicItems
            .filter { it.selected }.map { it.id }
        val resultNewsItems = newsItems.filter {
            var flag = false
            it.topicItems.forEach { topicItem ->
                if (selectedTopicIds.contains(topicItem.id)) flag = true
            }
            flag
        }
        return resultNewsItems
    }

}