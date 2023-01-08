package com.weiran.mynowinandroid.data.source

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.NewsLocal
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.source.room.AppDatabase
import com.weiran.mynowinandroid.data.source.room.model.NewsEntity
import com.weiran.mynowinandroid.data.source.room.model.NewsTopicEntity
import com.weiran.mynowinandroid.data.source.room.model.TopicEntity
import com.weiran.mynowinandroid.utils.FileUtil
import com.weiran.mynowinandroid.utils.SharedPreferenceUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalStorageImpl @Inject constructor(
    private val appDatabase: AppDatabase, @ApplicationContext private val context: Context
) : LocalStorage {

    private val gson = Gson()

    private fun getNewsFromAssets(): List<NewsLocal> {
        return gson.fromJson(
            FileUtil.getNewsFromAssets(
                context, ASSETS_NAME
            ), object : TypeToken<List<NewsLocal>>() {}.type
        )
    }

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
        topics.forEach { topic ->
            val topicEntity = TopicEntity().apply {
                id = topic.id.toLong()
                name = topic.name
                selected = topic.selected
                imageUrl = topic.imageUrl
            }
            appDatabase.topicDao().insert(topicEntity)
        }
    }

    override fun getNews(): List<News> {
        val newsLocals = getNewsFromAssets()
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
                    val newsTopicEntity = NewsTopicEntity().apply {
                        newsId = newsLocal.id
                        topicId = it
                    }
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
        NewsEntity().apply {
            id = news.id.toLong()
            title = news.title
            content = news.content
            isMarked = news.isMarked
            url = news.url
            headerImageUrl = news.headerImageUrl
        }

    private fun getNewsTopicsByNewsId(newsId: String): List<Topic> {
        return appDatabase.newsTopicDao().getByNewsId(newsId).map {
            val topicEntity = appDatabase.topicDao().getOne(it.topicId)
            Topic(
                id = topicEntity.id.toString(),
                name = topicEntity.name,
            )
        }
    }

    override fun writeFlag(key: String, value: Boolean) {
        SharedPreferenceUtil.writeBoolean(
            context, SharedPreferenceUtil.SHARED_PREFERENCE_FILE, key, value
        )
    }

    override fun readFlag(key: String) = SharedPreferenceUtil.readBoolean(
        context, SharedPreferenceUtil.SHARED_PREFERENCE_FILE, key, true
    )

    companion object {
        private const val ASSETS_NAME = "news.json"
    }

}