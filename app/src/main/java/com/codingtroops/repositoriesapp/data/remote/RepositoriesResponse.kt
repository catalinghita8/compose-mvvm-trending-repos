package com.codingtroops.repositoriesapp.data.remote

import com.codingtroops.repositoriesapp.data.remote.RemoteRepository
import com.google.gson.annotations.SerializedName


data class RepositoriesResponse(
    @SerializedName("count") val count: Int? = null,
    @SerializedName("msg") val message: String? = null,
    @SerializedName("items") val repositories: List<RemoteRepository> = listOf()
)