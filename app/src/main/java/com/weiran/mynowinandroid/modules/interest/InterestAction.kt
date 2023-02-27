package com.weiran.mynowinandroid.modules.interest

sealed class InterestAction {
    class TopicSelected(val topicId: String) : InterestAction()
}