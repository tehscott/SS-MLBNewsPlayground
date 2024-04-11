package com.mlb.news.playground.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "article")
data class ArticleLocal(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "image_url") var imageUrl: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "article_url") var articleUrl: String,
)