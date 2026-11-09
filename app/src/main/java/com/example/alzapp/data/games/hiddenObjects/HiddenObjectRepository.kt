package com.example.alzapp.data.games.hiddenObjects

import com.example.alzapp.data.games.hiddenObjects.local.HiddenObjectDao
import com.example.alzapp.data.games.hiddenObjects.local.HiddenObjectEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HiddenObjectRepository @Inject constructor(
    private val hiddenObjectDao: HiddenObjectDao
) {
    suspend fun getUnfinishedGame(): HiddenObjectEntity? {
        return hiddenObjectDao.getUnfinishedGame()
    }
    suspend fun insertGame(game: HiddenObjectEntity): Long {
       return hiddenObjectDao.insertGame(game)
    }
}