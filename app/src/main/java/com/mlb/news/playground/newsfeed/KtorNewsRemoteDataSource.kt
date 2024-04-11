package com.mlb.news.playground.newsfeed

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mlb.news.playground.R
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KtorNewsRemoteDataSource(
    private val client: HttpClient,
    private val context: Context,
    private val skipNetworkCall: Boolean, // If true, will load mock data from assets instead of making an API call to fetch the data.
    private val newsFeedRepository: NewsFeedRepository,
) : NewsDataSource {
    override val newsListLiveData = MutableLiveData<Result<List<Article>>>()

    override fun fetchNews() {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val data = if (skipNetworkCall) {
                    loadJsonFromAssets()
                } else {
                    client.get<String> {
                        url(context.getString(R.string.news_url))
                    }
                }

                newsFeedRepository.parseJson(data).articles
            }
                .onSuccess {
                    Log.d("KtorNewsRemoteDataSource", "news fetch successful")

                    newsListLiveData.postValue(Result.success(it))
                }
                .onFailure {
                    Log.e("KtorNewsRemoteDataSource", "error: $it")
                    newsListLiveData.postValue(Result.failure(it))
                }
        }
    }

    private fun loadJsonFromAssets(): String {
        return context.assets.open("mock_news_data.json").bufferedReader().use { it.readText() }
    }
}
