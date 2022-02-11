package com.codingtroops.repositoriesapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.codingtroops.repositoriesapp.domain.Language
import com.codingtroops.repositoriesapp.domain.PredefinedContent
import com.codingtroops.repositoriesapp.presentation.list.RepositoriesListScreen
import com.codingtroops.repositoriesapp.presentation.list.RepositoryListState
import com.codingtroops.repositoriesapp.theme.RepositoriesAppTheme
import com.codingtroops.repositoriesapp.utils.ContentDescription
import com.codingtroops.repositoriesapp.utils.MockValues
import com.codingtroops.repositoriesapp.utils.Tag

import org.junit.Test

import org.junit.Rule

class RepositoryListComposableTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialState_isDisplayed() {
        composeTestRule.setContent {
            RepositoriesAppTheme {
                RepositoriesListScreen(
                    state = RepositoryListState(
                        repos = emptyList(),
                        languages = PredefinedContent.predefinedLanguages,
                        isLoading = true,
                        selectedLanguage = Language.KOTLIN,
                        isSortingDescending = true,
                    ),
                    onSortingToggled = { },
                    onLanguageSelected = { },
                    onItemClick = { }
                )
            }
        }

        composeTestRule.onNodeWithTag(Tag.LOADING).assertIsDisplayed()
        composeTestRule.onNodeWithTag(Tag.LANGUAGE_DROPDOWN).apply {
            assertIsDisplayed()
            assertTextEquals(Language.KOTLIN.text)
        }
        composeTestRule.onNodeWithTag(Tag.ERROR_BOX).assertDoesNotExist()

        // Print merged tree
        composeTestRule.onRoot().printToLog("TAG")
    }

    @Test
    fun stateWithRepos_isDisplayed() {
        composeTestRule.setContent {
            RepositoriesAppTheme {
                RepositoriesListScreen(
                    state = RepositoryListState(
                        repos = MockValues.domainKotlinRepos,
                        languages = PredefinedContent.predefinedLanguages,
                        isLoading = false,
                        selectedLanguage = Language.KOTLIN,
                        isSortingDescending = true,
                    ),
                    onSortingToggled = { },
                    onLanguageSelected = { },
                    onItemClick = { }
                )
            }
        }

        composeTestRule.onNodeWithTag(Tag.LANGUAGE_DROPDOWN).apply {
            assertIsDisplayed()
            assertTextEquals(Language.KOTLIN.text)
        }
        composeTestRule.onNodeWithTag(Tag.LOADING).assertDoesNotExist()
        composeTestRule
            .onAllNodesWithContentDescription(ContentDescription.STARS_COUNT)
            .assertCountEquals(3)
    }

    @Test
    fun clickFirstRepo_callbackIsTriggered() {
        composeTestRule.setContent {
            RepositoriesAppTheme {
                RepositoriesListScreen(
                    state = RepositoryListState(
                        repos = MockValues.domainKotlinRepos,
                        languages = PredefinedContent.predefinedLanguages,
                        isLoading = false,
                        selectedLanguage = Language.KOTLIN,
                        isSortingDescending = true,
                    ),
                    onSortingToggled = { },
                    onLanguageSelected = { },
                    onItemClick = { key ->
                        assert(key == MockValues.domainKotlinRepos[0].key)
                    }
                )
            }
        }
        composeTestRule
            .onAllNodesWithContentDescription(ContentDescription.STARS_COUNT)
            .onFirst()
            .performClick()
    }
}