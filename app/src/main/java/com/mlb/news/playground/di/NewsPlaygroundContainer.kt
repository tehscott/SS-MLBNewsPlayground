package com.mlb.news.playground.di

import android.content.Context
import androidx.room.Room
import com.mlb.news.playground.db.ArticleLocalRepository
import com.mlb.news.playground.db.MlbDatabase
import com.mlb.news.playground.newsfeed.KtorNewsLocalDataSource
import com.mlb.news.playground.newsfeed.KtorNewsRemoteDataSource
import com.mlb.news.playground.newsfeed.NewsDataSource
import com.mlb.news.playground.newsfeed.NewsFeedRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

object NewsPlaygroundContainer : NewsPlaygroundContainerInterface {
    override fun provideNewsRemoteDataSource(context: Context): NewsDataSource {
        return KtorNewsRemoteDataSource(
            client = provideKtorClient(),
            context = context,
            skipNetworkCall = false,
            newsFeedRepository = NewsFeedRepository(
                context = context,
            )
        )
    }

    override fun provideNewsLocalDataSource(context: Context): NewsDataSource {
        return KtorNewsLocalDataSource(
            ArticleLocalRepository(
                Room.databaseBuilder(
                    context,
                    MlbDatabase::class.java, "mlb-database"
                ).build()
            )
        )
    }

    private fun provideKtorClient(): HttpClient {
        return HttpClient(Android) { install(JsonFeature) { serializer = KotlinxSerializer() } }
    }
}

// Interface for testing
interface NewsPlaygroundContainerInterface {
    fun provideNewsRemoteDataSource(context: Context): NewsDataSource
    fun provideNewsLocalDataSource(context: Context): NewsDataSource
}
