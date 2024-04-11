package com.mlb.news.playground.newsfeed

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mlb.news.playground.db.ArticleLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KtorNewsLocalDataSource(
    private val articleLocalRepository: ArticleLocalRepository,
) : NewsDataSource {
    override val newsListLiveData = MutableLiveData<Result<List<Article>>>()

    override fun fetchNews() {
        CoroutineScope(Dispatchers.IO).launch {
            val localArticles = articleLocalRepository.getAll().orEmpty()
            newsListLiveData.postValue(Result.success(localArticles))
            Log.d("KtorNewsLocalDataSource", "local news fetch successful")
        }
    }
}
