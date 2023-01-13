package com.weiran.mynowinandroid.repository

import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.datasource.DataSource
import com.weiran.mynowinandroid.data.source.sp.SpStorage
import com.weiran.mynowinandroid.repository.interfaces.TopicRepository
import com.weiran.mynowinandroid.theme.MyIcons
import javax.inject.Inject

class TopicRepositoryImpl @Inject constructor(
    private val spStorage: SpStorage,
    private val dataSource: DataSource
) : TopicRepository {

    override var topicItems = emptyList<TopicItem>()

    override suspend fun loadTopicItems(): List<TopicItem> {
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

    override fun checkTopicItemIsSelected(): Boolean {
        var isSelectedFlag = false
        topicItems.forEach {
            if (it.selected) {
                isSelectedFlag = true
                return@forEach
            }
        }
        return isSelectedFlag
    }

    override fun checkDoneShown(): Boolean = spStorage.readFlag(DONE_SHOWN_STATE)

    override fun updateDoneShown(flag: Boolean) = spStorage.writeFlag(DONE_SHOWN_STATE, flag)

    override suspend fun updateTopicSelected(topicId: String) {
        topicItems = topicItems.map { getSelectedTopicItem(it, topicId) }
        if (checkTopicItemIsSelected().not()) updateDoneShown(true)
        dataSource.updateTopicSelected(topicId)
    }

    private fun getSelectedTopicItem(topicItem: TopicItem, topicId: String) =
        if (topicItem.id == topicId) {
            topicItem.copy(
                selected = !topicItem.selected,
                icon = if (topicItem.selected.not()) MyIcons.Check else MyIcons.Add
            )
        } else topicItem

    companion object {
        private const val DONE_SHOWN_STATE = "doneShownState"
    }

}