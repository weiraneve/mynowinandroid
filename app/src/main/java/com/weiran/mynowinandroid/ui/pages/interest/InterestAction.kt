package com.weiran.mynowinandroid.ui.pages.interest

sealed class InterestAction {
    class TopicSelected(val topicId: String) : InterestAction()
}