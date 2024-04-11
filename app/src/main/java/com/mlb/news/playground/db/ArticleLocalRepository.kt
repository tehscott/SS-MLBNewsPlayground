package com.mlb.news.playground.db

import com.mlb.news.playground.newsfeed.Article
import com.mlb.news.playground.newsfeed.Image
import com.mlb.news.playground.newsfeed.Links
import com.mlb.news.playground.newsfeed.Mobile

class ArticleLocalRepository(
    private val database: MlbDatabase,
) {
    fun get(id: String): Article? =
        database.articleLocalDao().get(id)?.let { remote ->
            toArticleDomain(remote)
        }

    fun getAll(): List<Article>? =
        database.articleLocalDao().getAll()?.map { remote ->
            toArticleDomain(remote)
        }

    fun delete(articleLocal: ArticleLocal) =
        database.articleLocalDao().delete(articleLocal)

    fun insert(articleLocal: ArticleLocal) =
        database.articleLocalDao().insert(articleLocal)

    fun deleteAll() =
        database.articleLocalDao().deleteAll()

    // Barebones for now
    private fun toArticleDomain(articleLocal: ArticleLocal) = Article(
        dataSourceIdentifier = articleLocal.id,
        description = articleLocal.description,
        type = "",
        premium = false,
        links = Links(
            api = null,
            web = null,
            mobile = Mobile(
                href = articleLocal.articleUrl
            ),
        ),
        categories = listOf(),
        headline = articleLocal.title,
        images = listOf(
            Image(
                url = articleLocal.imageUrl
            )
        ),
        published = "",
        lastModified = "",
        byline = "",
    )
}