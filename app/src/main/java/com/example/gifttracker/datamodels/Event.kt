package com.example.gifttracker.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) val eventId : Int = 0,
    val name : String,
    val contactId : Int,
    val date : String
)
