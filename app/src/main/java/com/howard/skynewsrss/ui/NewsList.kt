package com.howard.skynewsrss.ui

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.howard.skynewsrss.R
import com.howard.skynewsrss.data.NewsUiState
import com.howard.skynewsrss.ui.theme.SkyNewsRSSTheme
import java.net.UnknownHostException
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsList() {
    val newsViewModel = viewModel<NewsViewModel>()

    // Get the random feed type then set that in the viewModel, so the request can be made
    var category by remember { mutableStateOf("") }
    if(category.isEmpty()) {
        category = stringResource(id = newsViewModel.getRandomFeedType())
    }
    newsViewModel.rssCategory = category

    // Change the list divider colour based on if dark mode is installed
    val dividerColor = if(isDarkTheme(LocalContext.current as Activity)) {
        Color.Black
    } else { 
        Color.White
    }

    newsViewModel.newsItems.collectAsState().value.let { newsItems ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(stringResource(id = R.string.feed_title, "${category.capitalize(Locale.ROOT)}"))
                    }
                )
                when (newsItems) {
                    // If the RSS feed is loaded, then populate the screen with a list of news items
                    is NewsUiState.Success -> {
                        LazyColumn {
                            items(newsItems.news.size) { index ->
                                NewsListItem(newsItem = newsItems.news[index])
                                Divider(color = dividerColor, thickness = 2.dp)
                            }
                        }
                    }
                    // If there are any errors, then display them to the user
                    is NewsUiState.Error -> {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(
                                text = newsItems.exception.message.toString(),
                                textAlign = TextAlign.Center,
                                style = typography.headlineMedium,
                            )
                            if (newsItems.exception is UnknownHostException) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                    text = "Try checking your internet connection",
                                    textAlign = TextAlign.Center,
                                    style = typography.titleMedium,
                                )
                            }
                        }
                    }
                    // Default State
                    else -> Unit
                }
            }
        }
    }
}

fun isDarkTheme(activity: Activity): Boolean {
    return activity.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO
}

@Preview(showBackground = true)
@Composable
fun NewsListPreview() {
    SkyNewsRSSTheme {
        NewsList()
    }
}