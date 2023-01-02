package com.weiran.mynowinandroid.viewmodel

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.LocalStorage
import com.weiran.mynowinandroid.ui.theme.MyIcons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections
import javax.inject.Inject

sealed class TopicAction {
    data class TopicSelected(val topicId: String) : TopicAction()
    object DoneDispatch : TopicAction()
}

sealed class SectionUiState {
    object Shown : SectionUiState()
    object NotShown : SectionUiState()
}

data class TopicState(
    val topicItems: List<TopicItem> = listOf(),
    val doneButtonState: Boolean = false,
    val sectionUiState: SectionUiState = SectionUiState.Shown,
    val newsItems: List<NewsItem> = listOf()
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
            checkTopicSelected()
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

    private fun selectedTopic(topicId: String) {
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
            checkTopicSelected()
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

    private fun dispatchDone() {
        _topicState.update {
            it.copy(sectionUiState = SectionUiState.NotShown)
        }
    }

    private suspend fun checkTopicSelected() {
        var isTopicSelected = false
        _topicState.value.topicItems.forEach {
            if (it.selected) {
                isTopicSelected = true
                return@forEach
            }
        }
        if (!isTopicSelected) {
            _topicState.update {
                it.copy(sectionUiState = SectionUiState.Shown)
            }
        }
        _topicState.update {
            it.copy(
                doneButtonState = isTopicSelected,
                newsItems = loadNewsByChoiceTopics()
            )
        }
    }

    private suspend fun loadNewsByChoiceTopics(): List<NewsItem> {
        return withContext(Dispatchers.Default) {
            val selectedTopicItems = getSelectedTopicItems()
            localStorage.getNewsFromAssets()
                .filter { !Collections.disjoint(it.topics, selectedTopicItems) }
                .map {
                    NewsItem(
                        id = it.id,
                        title = it.title,
                        content = it.content,
                        topics = findTopicById(it.topics)
                    )
                }
        }
    }

    private fun findTopicById(topicIds: List<String>): List<TopicItem> {
        return topicIds.map { topicId ->
            val topicName = _topicState.value.topicItems.find { topicItem ->
                topicItem.id == topicId
            }?.name ?: ""
            TopicItem(
                id = topicId,
                name = topicName
            )
        }
    }

    private fun getSelectedTopicItems(): List<String> {
        val selectedTopicItems = mutableListOf<String>()
        _topicState.value.topicItems.forEach {
            if (it.selected) {
                selectedTopicItems.add(it.id)
            }
        }
        return selectedTopicItems
    }

    fun dispatchAction(action: TopicAction) {
        when (action) {
            is TopicAction.TopicSelected -> selectedTopic(action.topicId)
            is TopicAction.DoneDispatch -> dispatchDone()
        }
    }

}