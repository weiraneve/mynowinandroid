package com.weiran.mynowinandroid.foryou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.usecase.NewsUseCase
import com.weiran.mynowinandroid.usecase.TopicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val topicUseCase: TopicUseCase,
    private val newsUseCase: NewsUseCase 
) : ViewModel() {

    private val _forYouState = MutableStateFlow(ForYouState())
    val forYouState = _forYouState.asStateFlow()

    fun observeData() {
        initFeedData()
        updateTopicsSection()
    }

    private fun updateTopicsSection() {
        if (topicUseCase.readTopicsSectionShown().not()) {
            _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.NotShown) }
        }
    }

    private fun selectedTopic(topicId: String) {
        _forYouState.update { it.copy(feedUIState = FeedUIState.Loading) }
        viewModelScope.launch { topicUseCase.updateTopicSelected(topicId) }
        updateTopicSelected()
        _forYouState.update { it.copy(topicItems = topicUseCase.topicItems) }
    }

    private fun dispatchDone() {
        _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.NotShown) }
        topicUseCase.updateTopicsSectionShown(false)
    }

    private fun updateTopicSelected() {
        val isTopicSelected = topicUseCase.checkTopicItemIsSelected()
        if (isTopicSelected.not()) {
            _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.Shown) }
            topicUseCase.updateTopicsSectionShown(true)
        }
        updateUIStateAndNewsItems(isTopicSelected)
    }

    private fun updateUIStateAndNewsItems(isTopicSelected: Boolean) {
        _forYouState.update {
            it.copy(
                doneShownState = isTopicSelected,
                newsItems = newsUseCase.getNewsItemsByChoiceTopics(topicUseCase.topicItems),
                feedUIState = FeedUIState.Success
            )
        }
        _forYouState.update { it.copy(feedUIState = FeedUIState.Success) }
    }

    private fun initFeedData() {
        viewModelScope.launch {
            newsUseCase.newsItems.ifEmpty { newsUseCase.loadNewsItems() }
            topicUseCase.topicItems.ifEmpty { topicUseCase.loadTopicItems() }
            _forYouState.update { it.copy(topicItems = topicUseCase.topicItems) }
            updateTopicSelected()
        }
    }

    private fun updateMarkNews(newsId: String) {
        viewModelScope.launch { newsUseCase.changeNewsItemsById(newsId) }
        _forYouState.update { it.copy(newsItems = newsUseCase.newsItems) }
    }

    fun dispatchAction(action: ForYouAction) {
        when (action) {
            is ForYouAction.TopicSelected -> selectedTopic(action.topicId)
            is ForYouAction.DoneDispatch -> dispatchDone()
            is ForYouAction.MarkNews -> updateMarkNews(action.newsId)
        }
    }

}