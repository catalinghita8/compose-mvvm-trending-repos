package com.codingtroops.repositoriesapp.presentation.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtroops.repositoriesapp.domain.Language
import com.codingtroops.repositoriesapp.data.ReposRepository
import com.codingtroops.repositoriesapp.di.MainDispatcher
import com.codingtroops.repositoriesapp.domain.PredefinedContent
import com.codingtroops.repositoriesapp.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class RepositoryListViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: ReposRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = mutableStateOf(
        RepositoryListState(
            repos = listOf(),
            isLoading = true,
            languages = PredefinedContent.predefinedLanguages,
            selectedLanguage = retrieveSavedStateLanguage(),
            isSortingDescending = !retrieveSavedStateIsSortingAscending()
        )
    )
    val state: State<RepositoryListState>
        get() = _state

    init {
        getRepos(_state.value.selectedLanguage)
    }

    private fun getRepos(language: Language) {
        viewModelScope.launch(dispatcher) {
            try {
                val repos = repository.getRepos(language)
                _state.value = _state.value.copy(
                    repos = sortRepos(repos, _state.value.isSortingDescending),
                    isLoading = false
                )
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception) {
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }

    fun toggleSorting() {
        val repos = _state.value.repos
        val isDescendingSorting = !_state.value.isSortingDescending
        storeIsSortingAscending(!isDescendingSorting)
        _state.value = _state.value.copy(
            isSortingDescending = isDescendingSorting,
            repos = sortRepos(repos, isDescendingSorting)
        )
    }

    private fun sortRepos(repos: List<Repository>, isAscendingSorting: Boolean): List<Repository> {
        fun String.toNumber() = this.replace(",", "").toInt()
        return if (isAscendingSorting)
            repos.sortedByDescending { it.stars.toNumber() }
        else repos.sortedBy { it.stars.toNumber() }
    }

    fun setLanguage(language: Language) {
        storeLanguageToSavedHandle(language)
        _state.value = _state.value.copy(selectedLanguage = language, isLoading = true)
        getRepos(language)
    }

    private fun retrieveSavedStateLanguage(): Language {
        val savedLanguage = stateHandle.get<String?>(SAVED_HANDLE_LANGUAGE)
        if (savedLanguage != null)
            return Language.valueOf(savedLanguage)
        return Language.KOTLIN
    }

    private fun retrieveSavedStateIsSortingAscending(): Boolean {
        return stateHandle.get<Boolean?>(SAVED_HANDLE_ASCENDING_SORTING) ?: false
    }

    private fun storeIsSortingAscending(value: Boolean) {
        stateHandle.set(SAVED_HANDLE_ASCENDING_SORTING, value)
    }

    private fun storeLanguageToSavedHandle(language: Language) {
        stateHandle.set(SAVED_HANDLE_LANGUAGE, language.name)
    }

    companion object {
        private const val SAVED_HANDLE_LANGUAGE = "saved_language_handle"
        private const val SAVED_HANDLE_ASCENDING_SORTING = "saved_ascending_handle"

    }

}