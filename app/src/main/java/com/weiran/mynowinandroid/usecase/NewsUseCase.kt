package com.weiran.mynowinandroid.usecase

import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.repository.interfaces.NewsRepository
import com.weiran.mynowinandroid.saved.SavedUIState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsUseCase @Inject constructor(private val newsRepository: NewsRepository) {

    var newsItems = emptyList<NewsItem>()

    suspend fun loadNewsItems(): List<NewsItem> {
        newsItems = newsRepository.getNews().map { getNewsItemByNews(it) }
        return newsItems
    }

    fun loadMarkedNewsItems() = newsItems.filter { it.isMarked }

    fun updateSavedUIState(): SavedUIState {
        val markedNewsItems = newsItems.filter { it.isMarked }
        return if (markedNewsItems.isEmpty()) SavedUIState.Empty else SavedUIState.NonEmpty
    }

    suspend fun changeNewsItemsById(newsId: String) {
        newsItems = newsItems.map { if (it.id == newsId) it.copy(isMarked = !it.isMarked) else it }
        newsRepository.updateIsMarkedById(newsId)
    }

    fun getNewsItemsByChoiceTopics(topicItems: List<TopicItem>): List<NewsItem> {
        val selectedTopicIds = topicItems.filter { it.selected }.map { it.id }
        val resultNewsItems = newsItems.filter { newsItems ->
            var flag = false
            newsItems.topicItems.forEach { if (selectedTopicIds.contains(it.id)) flag = true }
            flag
        }
        return resultNewsItems
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