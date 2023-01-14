package com.weiran.mynowinandroid.data.source.local

import com.weiran.mynowinandroid.data.model.NewsLocal

interface LocalStorage {

    fun getNewsFromAssets(): List<NewsLocal>

}