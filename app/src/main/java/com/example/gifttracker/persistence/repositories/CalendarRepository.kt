package com.example.gifttracker.persistence.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.gifttracker.datamodels.CalendarEvent
import com.example.gifttracker.datamodels.Contact
import com.example.gifttracker.datamodels.ContactWithEvents
import com.example.gifttracker.datamodels.Event
import com.example.gifttracker.persistence.dao.CalendarEventDAO
import com.example.gifttracker.persistence.dao.ContactDAO

class CalendarRepository(private val contactDAO: CalendarEventDAO) {
    val events : LiveData<List<CalendarEvent>> = contactDAO.getEvents()

    // The insert function calls the ContactDAO's insertContact method in non-blocking way

    fun insertEvent(contact : CalendarEvent) {
        contactDAO.insertEvent(contact)
    }

    @WorkerThread
    suspend fun deleteEvent(contact: CalendarEvent) {
        contactDAO.deleteEvent(contact)
    }

    @WorkerThread
    suspend fun updateEvent(contact: CalendarEvent) {
        contactDAO.updateEvent(contact)
    }
}