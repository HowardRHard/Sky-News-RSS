package com.howard.skynewsrss.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.howard.skynewsrss.data.NewsItem
import com.howard.skynewsrss.R
import com.howard.skynewsrss.ui.theme.SkyNewsRSSTheme

@Composable
fun NewsListItem(newsItem: NewsItem) {
    val uriHandler = LocalUriHandler.current
    val feedLinkError = stringResource(id = R.string.news_link_error)

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(128.dp)
        .clickable {
            uriHandler.openUri(newsItem.newsAddress ?: feedLinkError)
        }
    ) {
        Row (modifier = Modifier.align(Alignment.Center)) {
            AsyncImage(
                model = newsItem.imageAddress,
                contentDescription = newsItem.title,
                modifier = Modifier.size(128.dp),
                contentScale = ContentScale.Crop,
            )
            Column (
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = newsItem.title ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = newsItem.description ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsListItemPreview() {
    SkyNewsRSSTheme {
        NewsListItem(NewsItem("Title", "Description", "", ""))
    }
}