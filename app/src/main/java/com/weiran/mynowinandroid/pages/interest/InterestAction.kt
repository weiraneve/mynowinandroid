package com.weiran.mynowinandroid.pages.interest

sealed class InterestAction {
    class TopicSelected(val topicId: String) : InterestAction()
}