package com.example.gifttracker.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gifttracker.datamodels.Gift

@Dao
interface GiftDAO {

    @Query("SELECT * FROM Gift")
    fun getGifts(): LiveData<List<Gift>>

    @Query("SELECT * FROM Gift WHERE eventId = :eventID")
    fun getGiftsOfEvent(eventID: Int): LiveData<List<Gift>>

    @Query("SELECT * FROM Gift WHERE giftId = :giftID LIMIT 1")
    fun getGift(giftID: Int): LiveData<Gift>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGift(gift: Gift)

    @Delete
    suspend fun deleteGift(gift: Gift)

    @Update
    suspend fun updateGift(gift: Gift)
}