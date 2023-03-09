package com.weiran.mynowinandroid.pages.foryou

sealed class ForYouAction {
    class TopicSelected(val topicId: String) : ForYouAction()
    object DoneDispatch : ForYouAction()
    class MarkNews(val newsId: String) : ForYouAction()
    object Refresh : ForYouAction()
}