package com.weiran.mynowinandroid.foryou

sealed class FeedAction {
    data class TopicSelected(val topicId: String) : FeedAction()
    object DoneDispatch : FeedAction()
    data class MarkNews(val newsId: String) : FeedAction()
}