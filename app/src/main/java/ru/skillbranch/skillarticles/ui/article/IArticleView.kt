package ru.skillbranch.skillarticles.ui.article

import ru.skillbranch.skillarticles.viewmodels.base.Loading

interface IArticleView {
    // Показать search bar
    fun showSearchBar()

    // Скрыть search bar
    fun hideSearchBar()

    fun renderLoading(loadingState: Loading)
}