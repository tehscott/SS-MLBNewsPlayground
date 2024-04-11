package com.mlb.news.playground.newsfeed

import androidx.lifecycle.MutableLiveData

interface NewsDataSource {
    val newsListLiveData: MutableLiveData<Result<List<Article>>>

    fun fetchNews()
}
