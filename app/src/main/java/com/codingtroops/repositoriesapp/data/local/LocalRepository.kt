package com.codingtroops.repositoriesapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
data class LocalRepository(
    @PrimaryKey()
    @ColumnInfo(name = "r_unique_key")
    val key: String,

    @ColumnInfo(name = "r_name")
    val name: String,

    @ColumnInfo(name = "r_description")
    val description: String,

    @ColumnInfo(name = "r_language")
    val language: String,

    @ColumnInfo(name = "r_stars")
    val stars: String,

    @ColumnInfo(name = "r_avatar_url")
    val avatarUrl: String
)