package com.mlb.news.playground.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticleLocalDao {
    @Query("SELECT * FROM article")
    fun getAll(): List<ArticleLocal>?

    @Query("SELECT * FROM article WHERE id = :id")
    fun get(id: String): ArticleLocal?

    @Insert
    fun insert(category: ArticleLocal)

    @Delete
    fun delete(category: ArticleLocal)

    @Query("DELETE FROM article")
    fun deleteAll()
}