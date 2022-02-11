package com.codingtroops.repositoriesapp.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codingtroops.repositoriesapp.domain.Repository
import com.codingtroops.repositoriesapp.presentation.list.RepositoryDetails
import com.codingtroops.repositoriesapp.presentation.list.RepositoryIcon
import com.codingtroops.repositoriesapp.presentation.list.RepositoryStars
import com.codingtroops.repositoriesapp.theme.RepositoriesAppTheme
import com.codingtroops.repositoriesapp.utils.MockRepositories

@Composable
fun RepositoryDetailsScreen(item: Repository?) {
    if (item != null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            RepositoryIcon(
                item.avatarUrl,
                Modifier
                    .padding(top = 32.dp, bottom = 32.dp)
                    .weight(0.3f)
                    .fillMaxSize()
            )
            Column(
                modifier = Modifier.weight(0.85f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                RepositoryDetails(
                    title = item.name,
                    description = item.description,
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    textAlignment = TextAlign.Center
                )
                RepositoryStars(
                    count = item.stars,
                    modifier = Modifier.wrapContentSize()
                )
                Text(text = "More info coming soon!")
            }
        }
    }
}

@Preview
@Composable
fun RepositoryDetailsScreenPreview() {
    RepositoriesAppTheme {
        RepositoryDetailsScreen(MockRepositories.repos.firstOrNull())
    }
}