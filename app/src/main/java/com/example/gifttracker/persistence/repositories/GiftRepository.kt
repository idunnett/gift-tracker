package com.example.gifttracker.persistence.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.gifttracker.datamodels.Gift
import com.example.gifttracker.persistence.dao.GiftDAO

class GiftRepository(private val giftDAO: GiftDAO) {

    val gifts : LiveData<List<Gift>> = giftDAO.getGifts()

    fun getGift(giftID: Int): LiveData<Gift> {
        return giftDAO.getGift(giftID)
    }

    @WorkerThread
    suspend fun insertGift(gift : Gift) {
        giftDAO.insertGift(gift)
    }

    @WorkerThread
    suspend fun deleteGift(gift: Gift) {
        giftDAO.deleteGift(gift)
    }

    @WorkerThread
    suspend fun updateGift(gift: Gift) {
        giftDAO.updateGift(gift)
    }
}