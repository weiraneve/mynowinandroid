package com.weiran.mynowinandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.fakeTopics
import com.weiran.mynowinandroid.ui.component.MyIcons
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ForYouAction {
    data class TopicClickAction(val topicId: String) : ForYouAction()
}

data class ForYouState(
    val topicItems: List<TopicItem> = listOf()
)

class ForYouViewModel : ViewModel() {

    private val _forYouState = MutableStateFlow(ForYouState())
    val forYouState = _forYouState.asStateFlow()

    init {
        viewModelScope.launch {
            _forYouState.update {
                it.copy(
                    topicItems = convertInitializedData()
                )
            }
        }
    }

    private fun convertInitializedData(): List<TopicItem> {
        val topicItems = mutableListOf<TopicItem>()
        fakeTopics.forEach {
            topicItems.add(
                TopicItem(
                    name = it.name,
                    id = it.id
                )
            )
        }
        return topicItems
    }

    private fun clickTopicSelected(topicId: String) {
        viewModelScope.launch {
            _forYouState.update {
                it.copy(
                    topicItems = _forYouState.value.topicItems.map { topicItem ->
                        if (topicItem.id == topicId) {
                            val icon = if (topicItem.selected) MyIcons.Add else MyIcons.Check
                            topicItem.copy(
                                selected = !topicItem.selected,
                                icon = icon
                            )
                        } else {
                            topicItem
                        }
                    }
                )
            }
        }
    }

    fun dispatchAction(action: ForYouAction) {
        when (action) {
            is ForYouAction.TopicClickAction -> clickTopicSelected(action.topicId)
        }
    }

}