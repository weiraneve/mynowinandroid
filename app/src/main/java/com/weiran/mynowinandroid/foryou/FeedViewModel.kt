package com.weiran.mynowinandroid.foryou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.LocalStorage
import com.weiran.mynowinandroid.di.IoDispatcher
import com.weiran.mynowinandroid.repository.NewsRepository
import com.weiran.mynowinandroid.repository.TopicRepository
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
    private val topicRepository: TopicRepository,
    private val newsRepository: NewsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _feedState = MutableStateFlow(FeedState())
    val feedState = _feedState.asStateFlow()
    private var allNews = emptyList<News>()

    init {
        viewModelScope.launch(ioDispatcher) {
            allNews = newsRepository.allNews
            checkTopicsSection()
            initTopicItems()
            checkTopicSelected()
            loadMarkedNews()
        }
    }

    private fun checkTopicsSection() {
        if (!localStorage.readFlag(DONE_SHOWN_STATE)) {
            _feedState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.NotShown) }
        }
    }

    private suspend fun loadMarkedNews() {
        withContext(ioDispatcher) {
            val markedNewsIds = newsRepository.getMarkedNewsIds()
            if (markedNewsIds.isNotEmpty()) {
                initMarkedNewsAndSavedUI(markedNewsIds)
            }
        }
    }

    private fun initMarkedNewsAndSavedUI(markedNewsIds: List<String>) =
        _feedState.update {
            it.copy(
                savedUIState = SavedUIState.NonEmpty,
                markedNewsItems = newsRepository.getMarkedNewsByIds(
                    markedNewsIds,
                    _feedState.value.topicItems
                )
            )
        }

    private suspend fun initTopicItems() =
        _feedState.update { it.copy(topicItems = topicRepository.getTopicItems()) }

    private fun selectedTopic(topicId: String) {
        viewModelScope.launch(ioDispatcher) {
            updateTopic(topicId)
            localStorage.saveTopics(topicRepository.convertTopics(_feedState.value.topicItems))
            checkTopicSelected()
        }
    }

    private fun updateTopic(topicId: String) =
        _feedState.update { it.copy(topicItems = getTopicItemsByTopicId(topicId)) }

    private fun dispatchDone() {
        _feedState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.NotShown) }
        localStorage.writeFlag(DONE_SHOWN_STATE, false)
    }


    private suspend fun checkTopicSelected() {
        val isTopicSelected = topicRepository.checkTopicItemIsSelected(_feedState.value.topicItems)
        if (!isTopicSelected) {
            _feedState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.Shown) }
            localStorage.writeFlag(DONE_SHOWN_STATE, true)
        }
        updateUIStateAndNewsItems(isTopicSelected, _feedState.value.topicItems)
    }

    private suspend fun updateUIStateAndNewsItems(
        isTopicSelected: Boolean,
        topicItems: List<TopicItem>
    ) = _feedState.update {
        it.copy(
            doneShownState = isTopicSelected,
            newsItems = loadNewsByChoiceTopics(topicItems),
            feedUIState = FeedUIState.Success
        )
    }

    private suspend fun loadNewsByChoiceTopics(topicItems: List<TopicItem>) =
        withContext(ioDispatcher) {
            val markedNewsIds = newsRepository.getMarkedNewsIds()
            newsRepository.getNewItemsAndSaveInCacheMap(
                topicRepository.getSelectedTopicIds(topicItems),
                _feedState.value.topicItems,
                _feedState.value.markedNewsItems
            ).map { if (markedNewsIds.contains(it.id)) it.copy(isMarked = true) else it }
        }

    private fun getTopicItemsByTopicId(topicId: String) = _feedState.value.topicItems.map {
        if (it.id == topicId) {
            val icon = if (it.selected) MyIcons.Add else MyIcons.Check
            it.copy(selected = !it.selected, icon = icon)
        } else {
            it
        }
    }

    private fun updateMarkNews(newsId: String) {
        viewModelScope.launch {
            updateNewsItemsMarkedById(newsId)
            updateMarkedNewsItems()
            updateSavedUIState()
        }
    }

    private suspend fun updateNewsItemsMarkedById(newsId: String) {
        _feedState.update {
            it.copy(
                newsItems = newsRepository.getMarkedNewsItemsById(
                    newsId,
                    _feedState.value.newsItems
                )
            )
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
            is FeedAction.MarkNews -> updateMarkNews(action.newsId)
        }
    }

    companion object {
        private const val DONE_SHOWN_STATE = "doneShownState"
    }

}