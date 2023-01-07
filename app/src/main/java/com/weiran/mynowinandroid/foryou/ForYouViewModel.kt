package com.weiran.mynowinandroid.foryou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.data.model.News
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.LocalStorage
import com.weiran.mynowinandroid.di.IoDispatcher
import com.weiran.mynowinandroid.repository.NewsRepository
import com.weiran.mynowinandroid.repository.TopicRepository
import com.weiran.mynowinandroid.saved.SavedUIState
import com.weiran.mynowinandroid.theme.MyIcons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val localStorage: LocalStorage,
    private val topicRepository: TopicRepository,
    private val newsRepository: NewsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _forYouState = MutableStateFlow(ForYouState())
    val forYouState = _forYouState.asStateFlow()
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
            _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.NotShown) }
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
        _forYouState.update {
            it.copy(
                savedUIState = SavedUIState.NonEmpty,
                markedNewsItems = newsRepository.getMarkedNewsByIds(
                    markedNewsIds,
                    _forYouState.value.topicItems
                )
            )
        }

    private suspend fun initTopicItems() =
        _forYouState.update { it.copy(topicItems = topicRepository.getTopicItems()) }

    private fun selectedTopic(topicId: String) {
        viewModelScope.launch(ioDispatcher) {
            updateTopic(topicId)
            localStorage.saveTopics(topicRepository.convertTopics(_forYouState.value.topicItems))
            checkTopicSelected()
        }
    }

    private fun updateTopic(topicId: String) =
        _forYouState.update { it.copy(topicItems = getTopicItemsByTopicId(topicId)) }

    private fun dispatchDone() {
        _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.NotShown) }
        localStorage.writeFlag(DONE_SHOWN_STATE, false)
    }


    private suspend fun checkTopicSelected() {
        val isTopicSelected = topicRepository.checkTopicItemIsSelected(_forYouState.value.topicItems)
        if (!isTopicSelected) {
            _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.Shown) }
            localStorage.writeFlag(DONE_SHOWN_STATE, true)
        }
        updateUIStateAndNewsItems(isTopicSelected, _forYouState.value.topicItems)
    }

    private suspend fun updateUIStateAndNewsItems(
        isTopicSelected: Boolean,
        topicItems: List<TopicItem>
    ) = _forYouState.update {
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
                _forYouState.value.topicItems,
                _forYouState.value.markedNewsItems
            ).map { if (markedNewsIds.contains(it.id)) it.copy(isMarked = true) else it }
        }

    private fun getTopicItemsByTopicId(topicId: String) = _forYouState.value.topicItems.map {
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
        _forYouState.update {
            it.copy(
                newsItems = newsRepository.getMarkedNewsItemsById(
                    newsId,
                    _forYouState.value.newsItems
                )
            )
        }
    }

    private fun updateMarkedNewsItems() {
        _forYouState.update {
            it.copy(markedNewsItems = _forYouState.value.newsItems.filter { newsItem -> newsItem.isMarked })
        }
    }

    private fun updateSavedUIState() {
        if (_forYouState.value.markedNewsItems.isEmpty()) {
            _forYouState.update { it.copy(savedUIState = SavedUIState.Empty) }
        } else {
            _forYouState.update { it.copy(savedUIState = SavedUIState.NonEmpty) }
        }
    }

    fun dispatchAction(action: ForYouAction) {
        when (action) {
            is ForYouAction.TopicSelected -> selectedTopic(action.topicId)
            is ForYouAction.DoneDispatch -> dispatchDone()
            is ForYouAction.MarkNews -> updateMarkNews(action.newsId)
        }
    }

    companion object {
        private const val DONE_SHOWN_STATE = "doneShownState"
    }

}