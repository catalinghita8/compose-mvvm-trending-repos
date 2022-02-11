package com.codingtroops.repositoriesapp.data

import com.codingtroops.repositoriesapp.data.local.LocalRepository
import com.codingtroops.repositoriesapp.data.local.ReposDao
import com.codingtroops.repositoriesapp.data.remote.RemoteRepository
import com.codingtroops.repositoriesapp.data.remote.ReposApiService
import com.codingtroops.repositoriesapp.di.IoDispatcher
import com.codingtroops.repositoriesapp.di.MainDispatcher
import com.codingtroops.repositoriesapp.domain.Language
import com.codingtroops.repositoriesapp.domain.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReposRepository @Inject constructor(
    private val restInterface: ReposApiService,
    private val reposDao: ReposDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun getRepos(language: Language): List<Repository> {
        return withContext(dispatcher) {
            try {
                refreshCache(language)
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        if (reposDao.getByLanguage(language = language.name).isEmpty())
                            throw Exception("Something went wrong. We have no data.")
                    }
                    else -> throw e
                }
            }
            return@withContext reposDao
                .getByLanguage(language = language.text)
                .mapToDomainModel()
        }
    }

    suspend fun getRepoByKey(key: String): Repository? {
        return reposDao.getByKey(key)?.mapToLocalModel()
    }

    private suspend fun refreshCache(language: Language) {
        val remoteRepos = restInterface.getRepositories(language = language.name).repositories
        reposDao.addAll(remoteRepos.mapToLocalModel())
    }

    private fun LocalRepository.mapToLocalModel(): Repository {
        return Repository(
            key = this.key,
            name = this.name,
            description = this.description,
            language = this.language,
            stars = this.stars,
            avatarUrl = this.avatarUrl
        )
    }

    private fun List<RemoteRepository>.mapToLocalModel(): List<LocalRepository> {
        return this
            .filter { it.repoUrl != null }
            .map {
                LocalRepository(
                    key = constructUniqueId(it.repoUrl!!),
                    name = it.repoName ?: "N/A",
                    description = it.desc ?: "N/A",
                    language = it.lang ?: "N/A",
                    stars = it.stars ?: "N/A",
                    avatarUrl = it.avatarUrls.firstOrNull() ?: ""
                )
            }
    }

    private fun List<LocalRepository>.mapToDomainModel(): List<Repository> {
        return this.map { it.mapToLocalModel() }
    }

    private fun constructUniqueId(value: String): String {
        return value
            .replace("https://", "")
            .replace("/", "_")
    }
}