package com.example.gifttracker.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gifttracker.datamodels.CalendarEvent
import com.example.gifttracker.datamodels.Event
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface CalendarEventDAO {

    @Query("SELECT * FROM CalendarEvent")
    fun getEvents(): LiveData<List<CalendarEvent>>


    @Insert(onConflict = REPLACE)
    fun insertEvent(event: CalendarEvent)

    @Delete
    suspend fun deleteEvent(event: CalendarEvent)

    @Update
    suspend fun updateEvent(event: CalendarEvent)
}