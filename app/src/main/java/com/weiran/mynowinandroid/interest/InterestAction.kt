package com.weiran.mynowinandroid.interest

sealed class InterestAction {
    data class TopicSelected(val topicId: String) : InterestAction()
}