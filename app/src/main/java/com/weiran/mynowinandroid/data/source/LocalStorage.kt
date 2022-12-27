package com.weiran.mynowinandroid.data.source

import com.weiran.mynowinandroid.data.model.Topic

interface LocalStorage {

    fun getTopics(): List<Topic>

}