package com.codingtroops.repositoriesapp

import androidx.lifecycle.SavedStateHandle
import com.codingtroops.repositoriesapp.data.ReposRepository
import com.codingtroops.repositoriesapp.domain.Language
import com.codingtroops.repositoriesapp.domain.PredefinedContent
import com.codingtroops.repositoriesapp.fakes.FakeApiService
import com.codingtroops.repositoriesapp.fakes.FakeDao
import com.codingtroops.repositoriesapp.presentation.list.RepositoryListState
import com.codingtroops.repositoriesapp.presentation.list.RepositoryListViewModel
import com.codingtroops.repositoriesapp.utils.MockValues
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryListViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun initialStateWithEmptySavedHandle_isReproduced() = scope.runTest {
        val testVM = getViewModel()
        Truth.assertThat(testVM.state.value).isEqualTo(
            RepositoryListState(
                repos = emptyList(),
                languages = PredefinedContent.predefinedLanguages,
                isLoading = true,
                selectedLanguage = Language.KOTLIN,
                isSortingDescending = true,
            )
        )
    }

    @Test
    fun finalStateWithEmptySavedHandle_isReproduced() = scope.runTest {
        val testVM = getViewModel()
        // Advance time so that the data requested by the ViewModel arrives
        advanceTimeBy(3000)

        Truth.assertThat(testVM.state.value).isEqualTo(
            RepositoryListState(
                repos = MockValues.domainKotlinRepos,
                languages = PredefinedContent.predefinedLanguages,
                isLoading = false,
                selectedLanguage = Language.KOTLIN,
                isSortingDescending = true,
            )
        )
    }

    @Test
    fun stateUponChangingLanguage_isReproduced() = scope.runTest {
        val viewModel = getViewModel()
        // Advance time so that the initial data with Kotlin repos requested by the ViewModel arrives
        advanceTimeBy(3000)

        // Update language to Java
        viewModel.setLanguage(Language.JAVA)

        // Verify intermediary loading state
        Truth.assertThat(viewModel.state.value).isEqualTo(
            RepositoryListState(
                repos = MockValues.domainKotlinRepos,
                languages = PredefinedContent.predefinedLanguages,
                isLoading = true,
                selectedLanguage = Language.JAVA,
                isSortingDescending = true,
            )
        )

        // Advance time so that the second data with Java repos requested by the ViewModel arrives
        advanceTimeBy(3000)
        Truth.assertThat(viewModel.state.value).isEqualTo(
            RepositoryListState(
                repos = MockValues.domainJavaRepos,
                languages = PredefinedContent.predefinedLanguages,
                isLoading = false,
                selectedLanguage = Language.JAVA,
                isSortingDescending = true,
            )
        )
    }

    // TODO test sorting

    private fun getViewModel(): RepositoryListViewModel {
        val fakeDao = FakeDao()
        val fakeRepo = ReposRepository(
            FakeApiService(),
            fakeDao,
            dispatcher
        )
        return RepositoryListViewModel(
            SavedStateHandle(),
            fakeRepo,
            dispatcher
        )
    }
}