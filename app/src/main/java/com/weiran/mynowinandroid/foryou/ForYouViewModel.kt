package com.weiran.mynowinandroid.foryou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.repository.NewsRepository
import com.weiran.mynowinandroid.repository.TopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _forYouState = MutableStateFlow(ForYouState())
    val forYouState = _forYouState.asStateFlow()

    fun observeData() {
        viewModelScope.launch {
            _forYouState.update { it.copy(feedUIState = FeedUIState.Loading) }
            _forYouState.update {
                it.copy(
                    newsItems = newsRepository.loadNewsItems(),
                    topicItems = topicRepository.getTopicItems(),
                )
            }
            checkTopicsSection()
            checkTopicSelected()
        }
    }

    private fun checkTopicsSection() {
        if (!topicRepository.checkDoneShown()) {
            _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.NotShown) }
        }
    }

    private fun selectedTopic(topicId: String) {
        _forYouState.update { it.copy(feedUIState = FeedUIState.Loading) }
        viewModelScope.launch {
            topicRepository.updateTopicSelected(topicId)
            checkTopicSelected()
        }
        _forYouState.update { it.copy(topicItems = topicRepository.topicItems) }
    }

    private fun dispatchDone() {
        _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.NotShown) }
        topicRepository.updateDoneShown(false)
    }

    private fun checkTopicSelected() {
        val isTopicSelected =
            topicRepository.checkTopicItemIsSelected()
        if (!isTopicSelected) {
            _forYouState.update { it.copy(topicsSectionUIState = TopicsSectionUiState.Shown) }
            topicRepository.updateDoneShown(true)
        }
        updateUIStateAndNewsItems(isTopicSelected)
    }

    private fun updateUIStateAndNewsItems(isTopicSelected: Boolean) {
        _forYouState.update {
            it.copy(
                doneShownState = isTopicSelected,
                newsItems = newsRepository.getNewsByChoiceTopics(topicRepository.topicItems),
                feedUIState = FeedUIState.Success
            )
        }
    }

    private fun updateMarkNews(newsId: String) {
        viewModelScope.launch { newsRepository.changeNewsItemsById(newsId) }
        _forYouState.update { it.copy(newsItems = newsRepository.newsItems) }
    }

    fun dispatchAction(action: ForYouAction) {
        when (action) {
            is ForYouAction.TopicSelected -> selectedTopic(action.topicId)
            is ForYouAction.DoneDispatch -> dispatchDone()
            is ForYouAction.MarkNews -> updateMarkNews(action.newsId)
        }
    }

}