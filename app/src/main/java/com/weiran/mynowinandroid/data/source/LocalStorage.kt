package com.weiran.mynowinandroid.data.source

import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.Topic

interface LocalStorage {

    fun getNewsFromAssets(): List<News>

    fun getTopics(): List<Topic>

    fun saveTopics(topics: List<Topic>)

    fun getMarkedNewsIds(): List<String>

    fun saveMarkedNewsId(markedNewsId: String)

    fun removeMarkedNewsId(markedNewsId: String)

    fun writeFlagBySharedPreference(key: String, value: Boolean)

    fun readFlagBySharedPreference(key: String): Boolean

}