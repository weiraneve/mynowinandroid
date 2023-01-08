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

    private val _savedState = MutableStateFlow(SavedState())
    val savedState = _savedState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            loadMarkedNews()
            updateSavedUIState()
        }
    }

    private fun loadMarkedNews() {
        _savedState.update { it.copy(markedNewsItems = newsRepository.getMarkedNewsItems()) }
    }

    private fun updateMarkNews(newsId: String) {
        viewModelScope.launch(ioDispatcher) {
            newsRepository.changeNewsItemsById(newsId)
            loadMarkedNews()
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

    fun dispatchAction(action: SavedAction) {
        when (action) {
            is SavedAction.MarkNews -> updateMarkNews(action.newsId)
        }
    }

}