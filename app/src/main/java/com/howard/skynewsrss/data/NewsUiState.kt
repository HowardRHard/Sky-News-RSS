package com.howard.skynewsrss.data

sealed class NewsUiState {
    data class Success(val news: MutableList<NewsItem>): NewsUiState()
    data class Error(val exception: Throwable): NewsUiState()
    data object Empty: NewsUiState()
}