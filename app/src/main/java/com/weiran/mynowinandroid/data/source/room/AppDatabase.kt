package com.weiran.mynowinandroid.data.source.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weiran.mynowinandroid.data.source.room.dao.TopicDAO
import com.weiran.mynowinandroid.data.source.room.model.TopicEntity

@Database(
    entities = [TopicEntity::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "mynowinandroid-db"
    }

    abstract fun topicDao(): TopicDAO

}