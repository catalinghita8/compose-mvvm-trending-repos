package com.codingtroops.repositoriesapp.domain



data class Repository(
    val key: String,
    val name: String,
    val description: String,
    val language: String,
    val stars: String,
    val avatarUrl: String
)