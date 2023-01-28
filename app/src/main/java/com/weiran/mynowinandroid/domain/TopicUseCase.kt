package com.weiran.mynowinandroid.domain

import com.weiran.mynowinandroid.store.data.model.TopicItem
import com.weiran.mynowinandroid.store.repository.TopicRepository
import com.weiran.mynowinandroid.ui.theme.MyIcons

class TopicUseCase constructor(private val topicRepository: TopicRepository) {

    var topicItems = emptyList<TopicItem>()

    suspend fun loadTopicItems(): List<TopicItem> {
        topicItems = topicRepository.getTopics().map {
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

    fun checkTopicItemIsSelected(): Boolean {
        var isSelectedFlag = false
        topicItems.forEach {
            if (it.selected) {
                isSelectedFlag = true
                return@forEach
            }
        }
        return isSelectedFlag
    }

    fun readTopicsSectionShown(): Boolean =
        topicRepository.readTopicsSectionShown(TOPICS_SECTION_SHOWN)

    fun updateTopicsSectionShown(flag: Boolean) =
        topicRepository.updateTopicsSectionShown(TOPICS_SECTION_SHOWN, flag)


    suspend fun updateTopicSelected(topicId: String) {
        topicItems = topicItems.map { getSelectedTopicItem(it, topicId) }
        if (checkTopicItemIsSelected().not()) topicRepository.updateTopicsSectionShown(
            TOPICS_SECTION_SHOWN, true
        )
        topicRepository.updateTopicSelected(topicId)
    }

    private fun getSelectedTopicItem(topicItem: TopicItem, topicId: String) =
        if (topicItem.id == topicId) {
            topicItem.copy(
                selected = !topicItem.selected,
                icon = if (topicItem.selected.not()) MyIcons.Check else MyIcons.Add
            )
        } else topicItem

    companion object {
        private const val TOPICS_SECTION_SHOWN = "topics_section_state"
    }

}