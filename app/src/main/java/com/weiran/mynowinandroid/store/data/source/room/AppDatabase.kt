package com.weiran.mynowinandroid.store.data.source.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weiran.mynowinandroid.store.data.source.room.dao.NewsDAO
import com.weiran.mynowinandroid.store.data.source.room.dao.NewsTopicDAO
import com.weiran.mynowinandroid.store.data.source.room.dao.TopicDAO
import com.weiran.mynowinandroid.store.data.source.room.model.NewsEntity
import com.weiran.mynowinandroid.store.data.source.room.model.NewsTopicEntity
import com.weiran.mynowinandroid.store.data.source.room.model.TopicEntity

@Database(
    entities = [TopicEntity::class, NewsEntity::class, NewsTopicEntity::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "mynowinandroid-db"
    }

    abstract fun topicDao(): TopicDAO

    abstract fun newsDao(): NewsDAO

    abstract fun newsTopicDao(): NewsTopicDAO

}