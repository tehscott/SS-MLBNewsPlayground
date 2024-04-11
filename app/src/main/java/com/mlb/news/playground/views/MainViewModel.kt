package com.mlb.news.playground.views

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.mlb.news.playground.newsfeed.Article
import com.mlb.news.playground.newsfeed.NewsFeedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val context: Context,
    val lifecycleOwner: LifecycleOwner,
    val newsFeedRepository: NewsFeedRepository,
) : ViewModel() {
    var currentArticles = MutableStateFlow<List<Article>>(listOf())

    init {
        newsFeedRepository.newsLiveData.observe(
            lifecycleOwner
        ) { articles ->
            Log.d("NewsFeedFragment", "articles retrieved")
            if (articles.isFailure) {
                Toast.makeText(
                    context,
                    articles.exceptionOrNull()?.message.toString(),
                    Toast.LENGTH_LONG,
                ).show()
            } else {
                currentArticles.update { articles.getOrNull().orEmpty() }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            newsFeedRepository.refreshNewsArticles()
        }
    }

    fun onCardClicked(url: String) {
        if (hasNetwork()) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } else {
            Toast.makeText(
                context,
                "No internet connection!",
                Toast.LENGTH_LONG,
            ).show()
        }
    }

    private fun hasNetwork(): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)

        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
}