package com.mlb.news.playground.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.mlb.news.playground.R
import com.mlb.news.playground.Destination
import com.mlb.news.playground.db.ArticleLocalRepository
import com.mlb.news.playground.db.MlbDatabase
import com.mlb.news.playground.newsfeed.NewsFeedRepository
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MainView() {
    val viewModel = MainViewModel(
        context = LocalContext.current,
        lifecycleOwner = LocalLifecycleOwner.current,
        newsFeedRepository = NewsFeedRepository(
           context = LocalContext.current,
        ),
    )

    val articles by viewModel.currentArticles.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(Destination.Home.title)
                },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(articles) { article ->
                val url = article.links?.mobile?.href ?: stringResource(R.string.mlb_url)

                Card(
                    border = BorderStroke(2.dp, colorResource(id = R.color.cardStrokeColor)),
                    onClick = {
                        viewModel.onCardClicked(url)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        GlideImage(
                            model = article.images.firstOrNull()?.url,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            failure = placeholder(R.mipmap.ic_launcher),
                            modifier = Modifier
                                .padding(4.dp)
                                .size(120.dp)
                        )
                        Column(
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Text(
                                text = article.headline,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = article.description,
                                fontSize = 14.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}