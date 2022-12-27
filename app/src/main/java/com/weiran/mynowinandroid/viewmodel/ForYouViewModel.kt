package com.weiran.mynowinandroid.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiran.mynowinandroid.data.model.Topic
import com.weiran.mynowinandroid.data.source.LocalStorageImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ForYouAction {
    data class GetTopicsAction(val context: Context) : ForYouAction()
}

data class ForYouState(
    val topics: List<Topic> = listOf()
)

class ForYouViewModel : ViewModel() {

    private val _forYouState = MutableStateFlow(ForYouState())
    val forYouState = _forYouState.asStateFlow()

    private fun getTopics(context: Context) {
        viewModelScope.launch {
            _forYouState.update {
                it.copy(
                    topics = LocalStorageImpl(context).getTopics()
                )
            }
        }
    }

    fun dispatchAction(action: ForYouAction) {
        when (action) {
            is ForYouAction.GetTopicsAction -> getTopics(action.context)
        }
    }

}