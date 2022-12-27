package com.weiran.mynowinandroid.data.source

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.utils.FileUtil

class LocalStorageImpl(private val context: Context) : LocalStorage {

    private val gson = Gson()

    override fun getTopics(): List<Topic> {

        return gson.fromJson(
            FileUtil.getStringFromRaw(context, R.raw.topics),
            object : TypeToken<List<Topic>>() {}.type
        )
    }

}