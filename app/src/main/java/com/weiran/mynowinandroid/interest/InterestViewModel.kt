package com.weiran.mynowinandroid.interest

import androidx.lifecycle.ViewModel
import com.weiran.mynowinandroid.data.source.LocalStorage
import com.weiran.mynowinandroid.di.IoDispatcher
import com.weiran.mynowinandroid.repository.NewsRepository
import com.weiran.mynowinandroid.repository.TopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class InterestViewModel @Inject constructor(
    private val localStorage: LocalStorage,
    private val topicRepository: TopicRepository,
    private val newsRepository: NewsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {


}