package com.example.gifttracker.datamodels

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class ContactWithEvents(
    @Embedded val contact: Contact,
    @Relation(
        parentColumn = "id",
        entityColumn = "contactId"
    ) val events: List<Event> = emptyList()
)
