package com.example.gifttracker.persistence.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.gifttracker.datamodels.Contact
import com.example.gifttracker.datamodels.ContactWithEvents
import com.example.gifttracker.persistence.dao.ContactDAO

class ContactRepository(private val contactDAO: ContactDAO) {

    // Get contacts, after wrapping them in LiveData, by calling ContactDAO's getContacts method
    val contacts : LiveData<List<Contact>> = contactDAO.getContacts()

    // Get a LiveData object that wraps the contact that match our contactID,
    // In the end, we only need one contact, but the LiveData approach ensures thread safety
    fun getContact(contactID: Int): LiveData<Contact> {
        return contactDAO.getContact(contactID)
    }

    fun getContactWithEvents(contactID: Int): LiveData<ContactWithEvents> {
        return contactDAO.getContactsWithEvents(contactID)
    }

    // The insert function calls the ContactDAO's insertContact method in non-blocking way
    @WorkerThread
    suspend fun insertContact(contact : Contact) {
        contactDAO.insertContact(contact)
    }

    @WorkerThread
    suspend fun deleteContact(contact: Contact) {
        contactDAO.deleteContact(contact)
    }

    @WorkerThread
    suspend fun updateContact(contact: Contact) {
        contactDAO.updateContact(contact)
    }
}