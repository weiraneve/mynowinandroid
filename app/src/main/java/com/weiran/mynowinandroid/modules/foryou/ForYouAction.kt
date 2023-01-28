package com.weiran.mynowinandroid.modules.foryou

sealed class ForYouAction {
    data class TopicSelected(val topicId: String) : ForYouAction()
    object DoneDispatch : ForYouAction()
    data class MarkNews(val newsId: String) : ForYouAction()
}