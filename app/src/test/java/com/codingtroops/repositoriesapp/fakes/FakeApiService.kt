package com.codingtroops.repositoriesapp.fakes

import com.codingtroops.repositoriesapp.utils.MockValues
import com.codingtroops.repositoriesapp.data.remote.ReposApiService
import com.codingtroops.repositoriesapp.data.remote.RepositoriesResponse
import kotlinx.coroutines.delay

class FakeApiService: ReposApiService {
    override suspend fun getRepositories(language: String): RepositoriesResponse {
        delay(1000)
        return RepositoriesResponse(repositories = MockValues.getResponseRepos(language))
    }
}