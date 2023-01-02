package com.weiran.mynowinandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.data.source.LocalStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

sealed class NewsAction {
    object TopicNewsSelected : NewsAction()
}

sealed class NewsFeedUIState {
    object Loading : NewsFeedUIState()
    object Success : NewsFeedUIState()
}

data class NewsState(
    val newsItems: List<NewsItem> = listOf(),
    val newsFeedUIState: NewsFeedUIState = NewsFeedUIState.Loading
)

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val localStorage: LocalStorage
) : ViewModel() {

    private val _newsState = MutableStateFlow(NewsState())
    val newsState = _newsState.asStateFlow()

    private val allNewsItems = localStorage.getNewsFromAssets()

    init {
        viewModelScope.launch { refreshNews() }
    }

    private suspend fun refreshNews() {
        viewModelScope.launch {
            _newsState.update {
                it.copy(
                    newsItems = getNewsItems(),
                    newsFeedUIState = NewsFeedUIState.Success
                )
            }
        }
    }

    private suspend fun getNewsItems(): List<NewsItem> {
        val selectedTopicItems = getSelectedTopicItems()
        return allNewsItems
            .filter { news -> !Collections.disjoint(news.topics, selectedTopicItems) }
            .map { news ->
                NewsItem(
                    id = news.id,
                    title = news.title,
                    content = news.content,
                    topics = findTopicById(news.topics)
                )
            }
    }

    private suspend fun findTopicById(topicIds: List<String>): List<TopicItem> {
        return topicIds.map { topicId ->
            val topicName = loadTopicItems().find { topicItem ->
                topicItem.id == topicId
            }?.name ?: ""
            TopicItem(
                id = topicId,
                name = topicName
            )
        }
    }

    private suspend fun getSelectedTopicItems(): List<String> {
        val selectedTopicItems = mutableListOf<String>()
        loadTopicItems().forEach {
            if (it.selected) {
                selectedTopicItems.add(it.id)
            }
        }
        return selectedTopicItems
    }

    private suspend fun loadTopicItems(): List<TopicItem> {
        return localStorage.getTopics().map {
            TopicItem(
                name = it.name,
                id = it.id,
                selected = it.selected
            )
        }
    }

    fun dispatchAction(action: NewsAction) {
        when (action) {
            is NewsAction.TopicNewsSelected -> viewModelScope.launch { refreshNews() }
        }
    }

}