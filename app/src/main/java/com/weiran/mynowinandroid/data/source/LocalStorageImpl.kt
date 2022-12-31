package com.weiran.mynowinandroid.data.source

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.source.room.AppDatabase
import com.weiran.mynowinandroid.data.source.room.model.TopicEntity
import com.weiran.mynowinandroid.utils.FileUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalStorageImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    @ApplicationContext private val context: Context
) : LocalStorage {

    private val gson = Gson()

    override fun getNewsFromAssets(): List<News> {
        return gson.fromJson(
            FileUtil.getStringFromRaw(
                context, R.raw.news
            ), object : TypeToken<List<News>>() {}.type
        )
    }

    override suspend fun getTopics(): List<Topic> {
        return withContext(Dispatchers.Default) {
            if (appDatabase.topicDao().getAll().isEmpty()) {
                saveTopics(fakeTopics)
            }
            appDatabase.topicDao().getAll().map {
                Topic(
                    name = it.name,
                    id = it.id.toString(),
                    selected = it.selected,
                )
            }
        }
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