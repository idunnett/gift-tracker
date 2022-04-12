package com.example.gifttracker.persistence.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.gifttracker.datamodels.ContactWithEvents
import com.example.gifttracker.datamodels.Event
import com.example.gifttracker.datamodels.EventWithGifts
import com.example.gifttracker.persistence.dao.EventDAO

class EventRepository(private val eventDAO: EventDAO) {

    val events : LiveData<List<Event>> = eventDAO.getEvents()

    fun getEvent(eventID: Int): LiveData<Event> {
        return eventDAO.getEvent(eventID)
    }

    fun getEventWithGifts(eventID: Int): LiveData<EventWithGifts> {
        return eventDAO.getEventWithGifts(eventID)
    }

    @WorkerThread
    suspend fun insertEvent(event : Event) {
        eventDAO.insertEvent(event)
    }

    @WorkerThread
    suspend fun deleteEvent(event: Event) {
        eventDAO.deleteEvent(event)
    }

    @WorkerThread
    suspend fun updateEvent(event: Event) {
        eventDAO.updateEvent(event)
    }
}