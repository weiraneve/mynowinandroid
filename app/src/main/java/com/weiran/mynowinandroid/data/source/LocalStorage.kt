package com.weiran.mynowinandroid.data.source

import com.weiran.mynowinandroid.data.model.Topic

interface LocalStorage {

    suspend fun getTopics(): List<Topic>

    suspend fun saveTopics(topics: List<Topic>)

}