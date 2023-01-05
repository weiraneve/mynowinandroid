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
            loadMarkedNews()
        }
    }

    private suspend fun loadMarkedNews() {
        withContext(ioDispatcher) {
            val markedNewsIds = localStorage.getMarkedNewsIds()
            if (markedNewsIds.isNotEmpty()) {
                _feedState.update { it.copy(savedUIState = SavedUIState.NonEmpty) }
            }
            _feedState.update { it.copy(markedNewsItems = getMarkedNewsByIds(markedNewsIds)) }
        }
    }

    private fun getMarkedNewsByIds(markedNewsIds: List<String>) =
        _feedState.value.newsItems.filter {
            markedNewsIds.contains(it.id)
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
        val markedNewsIds = localStorage.getMarkedNewsIds()
        getNewItemsAndSaveInCacheMap(selectedTopicIds).map {
            if (markedNewsIds.contains(it.id)) {
                it.copy(isMarked = true)
            } else {
                it
            }
        }
    }

    private fun getNewItemsAndSaveInCacheMap(selectedTopicIds: List<String>): List<NewsItem> {
        val resultNewsItems = mutableListOf<NewsItem>()
        selectedTopicIds.forEach {
            if (!topicIdNewsItemsMap.containsKey(it)) {
                topicIdNewsItemsMap[it] = getNewsItemsByNewsTopicId(it)
            }
            topicIdNewsItemsMap[it]?.let { newsItems -> resultNewsItems.addAll(newsItems) }
        }
        return updateNewsItemsForMarked(resultNewsItems)
    }

    private fun updateNewsItemsForMarked(newsItems: List<NewsItem>): List<NewsItem> {
        val markedNewsItemIds = _feedState.value.markedNewsItems.map { it.id }
        return newsItems.map {
            if (markedNewsItemIds.contains(it.id)) it.copy(isMarked = true) else it
        }
    }

    private fun getTopicItems() = localStorage.getTopics().map {
        val icon = if (it.selected) MyIcons.Check else MyIcons.Add
        TopicItem(
            name = it.name, id = it.id, selected = it.selected, icon = icon, imageUrl = it.imageUrl
        )
    }

    private fun getTopicItemsByTopicId(topicId: String) = _feedState.value.topicItems.map {
        if (it.id == topicId) {
            val icon = if (it.selected) MyIcons.Add else MyIcons.Check
            it.copy(selected = !it.selected, icon = icon)
        } else {
            it
        }
    }

    private fun getNewsItemsByNewsTopicId(id: String) =
        allNews.filter { it.topics.contains(id) }.map {
            NewsItem(
                id = it.id,
                title = it.title,
                content = it.content,
                headerImageUrl = it.headerImageUrl,
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

    private fun changeMarkNews(newsId: String) {
        updateNewsItemsMarkedById(newsId)
        updateMarkedNewsItems()
        updateSavedUIState()
    }

    private fun updateNewsItemsMarkedById(newsId: String) {
        _feedState.update { it.copy(newsItems = getMarkedNewsItemsById(newsId)) }
    }

    private fun getMarkedNewsItemsById(newsId: String) = _feedState.value.newsItems.map {
        if (it.id == newsId) {
            changeDBMarkedNews(it)
            it.copy(isMarked = !it.isMarked)
        } else {
            it
        }
    }

    private fun changeDBMarkedNews(newsItem: NewsItem) {
        viewModelScope.launch(ioDispatcher) {
            if (newsItem.isMarked) {
                localStorage.removeMarkedNewsId(newsItem.id)
            } else {
                localStorage.saveMarkedNewsId(newsItem.id)
            }
        }
    }

    private fun updateMarkedNewsItems() {
        _feedState.update {
            it.copy(markedNewsItems = _feedState.value.newsItems.filter { newsItem -> newsItem.isMarked })
        }
    }

    private fun updateSavedUIState() {
        if (_feedState.value.markedNewsItems.isEmpty()) {
            _feedState.update { it.copy(savedUIState = SavedUIState.Empty) }
        } else {
            _feedState.update { it.copy(savedUIState = SavedUIState.NonEmpty) }
        }
    }

    fun dispatchAction(action: FeedAction) {
        when (action) {
            is FeedAction.TopicSelected -> selectedTopic(action.topicId)
            is FeedAction.DoneDispatch -> dispatchDone()
            is FeedAction.MarkNews -> changeMarkNews(action.newsId)
        }
    }

}