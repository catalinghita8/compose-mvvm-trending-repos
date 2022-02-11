package com.codingtroops.repositoriesapp.data.remote

import com.google.gson.annotations.SerializedName


data class RemoteRepository(
    @SerializedName("repo") var repoName: String? = null,
    @SerializedName("repo_link") var repoUrl: String? = null,
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("lang") var lang: String? = null,
    @SerializedName("stars") var stars: String? = null,
    @SerializedName("forks") var forks: String? = null,
    @SerializedName("added_stars") var addedStars: String? = null,
    @SerializedName("avatars") var avatarUrls: ArrayList<String> = arrayListOf()
)