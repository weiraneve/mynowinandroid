package com.weiran.mynowinandroid.data.source

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.source.room.AppDatabase
import com.weiran.mynowinandroid.data.source.room.model.MarkedNewsEntity
import com.weiran.mynowinandroid.data.source.room.model.TopicEntity
import com.weiran.mynowinandroid.utils.FileUtil
import com.weiran.mynowinandroid.utils.SharedPreferenceUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalStorageImpl @Inject constructor(
    private val appDatabase: AppDatabase, @ApplicationContext private val context: Context
) : LocalStorage {

    private val gson = Gson()

    override fun getNewsFromAssets(): List<News> {
        return gson.fromJson(
            FileUtil.getNewsFromAssets(
                context, ASSETS_NAME
            ), object : TypeToken<List<News>>() {}.type
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

    override fun getMarkedNewsIds(): List<String> =
        appDatabase.markedNewsDao().getAll().map { it.id.toString() }

    override fun saveMarkedNewsId(markedNewsId: String) {
        appDatabase.markedNewsDao().insert(MarkedNewsEntity().apply { id = markedNewsId.toLong() })
    }

    override fun removeMarkedNewsId(markedNewsId: String) {
        appDatabase.markedNewsDao().remove(MarkedNewsEntity().apply { id = markedNewsId.toLong() })
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