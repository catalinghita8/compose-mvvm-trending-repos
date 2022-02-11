package com.codingtroops.repositoriesapp.presentation.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtroops.repositoriesapp.utils.NavConstants
import com.codingtroops.repositoriesapp.data.ReposRepository
import com.codingtroops.repositoriesapp.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RepositoryDetailsViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val reposRepository: ReposRepository
) : ViewModel() {
    private val _state = mutableStateOf<Repository?>(null)

    val state: State<Repository?>
        get() = _state

    init {
        val id = stateHandle.get<String>(NavConstants.RepoDetailsKey)
            ?: throw Exception("Couldn't find repo.")
        viewModelScope.launch {
            val repository = getRepository(id)
            _state.value = repository
        }
    }

    private suspend fun getRepository(key: String): Repository? {
        return withContext(Dispatchers.IO) {
            return@withContext reposRepository.getRepoByKey(key)
        }
    }
}