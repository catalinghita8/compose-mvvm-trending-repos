package com.codingtroops.repositoriesapp.presentation.list

import com.codingtroops.repositoriesapp.domain.Language
import com.codingtroops.repositoriesapp.domain.Repository

data class RepositoryListState(
    val repos: List<Repository>,
    val languages: List<Language>,
    val selectedLanguage: Language,
    val isSortingDescending: Boolean,
    val isLoading: Boolean,
    val error: String? = null
)