package com.codingtroops.repositoriesapp.data.local


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [LocalRepository::class],
    version = 2,
    exportSchema = false
)
abstract class ReposDb : RoomDatabase() {
    abstract val dao: ReposDao
}