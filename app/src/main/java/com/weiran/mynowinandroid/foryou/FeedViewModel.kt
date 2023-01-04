package com.weiran.mynowinandroid.foryou

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

sealed class FeedAction {
    data class TopicSelected(val topicId: String) : FeedAction()
    object DoneDispatch : FeedAction()
    data class MarkNews(val newsId: String) : FeedAction()
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val localStorage: LocalStorage,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _feedState = MutableStateFlow(FeedState())
    val feedState = _feedState.asStateFlow()
    private var allNews = emptyList<News>()
    private val topicIdNewsItemsMap = mutableMapOf<String, List<NewsItem>>()

    init {
        viewModelScope.launch(ioDispatcher) {
            allNews = localStorage.getNewsFromAssets()
            initTopicItems()
            checkTopicSelected()
        }
    }

    private fun initTopicItems() = _feedState.update { it.copy(topicItems = getTopicItems()) }

    private fun selectedTopic(topicId: String) {
        viewModelScope.launch(ioDispatcher) {
            updateTopic(topicId)
            localStorage.saveTopics(convertTopics(_feedState.value.topicItems))
            checkTopicSelected()
        }
    }

    private fun updateTopic(topicId: String) =
        _feedState.update { it.copy(topicItems = getTopicItemsByTopicId(topicId)) }

    private fun convertTopics(topicItems: List<TopicItem>): List<Topic> =
        getTopicsByTopicItems(topicItems)

    private fun dispatchDone() =
        _feedState.update { it.copy(sectionUiState = SectionUiState.NotShown) }

    private suspend fun checkTopicSelected() {
        val isTopicSelected = checkTopicItemIsSelected()
        if (!isTopicSelected) {
            _feedState.update { it.copy(sectionUiState = SectionUiState.Shown) }
        }
        updateUIStateAndNewsItems(isTopicSelected)
    }

    private suspend fun updateUIStateAndNewsItems(isTopicSelected: Boolean) = _feedState.update {
        it.copy(
            doneButtonState = isTopicSelected,
            newsItems = loadNewsByChoiceTopics(),
            feedUIState = FeedUIState.Success
        )
    }

    private fun checkTopicItemIsSelected(): Boolean {
        var isSelectedFlag = false
        _feedState.value.topicItems.forEach {
            if (it.selected) {
                isSelectedFlag = true
                return@forEach
            }
        }
        return isSelectedFlag
    }

    private suspend fun loadNewsByChoiceTopics() = withContext(ioDispatcher) {
        val selectedTopicIds = getSelectedTopicIds()
        getNewItemsAndSaveInCacheMap(selectedTopicIds)
    }

    private fun getNewItemsAndSaveInCacheMap(selectedTopicIds: List<String>): List<NewsItem> {
        val resultNewsItems = mutableListOf<NewsItem>()
        selectedTopicIds.forEach {
            if (!topicIdNewsItemsMap.containsKey(it)) {
                topicIdNewsItemsMap[it] = getNewsItemsByNewsTopicId(it)
            }
            topicIdNewsItemsMap[it]?.let { newsItems -> resultNewsItems.addAll(newsItems) }
        }
        return resultNewsItems
    }

    private fun getTopicItems() = localStorage.getTopics().map {
        val icon = if (it.selected) MyIcons.Check else MyIcons.Add
        TopicItem(
            name = it.name, id = it.id, selected = it.selected, icon = icon
        )
    }

    private fun getTopicItemsByTopicId(topicId: String) = _feedState.value.topicItems.map {
        if (it.id == topicId) {
            val icon = if (it.selected) MyIcons.Add else MyIcons.Check
            it.copy(selected = !it.selected, icon = icon)
        } else it
    }

    private fun getNewsItemsByNewsTopicId(id: String) =
        allNews.filter { it.topics.contains(id) }.map {
            NewsItem(
                id = it.id,
                title = it.title,
                content = it.content,
                topics = getTopicItemsById(it.topics)
            )
        }

    private fun getTopicsByTopicItems(topicItems: List<TopicItem>) = topicItems.map {
        Topic(
            id = it.id,
            name = it.name,
            selected = it.selected,
        )
    }

    private fun getTopicItemsById(topicIds: List<String>) = topicIds.map { topicId ->
        _feedState.value.topicItems.find { topicItem -> topicItem.id == topicId }.let {
            TopicItem(id = topicId, name = it?.name ?: "")
        }
    }

    private fun getSelectedTopicIds(): List<String> =
        _feedState.value.topicItems.filter { it.selected }.map { it.id }

    // todo refactor
    private fun markAndCancelNews(newsId: String) {
        _feedState.update {
            it.copy(
                newsItems = _feedState.value.newsItems.map { newsItem ->
                    if (newsItem.id == newsId) {
                        NewsItem(
                            id = newsItem.id,
                            isMarked = !newsItem.isMarked,
                            title = newsItem.title,
                            content = newsItem.content,
                            topics = newsItem.topics
                        )
                    } else {
                        newsItem
                    }
                }
            )
        }
        _feedState.update {
            it.copy(
                markedNewsItems = _feedState.value.newsItems.filter { newsItem ->
                    newsItem.isMarked
                }
            )
        }
        if (_feedState.value.markedNewsItems.isEmpty()) {
            _feedState.update {
                it.copy(
                    savedUIState = SavedUIState.Empty
                )
            }
        } else {
            _feedState.update {
                it.copy(
                    savedUIState = SavedUIState.NonEmpty
                )
            }
        }
    }

    fun dispatchAction(action: FeedAction) {
        when (action) {
            is FeedAction.TopicSelected -> selectedTopic(action.topicId)
            is FeedAction.DoneDispatch -> dispatchDone()
            is FeedAction.MarkNews -> markAndCancelNews(action.newsId)
        }
    }

}