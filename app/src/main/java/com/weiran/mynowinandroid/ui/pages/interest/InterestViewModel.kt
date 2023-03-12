package com.weiran.mynowinandroid.ui.pages.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.domain.TopicUseCase
import com.weiran.mynowinandroid.ui.pages.foryou.FeedUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InterestViewModel constructor(private val topicUseCase: TopicUseCase) : ViewModel() {

    private val _interestState = MutableStateFlow(InterestState())
    val interestState = _interestState.asStateFlow()

    fun fetchData() {
        _interestState.update {
            it.copy(
                topicItems = topicUseCase.topicItems,
                feedUIState = FeedUIState.Success
            )
        }
    }

    private fun selectedTopic(topicId: String) {
        viewModelScope.launch { topicUseCase.updateTopicSelected(topicId) }
        _interestState.update { it.copy(topicItems = topicUseCase.topicItems) }
    }

    fun onAction(action: InterestAction) {
        when (action) {
            is InterestAction.TopicSelected -> selectedTopic(action.topicId)
        }
    }
}