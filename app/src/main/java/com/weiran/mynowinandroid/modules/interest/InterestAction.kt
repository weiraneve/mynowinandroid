package com.weiran.mynowinandroid.modules.interest

sealed class InterestAction {
    data class TopicSelected(val topicId: String) : InterestAction()
}