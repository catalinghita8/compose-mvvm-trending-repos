package com.codingtroops.repositoriesapp.fakes

import com.codingtroops.repositoriesapp.data.local.LocalRepository
import com.codingtroops.repositoriesapp.data.local.ReposDao
import kotlinx.coroutines.delay

class FakeDao : ReposDao {
    private var repos = arrayListOf<LocalRepository>()

    override suspend fun getByLanguage(language: String): List<LocalRepository> {
        delay(200)
        return repos.filter { it.language == language }
    }

    override suspend fun getByKey(key: String): LocalRepository? {
        delay(200)
        return repos.firstOrNull { it.key == key }
    }

    override suspend fun addAll(repos: List<LocalRepository>) {
        this.repos.addAll(repos)
    }
}