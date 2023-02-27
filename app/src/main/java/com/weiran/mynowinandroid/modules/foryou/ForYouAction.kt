package com.weiran.mynowinandroid.modules.foryou

sealed class ForYouAction {
    class TopicSelected(val topicId: String) : ForYouAction()
    object DoneDispatch : ForYouAction()
    class MarkNews(val newsId: String) : ForYouAction()
}