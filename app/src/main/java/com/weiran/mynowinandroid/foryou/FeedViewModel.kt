package com.weiran.mynowinandroid.foryou

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.LocalStorage
import com.weiran.mynowinandroid.di.IoDispatcher
import com.weiran.mynowinandroid.theme.MyIcons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class FeedUIState {
    object Loading : FeedUIState()
    object Success : FeedUIState()
}

sealed class FeedAction {
    data class TopicSelected(val topicId: String) : FeedAction()
    object DoneDispatch : FeedAction()
}

sealed class SectionUiState {
    object Shown : SectionUiState()
    object NotShown : SectionUiState()
}

data class FeedState(
    val topicItems: List<TopicItem> = listOf(),
    val doneButtonState: Boolean = false,
    val sectionUiState: SectionUiState = SectionUiState.Shown,
    val newsItems: List<NewsItem> = listOf(),
    val feedUIState: FeedUIState = FeedUIState.Loading
)

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val localStorage: LocalStorage,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _feedState = MutableStateFlow(FeedState())
    val feedState = _feedState.asStateFlow()
    private var allNews = emptyList<News>()
    private val newsItemMap = mutableMapOf<String, List<NewsItem>>()

    init {
        viewModelScope.launch(ioDispatcher) {
            allNews = localStorage.getNewsFromAssets()
            initTopicItems()
            checkTopicSelected()
        }
    }

    private suspend fun initTopicItems() {
        withContext(ioDispatcher) {
            _feedState.update {
                it.copy(
                    topicItems = localStorage.getTopics().map { topic ->
                        val icon: ImageVector = if (topic.selected) MyIcons.Check else MyIcons.Add
                        TopicItem(
                            name = topic.name,
                            id = topic.id,
                            selected = topic.selected,
                            icon = icon
                        )
                    }
                )
            }
        }
    }

    private fun selectedTopic(topicId: String) {
        viewModelScope.launch(ioDispatcher) {
            updateTopic(topicId)
            localStorage.saveTopics(convertTopics(_feedState.value.topicItems))
            checkTopicSelected()
        }
    }

    private fun updateTopic(topicId: String) {
        _feedState.update {
            it.copy(
                topicItems = _feedState.value.topicItems.map { topicItem ->
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
        _feedState.update {
            it.copy(sectionUiState = SectionUiState.NotShown)
        }
    }

    private suspend fun checkTopicSelected() {
        var isTopicSelected = false
        _feedState.value.topicItems.forEach {
            if (it.selected) {
                isTopicSelected = true
                return@forEach
            }
        }
        if (!isTopicSelected) {
            _feedState.update {
                it.copy(sectionUiState = SectionUiState.Shown)
            }
        }
        _feedState.update {
            it.copy(
                doneButtonState = isTopicSelected,
                newsItems = loadNewsByChoiceTopics(),
                feedUIState = FeedUIState.Success
            )
        }
    }

    private suspend fun loadNewsByChoiceTopics(): List<NewsItem> {
        val selectedTopicIds = getSelectedTopicIds()
        withContext(ioDispatcher) {
            selectedTopicIds.forEach { id ->
                if (!newsItemMap.containsKey(id)) {
                    newsItemMap[id] = allNews
                        .filter { it.topics.contains(id) }
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
        }
        val resultNewsItems = mutableListOf<NewsItem>()
        selectedTopicIds.forEach {
            newsItemMap[it]?.forEach { newsItem ->
                resultNewsItems.add(newsItem)
            }
        }
        return resultNewsItems
    }

    private fun findTopicById(topicIds: List<String>): List<TopicItem> {
        return topicIds.map { topicId ->
            _feedState.value.topicItems.find { topicItem ->
                topicItem.id == topicId
            }.let {
                TopicItem(
                    id = topicId,
                    name = it?.name ?: ""
                )
            }
        }
    }

    private fun getSelectedTopicIds(): List<String> {
        val selectedTopicItems = mutableListOf<String>()
        _feedState.value.topicItems.forEach {
            if (it.selected) {
                selectedTopicItems.add(it.id)
            }
        }
        return selectedTopicItems
    }

    fun dispatchAction(action: FeedAction) {
        when (action) {
            is FeedAction.TopicSelected -> selectedTopic(action.topicId)
            is FeedAction.DoneDispatch -> dispatchDone()
        }
    }

}