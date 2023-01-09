package com.weiran.mynowinandroid.data.source.datasource

import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.NewsLocal
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.source.fakeTopics
import com.weiran.mynowinandroid.data.source.local.LocalStorage
import com.weiran.mynowinandroid.data.source.room.AppDatabase
import com.weiran.mynowinandroid.data.source.room.model.NewsEntity
import com.weiran.mynowinandroid.data.source.room.model.NewsTopicEntity
import com.weiran.mynowinandroid.data.source.room.model.TopicEntity
import javax.inject.Inject

class DataSourceImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val localStorage: LocalStorage
) : DataSource {

    override fun getTopics(): List<Topic> {
        if (appDatabase.topicDao().getAll().isEmpty()) {
            saveTopics(fakeTopics)
        }
        return appDatabase.topicDao().getAll().map {
            Topic(
                name = it.name,
                id = it.id.toString(),
                selected = it.selected,
                imageUrl = it.imageUrl
            )
        }
    }

    override fun saveTopics(topics: List<Topic>) {
        val topicEntities = topics.map {
            TopicEntity(
                id = it.id.toLong(),
                name = it.name,
                selected = it.selected,
                imageUrl = it.imageUrl,
            )
        }
        appDatabase.topicDao().inserts(topicEntities)
    }

    override fun getNews(): List<News> {
        val newsLocals = localStorage.getNewsFromAssets()
        initNewsTopics(newsLocals)
        initNewsLocals(newsLocals)
        return convertNewsEntitiesToNews(appDatabase.newsDao().getAll())
    }

    private fun initNewsLocals(newsLocals: List<NewsLocal>) {
        if (appDatabase.newsDao().getAll().isEmpty()) saveNews(convertNewsLocalToNews(newsLocals))
    }

    private fun initNewsTopics(newsLocals: List<NewsLocal>) {
        if (appDatabase.newsTopicDao().getAll().isEmpty()) {
            newsLocals.forEach { newsLocal ->
                newsLocal.topics.forEach {
                    val newsTopicEntity = NewsTopicEntity(newsId = newsLocal.id, topicId = it)
                    appDatabase.newsTopicDao().insert(newsTopicEntity)
                }
            }
        }
    }

    private fun convertNewsEntitiesToNews(newsEntities: List<NewsEntity>) =
        newsEntities.map {
            News(
                id = it.id.toString(),
                title = it.title,
                content = it.content,
                isMarked = it.isMarked,
                url = it.url,
                headerImageUrl = it.headerImageUrl,
                topics = getNewsTopicsByNewsId(it.id.toString())
            )
        }

    private fun convertNewsLocalToNews(newsLocals: List<NewsLocal>): List<News> =
        newsLocals.map {
            News(
                id = it.id,
                title = it.title,
                content = it.content,
                isMarked = false,
                url = it.url,
                headerImageUrl = it.headerImageUrl,
                topics = getNewsTopicsByNewsId(it.id)
            )
        }

    override fun saveNews(newsList: List<News>) {
        newsList.forEach { news -> appDatabase.newsDao().insert(generateNewsEntity(news)) }
    }

    override fun updateIsMarkedById(newsId: String) {
        val newsEntity = appDatabase.newsDao().getOne(newsId.toLong())
        newsEntity.isMarked = !newsEntity.isMarked
        appDatabase.newsDao().insert(newsEntity)
    }

    private fun generateNewsEntity(news: News) =
        NewsEntity(
            id = news.id.toLong(),
            title = news.title,
            content = news.content,
            isMarked = news.isMarked,
            url = news.url,
            headerImageUrl = news.headerImageUrl
        )

    private fun getNewsTopicsByNewsId(newsId: String): List<Topic> {
        return appDatabase.newsTopicDao().getByNewsId(newsId).map {
            val topicEntity = appDatabase.topicDao().getOne(it.topicId)
            Topic(
                id = topicEntity.id.toString(),
                name = topicEntity.name,
            )
        }
    }

}