package com.codingtroops.repositoriesapp.data.local


import androidx.room.*
import javax.inject.Singleton

@Singleton
@Dao
interface ReposDao {
    @Query("SELECT * FROM repos WHERE r_language = :language")
    suspend fun getByLanguage(language: String): List<LocalRepository>

    @Query("SELECT * FROM repos WHERE r_unique_key = :key")
    suspend fun getByKey(key: String): LocalRepository?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(repos: List<LocalRepository>)

}