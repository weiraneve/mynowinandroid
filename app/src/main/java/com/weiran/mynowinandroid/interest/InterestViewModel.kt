package com.weiran.mynowinandroid.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.usecase.TopicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestViewModel @Inject constructor(private val topicUseCase: TopicUseCase) : ViewModel() {

    private val _interestState = MutableStateFlow(InterestState())
    val interestState = _interestState.asStateFlow()

    fun observeData() {
        _interestState.update { it.copy(topicItems = topicUseCase.topicItems) }
    }

    private fun selectedTopic(topicId: String) {
        viewModelScope.launch { topicUseCase.updateTopicSelected(topicId) }
        _interestState.update { it.copy(topicItems = topicUseCase.topicItems) }
    }

    fun dispatchAction(action: InterestAction) {
        when (action) {
            is InterestAction.TopicSelected -> selectedTopic(action.topicId)
        }
    }

}