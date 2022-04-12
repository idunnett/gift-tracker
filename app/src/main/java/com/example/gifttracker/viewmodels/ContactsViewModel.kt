package com.example.gifttracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.gifttracker.datamodels.Contact
import com.example.gifttracker.datamodels.ContactWithEvents
import com.example.gifttracker.persistence.database.GiftTrackerDatabase
import com.example.gifttracker.persistence.repositories.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private val contactRepository : ContactRepository
    val contacts : LiveData<List<Contact>>

    // Initializes contacts by fetching them from the ContactRepository through the ContactDAO
    init {
        val contactsDAO = GiftTrackerDatabase.getDatabase(application).contactDAO()
        contactRepository = ContactRepository(contactsDAO)
        contacts = contactRepository.contacts
    }

    fun getContact(contactID: Int): LiveData<Contact> {
        return contactRepository.getContact(contactID)
    }

    fun getContactWithEvents(contactID: Int): LiveData<ContactWithEvents> {
        return contactRepository.getContactWithEvents(contactID)
    }

    // Adds a new contact to the data source, i.e. the database, by calling the ContactRepository's insert method.
    // This method in turn calls the ContactDAO's insert method.
    fun insertContact(contact : Contact) = viewModelScope.launch(Dispatchers.IO) {
        contactRepository.insertContact(contact)
    }

    fun deleteContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        contactRepository.deleteContact(contact)
    }

    fun updateContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        contactRepository.updateContact(contact)
    }

}