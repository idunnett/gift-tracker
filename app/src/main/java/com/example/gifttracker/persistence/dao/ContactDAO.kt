package com.example.gifttracker.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gifttracker.datamodels.Contact
import com.example.gifttracker.datamodels.ContactWithEvents

@Dao
interface ContactDAO {

    @Query("SELECT * FROM Contact")
    fun getContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM Contact WHERE id = :contactID LIMIT 1")
    fun getContact(contactID: Int): LiveData<Contact>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Update
    suspend fun updateContact(contact: Contact)

    @Transaction
    @Query("SELECT * FROM Contact WHERE id = :contactID LIMIT 1")
    fun getContactsWithEvents(contactID: Int): LiveData<ContactWithEvents>
}