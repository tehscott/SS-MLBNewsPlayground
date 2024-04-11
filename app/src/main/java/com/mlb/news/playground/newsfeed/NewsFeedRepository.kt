package com.mlb.news.playground.newsfeed

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mlb.news.playground.R
import com.mlb.news.playground.db.ArticleLocal
import com.mlb.news.playground.db.ArticleLocalRepository
import com.mlb.news.playground.db.MlbDatabase
import com.mlb.news.playground.di.NewsPlaygroundContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NewsFeedRepository(
    private val context: Context,
) {
    private val articleLocalRepository: ArticleLocalRepository = ArticleLocalRepository(
        Room.databaseBuilder(
            context,
            MlbDatabase::class.java, "mlb-database"
        ).build()
    )

    val newsLiveData = MutableLiveData<Result<List<Article>>>()

    fun refreshNewsArticles() {
        Log.d("NewsFeedRepository", "refreshNewsArticles")

        val hasNetwork = hasNetwork()
        val dataSource = if (hasNetwork) {
            NewsPlaygroundContainer.provideNewsRemoteDataSource(context)
        } else {
            NewsPlaygroundContainer.provideNewsLocalDataSource(context)
        }

        dataSource.apply {
            fetchNews()

            MainScope().launch {
                newsListLiveData.observeForever { news ->
                    if (news.isFailure) {
                        newsLiveData.postValue(Result.failure(news.exceptionOrNull() ?: Exception("news fetch failure")))
                    } else {
                        Log.d("NewsFeedRepository", "news fetch successful")
                        val articles = news.getOrThrow()

                        CoroutineScope(Dispatchers.IO).launch {
                            // Delete all
                            articleLocalRepository.deleteAll()

                            // Persist
                            articles.forEach { article ->
                                articleLocalRepository.insert(
                                    ArticleLocal(
                                        imageUrl = article.images.firstOrNull()?.url.orEmpty(),
                                        title = article.headline,
                                        description = article.description,
                                        articleUrl = article.links?.mobile?.href ?: context.getString(R.string.mlb_url)
                                    )
                                )
                            }
                        }

                        newsLiveData.postValue(Result.success(articles))
                    }
                }
            }
        }
    }

    private fun hasNetwork(): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)

        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

    fun parseJson(jsonString: String): NewsResponse {
        val type = object : TypeToken<NewsResponse>() {}.type
        return Gson().fromJson(jsonString, type)
    }
}
