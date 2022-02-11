package com.codingtroops.repositoriesapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface ReposApiService {
    @GET("repo?since=monthly")
    suspend fun getRepositories(@Query("lang") language: String): RepositoriesResponse
}