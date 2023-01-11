package com.weiran.mynowinandroid.repository

import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.datasource.DataSource
import com.weiran.mynowinandroid.data.source.sp.SpStorage
import com.weiran.mynowinandroid.theme.MyIcons
import javax.inject.Inject

class TopicRepository @Inject constructor(
    private val spStorage: SpStorage,
    private val dataSource: DataSource,
) {

    var topicItems = emptyList<TopicItem>()

    suspend fun getTopicItems(): List<TopicItem> {
        topicItems = dataSource.getTopics().map {
            val icon = if (it.selected) MyIcons.Check else MyIcons.Add
            TopicItem(
                name = it.name,
                id = it.id,
                selected = it.selected,
                icon = icon,
                imageUrl = it.imageUrl
            )
        }
        return topicItems
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

    fun checkDoneShown(): Boolean = spStorage.readFlag(DONE_SHOWN_STATE)

    fun updateDoneShown(flag: Boolean) = spStorage.writeFlag(DONE_SHOWN_STATE, flag)

    suspend fun updateTopicSelected(topicId: String) {
        topicItems = topicItems.map {
            if (it.id == topicId) {
                it.copy(
                    selected = !it.selected,
                    icon = if (it.selected.not()) MyIcons.Check else MyIcons.Add
                )
            } else it
        }
        dataSource.updateTopicSelected(topicId)
    }

    companion object {
        private const val DONE_SHOWN_STATE = "doneShownState"
    }

}