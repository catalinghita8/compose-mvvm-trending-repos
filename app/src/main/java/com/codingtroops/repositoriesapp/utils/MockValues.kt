package com.codingtroops.repositoriesapp.utils

import com.codingtroops.repositoriesapp.data.remote.RemoteRepository
import com.codingtroops.repositoriesapp.domain.Language
import com.codingtroops.repositoriesapp.domain.Repository

object MockValues {
    fun getResponseRepos(language: String): List<RemoteRepository> {
        return when (language) {
            Language.KOTLIN.name -> responseKotlinRepos
            Language.JAVA.name -> responseJavaRepos
            else -> responseKotlinRepos
        }
    }

    val domainKotlinRepos = listOf(
        Repository(
            "2",
            "A Demo repo",
            "Demo description Demo description Demo description Demo description Demo description Demo description Demo description",
            "Kotlin",
            "11,908",
            "https://avatars.githubusercontent.com/u/45714956?s=40&v=4"
        ),
        Repository(
            "24",
            "B Demo repo",
            "Demo description Demo description Demo description Demo description Demo description Demo description Demo description",
            "Kotlin",
            "11,908",
            "https://avatars.githubusercontent.com/u/45714956?s=40&v=4"
        ),
        Repository(
            "25",
            "C Demo repo",
            "Demo description Demo description Demo description Demo description Demo description Demo description Demo description",
            "Kotlin",
            "11,908",
            "https://avatars.githubusercontent.com/u/45714956?s=40&v=4"
        ),
    )

    val domainJavaRepos = listOf(
        Repository(
            "498",
            "A Demo Java repo",
            "Demo description Demo description Demo description Demo description Demo description Demo description Demo description",
            "Java",
            "3,333",
            "https://avatars.githubusercontent.com/u/4574956?s=40&v=4"
        )
    )

    private val responseKotlinRepos = domainKotlinRepos.map {
        RemoteRepository(
            it.name,
            it.key,
            it.description,
            it.language,
            it.stars,
            "",
            "",
            arrayListOf(it.avatarUrl),
        )
    }

    private val responseJavaRepos = domainJavaRepos.map {
        RemoteRepository(
            it.name,
            it.key,
            it.description,
            it.language,
            it.stars,
            "",
            "",
            arrayListOf(it.avatarUrl),
        )
    }
}