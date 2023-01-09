package com.weiran.mynowinandroid.repository

import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.LocalStorage
import com.weiran.mynowinandroid.di.IoDispatcher
import com.weiran.mynowinandroid.theme.MyIcons
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TopicRepository @Inject constructor(
    private val localStorage: LocalStorage,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getTopicItems() = withContext(ioDispatcher) {
        localStorage.getTopics().map {
            val icon = if (it.selected) MyIcons.Check else MyIcons.Add
            TopicItem(
                name = it.name,
                id = it.id,
                selected = it.selected,
                icon = icon,
                imageUrl = it.imageUrl
            )
        }
    }

    fun checkTopicItemIsSelected(topicItems: List<TopicItem>): Boolean {
        var isSelectedFlag = false
        topicItems.forEach {
            if (it.selected) {
                isSelectedFlag = true
                return@forEach
            }
        }
        return isSelectedFlag
    }

    fun convertTopics(topicItems: List<TopicItem>): List<Topic> =
        getTopicsByTopicItems(topicItems)

    private fun getTopicsByTopicItems(topicItems: List<TopicItem>) = topicItems.map {
        Topic(
            id = it.id,
            name = it.name,
            selected = it.selected,
            imageUrl = it.imageUrl
        )
    }

}