package com.example.gifttracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.gifttracker.datamodels.Event
import com.example.gifttracker.datamodels.EventWithGifts
import com.example.gifttracker.persistence.database.GiftTrackerDatabase
import com.example.gifttracker.persistence.repositories.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventsViewModel(application: Application) : AndroidViewModel(application) {

    private val eventRepository : EventRepository
    val events : LiveData<List<Event>>

    // Initializes contacts by fetching them from the ContactRepository through the ContactDAO
    init {
        val eventsDAO = GiftTrackerDatabase.getDatabase(application).eventDAO()
        eventRepository = EventRepository(eventsDAO)
        events = eventRepository.events
    }

    fun getEvent(eventID: Int): LiveData<Event> {
        return eventRepository.getEvent(eventID)
    }

    fun getEventWithGifts(eventID: Int): LiveData<EventWithGifts> {
        return eventRepository.getEventWithGifts(eventID)
    }

    // Adds a new event to the data source, i.e. the database, by calling the EventRepository's insert method.
    // This method in turn calls the EventDAO's insert method.
    fun insertEvent(event : Event) = viewModelScope.launch(Dispatchers.IO) {
        eventRepository.insertEvent(event)
    }

    fun deleteEvent(event: Event) = viewModelScope.launch(Dispatchers.IO) {
        eventRepository.deleteEvent(event)
    }

    fun updateEvent(event: Event) = viewModelScope.launch(Dispatchers.IO) {
        eventRepository.updateEvent(event)
    }

}