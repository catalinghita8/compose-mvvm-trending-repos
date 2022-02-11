package com.codingtroops.repositoriesapp.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.codingtroops.repositoriesapp.domain.Language
import com.codingtroops.repositoriesapp.R
import com.codingtroops.repositoriesapp.domain.Repository
import com.codingtroops.repositoriesapp.theme.RepositoriesAppTheme
import com.codingtroops.repositoriesapp.utils.ContentDescription
import com.codingtroops.repositoriesapp.utils.MockRepositories
import com.codingtroops.repositoriesapp.utils.Tag

@Composable
fun RepositoriesListScreen(
    state: RepositoryListState,
    onItemClick: (id: String) -> Unit,
    onLanguageSelected: (language: Language) -> Unit,
    onSortingToggled: () -> Unit
) {
    Scaffold(
        topBar = {
            ReposListAppBar(state.isSortingDescending, onSortingToggled)
        }
    ) {
        Column {
            LanguageDropdown(state.selectedLanguage, state.languages, onLanguageSelected)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                RepositoryList(state.repos, onItemClick)
                if (state.isLoading)
                    CircularProgressIndicator(modifier = Modifier.testTag(Tag.LOADING))
                if (state.error != null)
                    RepositoriesError(state.error)
            }
        }
    }
}

@Composable
private fun ReposListAppBar(isSortingDescending: Boolean, onSortingToggled: () -> Unit) {
    TopAppBar {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Trending repos", modifier = Modifier
                    .weight(0.85f)
                    .padding(16.dp)
            )
            Row(
                modifier = Modifier
                    .weight(0.15f)
                    .clickable { onSortingToggled() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_star_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
                Image(
                    imageVector = if (isSortingDescending)
                        Icons.Default.KeyboardArrowDown
                    else
                        Icons.Default.KeyboardArrowUp,
                    contentDescription = "Stars sorting icon",
                    modifier = Modifier.padding(start = 4.dp, end = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun RepositoriesError(error: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(Color.LightGray)
            .testTag(Tag.ERROR_BOX)
    ) {
        Text(error)
    }
}

@Composable
private fun RepositoryList(
    repos: List<Repository>,
    onItemClick: (id: String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 8.dp
        ), modifier = Modifier.fillMaxSize()
    ) {
        items(repos) { repo ->
            RepositoryItem(
                item = repo,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun RepositoryItem(
    item: Repository,
    onItemClick: (id: String) -> Unit
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onItemClick(item.key) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .heightIn(96.dp, 96.dp)
        ) {
            RepositoryIcon(
                url = item.avatarUrl, modifier = Modifier
                    .weight(0.2f)
                    .fillMaxSize()
            )
            RepositoryDetails(
                title = item.name,
                description = item.description,
                modifier = Modifier.weight(0.55f)
            )
            RepositoryStars(
                count = item.stars,
                modifier = Modifier.weight(0.25f)
            )
        }
    }
}

@Composable
fun RepositoryIcon(url: String, modifier: Modifier) {
    Image(
        painter = rememberImagePainter(
            data = url,
            builder = {
                transformations(CircleCropTransformation())
            }
        ),
        contentDescription = ContentDescription.STARS_COUNT,
        modifier = modifier.padding(8.dp)
    )
}

@Composable
fun RepositoryStars(count: String, modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_star_svgrepo_com),
                contentDescription = null,
                modifier = modifier.size(24.dp)
            )
            Text(
                text = count,
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(48.dp)
                    .padding(start = 2.dp)
            )
        }
    }
}

@Composable
fun RepositoryDetails(
    title: String,
    description: String,
    modifier: Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    textAlignment: TextAlign = TextAlign.Left
) {
    Column(
        modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = textAlignment
        )
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.medium
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.body2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = textAlignment
            )
        }
    }
}

@Composable
fun LanguageDropdown(
    selectedLanguage: Language,
    languages: List<Language>,
    onLanguageSelected: (language: Language) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .testTag(Tag.LANGUAGE_DROPDOWN)
            .wrapContentSize(Alignment.TopStart)
            .clickable(onClick = { expanded = true })
    ) {
        Card(
            elevation = 4.dp,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedLanguage.text,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Image(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Language dropdown"
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            languages.forEachIndexed { index, language ->
                DropdownMenuItem(onClick = {
                    onLanguageSelected(languages[index])
                    expanded = false
                }) {
                    Text(text = language.text)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RepositoriesAppTheme {
        RepositoriesListScreen(
            RepositoryListState(
                repos = MockRepositories.repos,
                isLoading = true,
                languages = listOf(Language.JAVA),
                isSortingDescending = true,
                selectedLanguage = Language.JAVA,
                error = null
            ),
            {}, {}, {})
    }
}