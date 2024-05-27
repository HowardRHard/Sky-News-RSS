package com.howard.skynewsrss.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.howard.skynewsrss.data.NewsItem
import com.howard.skynewsrss.R
import com.howard.skynewsrss.data.NewsUiState
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

const val RSS_URL = "https://feeds.skynews.com/feeds/rss/%s.xml"

class NewsViewModel: ViewModel() {
    private val _newsItems = MutableStateFlow<NewsUiState>(NewsUiState.Empty)
    val newsItems: StateFlow<NewsUiState> = _newsItems

    lateinit var rssCategory: String

    init {
        getRSSNewsfeed()
    }

    private fun getRSSNewsfeed() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val newsList: MutableList<NewsItem> = emptyList<NewsItem>().toMutableList()
                    val rssChannel: RssChannel = RssParser().getRssChannel(String.format(RSS_URL, rssCategory))
                    rssChannel.items.forEach {
                        newsList.add(NewsItem(
                            it.title,
                            it.description,
                            it.image,
                            it.link)
                        )
                    }
                    _newsItems.value = NewsUiState.Success(newsList)
                } catch (e: UnknownHostException) {
                    _newsItems.value = NewsUiState.Error(e)
                } catch (e: Exception) {
                    _newsItems.value = NewsUiState.Error(e)
                }
            }
        }
    }

    fun getRandomFeedType(): Int {
        val feedNo = (0..3).random()

        return when (feedNo) {
            1 -> R.string.feed_uk
            2 -> R.string.feed_tech
            else -> R.string.feed_strange
        }
    }
}