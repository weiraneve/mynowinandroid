package com.weiran.mynowinandroid.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.di.IoDispatcher
import com.weiran.mynowinandroid.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    //todo
    private val _savedState = MutableStateFlow(SavedState())
    val savedState = _savedState.asStateFlow()
    private var newsItems = emptyList<NewsItem>()
    
    init {
        viewModelScope.launch(ioDispatcher) {
            loadMarkedNews()
            newsItems = newsRepository.loadNewsItems()
        }
    }

    private suspend fun loadMarkedNews() {
        viewModelScope.launch {
            val markedNewsIds = newsRepository.getMarkedNewsIds()
            if (markedNewsIds.isNotEmpty()) {
                initMarkedNewsAndSavedUI(markedNewsIds)
            }
        }
    }

    private fun initMarkedNewsAndSavedUI(markedNewsIds: List<String>) =
        _savedState.update {
            it.copy(
                savedUIState = SavedUIState.NonEmpty,
                markedNewsItems = newsRepository.getMarkedNewsByIds(markedNewsIds)
            )
        }

    private fun updateMarkNews(newsId: String) {
        viewModelScope.launch {
            updateMarkedNewsItemsById(newsId)
            updateSavedUIState()
        }
    }

    private fun updateSavedUIState() {
        if (_savedState.value.markedNewsItems.isEmpty()) {
            _savedState.update { it.copy(savedUIState = SavedUIState.Empty) }
        } else {
            _savedState.update { it.copy(savedUIState = SavedUIState.NonEmpty) }
        }
    }

    private fun updateMarkedNewsItemsById(newsId: String) {
        _savedState.update {
            it.copy(markedNewsItems = newsItems.filter { newsItems -> newsItems.id == newsId })
        }
    }

    fun dispatchAction(action: SavedAction) {
        when (action) {
            is SavedAction.MarkNews -> updateMarkNews(action.newsId)
        }
    }

}