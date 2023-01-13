package com.weiran.mynowinandroid.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.repository.interfaces.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    private val _savedState = MutableStateFlow(SavedState())
    val savedState = _savedState.asStateFlow()

    fun observeData() { updateSavedState() }

    private fun updateMarkNews(newsId: String) {
        viewModelScope.launch { newsRepository.changeNewsItemsById(newsId) }
        updateSavedState()
    }

    private fun updateSavedState() {
        _savedState.update {
            it.copy(
                markedNewsItems = newsRepository.loadMarkedNewsItems(),
                savedUIState = newsRepository.updateSavedUIState()
            )
        }
    }

    fun dispatchAction(action: SavedAction) {
        when (action) {
            is SavedAction.MarkNews -> updateMarkNews(action.newsId)
        }
    }

}