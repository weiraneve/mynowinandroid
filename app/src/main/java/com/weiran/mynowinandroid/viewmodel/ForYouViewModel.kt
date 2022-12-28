package com.weiran.mynowinandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.source.fakeTopics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ForYouAction {
    data class TopicClickAction(val topicId: String) : ForYouAction()
}

data class ForYouState(
    val topics: List<Topic> = listOf()
)

class ForYouViewModel : ViewModel() {

    private val _forYouState = MutableStateFlow(ForYouState())
    val forYouState = _forYouState.asStateFlow()

    init {
        viewModelScope.launch {
            _forYouState.update {
                it.copy(
                    topics = fakeTopics
                )
            }
        }
    }

    private fun clickTopicSelected(topicId: String) {
        viewModelScope.launch {
            _forYouState.update {
                it.copy(
                    topics = _forYouState.value.topics.map { topic ->
                        if (topic.id == topicId) {
                            topic.copy(selected = !topic.selected)
                        } else {
                            topic
                        }
                    }
                )
            }
        }
    }

    fun dispatchAction(action: ForYouAction) {
        when (action) {
            is ForYouAction.TopicClickAction -> clickTopicSelected(action.topicId)
        }
    }

}