package com.weiran.mynowinandroid.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.LocalStorage
import com.weiran.mynowinandroid.ui.component.MyIcons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TopicAction {
    data class TopicClickAction(val topicId: String) : TopicAction()
}

data class TopicState(
    val topicItems: List<TopicItem> = listOf()
)

@HiltViewModel
class TopicViewModel @Inject constructor(
    private val localStorage: LocalStorage
) : ViewModel() {

    private val _topicState = MutableStateFlow(TopicState())
    val topicState = _topicState.asStateFlow()

    init {
        viewModelScope.launch {
            _topicState.update {
                it.copy(
                    topicItems = readData()
                )
            }
        }
    }

    private suspend fun readData(): List<TopicItem> {
        return localStorage.getTopics().map {
            val icon: ImageVector = if (it.selected) MyIcons.Check else MyIcons.Add
            TopicItem(
                name = it.name,
                id = it.id,
                selected = it.selected,
                icon = icon
            )
        }
    }

    private fun clickTopicSelected(topicId: String) {
        viewModelScope.launch {
            _topicState.update {
                it.copy(
                    topicItems = _topicState.value.topicItems.map { topicItem ->
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
            localStorage.saveTopics(convertTopics(_topicState.value.topicItems))
        }
    }

    private fun convertTopics(topicItems: List<TopicItem>): List<Topic> {
        return topicItems.map {
            Topic(
                id = it.id,
                name = it.name,
                selected = it.selected,
            )
        }
    }

    fun dispatchAction(action: TopicAction) {
        when (action) {
            is TopicAction.TopicClickAction -> clickTopicSelected(action.topicId)
        }
    }

}