package com.weiran.mynowinandroid.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        viewModelScope.launch(ioDispatcher) {
            loadMarkedNews()
        }
    }

    private val _savedState = MutableStateFlow(SavedState())
    val savedState = _savedState.asStateFlow()

    private suspend fun loadMarkedNews() {
        viewModelScope.launch {
            val markedNewsIds = newsRepository.getMarkedNewsIds()
            if (markedNewsIds.isNotEmpty()) {
                initMarkedNewsAndSavedUI(markedNewsIds)
            }
        }
    }

    private suspend fun initMarkedNewsAndSavedUI(markedNewsIds: List<String>) =
        _savedState.update {
            it.copy(
                savedUIState = SavedUIState.NonEmpty,
                markedNewsItems = newsRepository.getMarkedNewsByIds(markedNewsIds)
            )
        }

    private fun updateMarkNews(newsId: String) {
        viewModelScope.launch {
            updateNewsItemsMarkedById(newsId)
            updateMarkedNewsItems()
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

    private fun updateMarkedNewsItems() {
        _savedState.update {
            it.copy(markedNewsItems = _savedState.value.newsItems.filter { newsItem -> newsItem.isMarked })
        }
    }

    private suspend fun updateNewsItemsMarkedById(newsId: String) {
        _savedState.update {
            it.copy(
                newsItems = newsRepository.getMarkedNewsItemsById(
                    newsId,
                    _savedState.value.newsItems
                )
            )
        }
    }

    fun dispatchAction(action: SavedAction) {
        when (action) {
            is SavedAction.MarkNews -> updateMarkNews(action.newsId)
        }
    }

}