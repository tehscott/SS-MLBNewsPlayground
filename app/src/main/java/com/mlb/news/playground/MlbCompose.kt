package com.mlb.news.playground

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mlb.news.playground.views.MainView

@Composable
fun MlbCompose() {
    MlbNavHost()
}

@Composable
fun MlbNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Destination.Home.route) {
        composable(route = Destination.Home.route) {
            MainView()
        }
    }
}

enum class Destination(val route: String, val title: String) {
    Home( "main", "News Playground"),
}