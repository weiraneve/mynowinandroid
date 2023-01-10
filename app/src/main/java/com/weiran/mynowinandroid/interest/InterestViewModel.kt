package com.weiran.mynowinandroid.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.di.IoDispatcher
import com.weiran.mynowinandroid.repository.TopicRepository
import com.weiran.mynowinandroid.theme.MyIcons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _interestState = MutableStateFlow(InterestState())
    val interestState = _interestState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) { initTopicItems() }
    }

    private suspend fun initTopicItems() =
        _interestState.update { it.copy(topicItems = topicRepository.getTopicItems()) }

    private fun selectedTopic(topicId: String) = updateTopic(topicId)

    private fun updateTopic(topicId: String) =
        _interestState.update { it.copy(topicItems = getTopicItemsByTopicId(topicId)) }

    private fun getTopicItemsByTopicId(topicId: String) = _interestState.value.topicItems.map {
        if (it.id == topicId) {
            val icon = if (it.selected) MyIcons.Add else MyIcons.Check
            it.copy(selected = !it.selected, icon = icon)
        } else {
            it
        }
    }

    fun dispatchAction(action: InterestAction) {
        when (action) {
            is InterestAction.TopicSelected -> selectedTopic(action.topicId)
        }
    }

}