package com.weiran.mynowinandroid.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.usecase.NewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(private val newsUseCase: NewsUseCase) : ViewModel() {

    private val _savedState = MutableStateFlow(SavedState())
    val savedState = _savedState.asStateFlow()

    fun observeData() { updateFeedData() }

    private fun updateMarkNews(newsId: String) {
        viewModelScope.launch { newsUseCase.changeNewsItemsById(newsId) }
        updateFeedData()
    }

    private fun updateFeedData() {
        _savedState.update {
            it.copy(
                markedNewsItems = newsUseCase.loadMarkedNewsItems(),
                savedUIState = newsUseCase.updateSavedUIState()
            )
        }
    }

    fun dispatchAction(action: SavedAction) {
        when (action) {
            is SavedAction.MarkNews -> updateMarkNews(action.newsId)
        }
    }

}