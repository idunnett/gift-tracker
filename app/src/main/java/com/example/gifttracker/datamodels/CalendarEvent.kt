package com.example.gifttracker.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true)
    val eventId: Long = 0,
    val name : String = "",
    val description : String = "",
    val dtStart : Long = 0,
    val peoples : String = ""
){
    override fun toString(): String {
        return "CalendarEvent(eventId=$eventId, name='$name', description='$description', dtStart=$dtStart, peoples='$peoples')"
    }
}