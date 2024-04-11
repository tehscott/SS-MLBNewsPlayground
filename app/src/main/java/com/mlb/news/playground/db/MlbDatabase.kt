package com.mlb.news.playground.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ArticleLocal::class,
    ],
    version = 1
)
abstract class MlbDatabase : RoomDatabase() {
    abstract fun articleLocalDao(): ArticleLocalDao
}