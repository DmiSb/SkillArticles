package ru.skillbranch.skillarticles.viewmodels.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.skillarticles.viewmodels.ArticleViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val args: Any?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(args as? String? ?: "0") as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}