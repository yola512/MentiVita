package com.example.alzapp.data.games.cryptogram

import com.example.alzapp.data.games.cryptogram.local.ProverbDao
import com.example.alzapp.data.games.cryptogram.local.ProverbEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptogramRepository @Inject constructor(
    private val proverbDao: ProverbDao
) {
    suspend fun getUnfinishedProverb(): ProverbEntity? {
        return proverbDao.getUnfinishedProverb()
    }
    suspend fun getRandomNewProverb(): ProverbEntity? {
        return proverbDao.getRandomNewProverb()
    }
    suspend fun updateProverb(proverb: ProverbEntity) {
        proverbDao.updateProverb(proverb)
    }
}