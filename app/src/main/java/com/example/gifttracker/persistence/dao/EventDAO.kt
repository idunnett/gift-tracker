package com.example.gifttracker.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gifttracker.datamodels.Event
import com.example.gifttracker.datamodels.EventWithGifts

@Dao
interface EventDAO {

    @Query("SELECT * FROM Event")
    fun getEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM Event WHERE contactId = :contactID")
    fun getEventsOfContact(contactID: Int): LiveData<List<Event>>

    @Query("SELECT * FROM Event WHERE eventId = :eventID LIMIT 1")
    fun getEvent(eventID: Int): LiveData<Event>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Update
    suspend fun updateEvent(event: Event)

    @Transaction
    @Query("SELECT * FROM Event WHERE eventId = :eventID LIMIT 1")
    fun getEventWithGifts(eventID: Int): LiveData<EventWithGifts>
}